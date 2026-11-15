package com.example.ui

import android.app.Application
import android.graphics.Bitmap
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.data.AdAiRepository
import com.example.data.AdBadgePosition
import com.example.data.AdBadgeStyle
import com.example.data.AdCampaignPreset
import com.example.data.AdCategory
import com.example.data.AdLighting
import com.example.data.AdRatio
import com.example.data.GeneratedPhotoAd
import com.example.data.LocalPortfolioStore
import com.example.data.UserPreferences
import com.example.data.VideoAdCampaign
import com.example.data.VideoScene
import java.util.Locale
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

private const val TAG = "AdViewModel"

sealed interface PhotoGenState {
    data object Idle : PhotoGenState
    data class Generating(val stepMessage: String) : PhotoGenState
    data class Success(val photoAd: GeneratedPhotoAd) : PhotoGenState
    data class Error(val message: String) : PhotoGenState
}

sealed interface VideoGenState {
    data object Idle : VideoGenState
    data class Generating(val stepMessage: String) : VideoGenState
    data class Success(val campaign: VideoAdCampaign) : VideoGenState
    data class Error(val message: String) : VideoGenState
}

data class VideoPlayerState(
    val isPlaying: Boolean = false,
    val currentSceneIndex: Int = 0,
    val sceneProgress: Float = 0f,
    val playbackRatio: AdRatio = AdRatio.STORY_REEL
)

class AdViewModel @JvmOverloads constructor(
    application: Application,
    private val userPrefs: UserPreferences = UserPreferences(application),
    private val repository: AdAiRepository = AdAiRepository(application) { userPrefs.customApiKey },
    private val portfolioStore: LocalPortfolioStore = LocalPortfolioStore(application)
) : AndroidViewModel(application) {

    // --- User & Pro Account State ---
    private val _isPro = MutableStateFlow(userPrefs.isPro)
    val isPro: StateFlow<Boolean> = _isPro.asStateFlow()

    private val _creditsRemaining = MutableStateFlow(userPrefs.getDailyCredits())
    val creditsRemaining: StateFlow<Int> = _creditsRemaining.asStateFlow()

    private val _showProPaywall = MutableStateFlow(false)
    val showProPaywall: StateFlow<Boolean> = _showProPaywall.asStateFlow()

    private val _customApiKey = MutableStateFlow(userPrefs.customApiKey)
    val customApiKey: StateFlow<String> = _customApiKey.asStateFlow()

    private val _showWatermark = MutableStateFlow(userPrefs.showWatermark)
    val showWatermark: StateFlow<Boolean> = _showWatermark.asStateFlow()

    // --- Photo Ad Studio State ---
    private val _photoPrompt = MutableStateFlow("Flacon de parfum de luxe noir et doré sur marbre sombre avec brume dorée")
    val photoPrompt: StateFlow<String> = _photoPrompt.asStateFlow()

    private val _photoCategory = MutableStateFlow(AdCategory.LUXURY)
    val photoCategory: StateFlow<AdCategory> = _photoCategory.asStateFlow()

    private val _photoLighting = MutableStateFlow(AdLighting.STUDIO_SOFTBOX)
    val photoLighting: StateFlow<AdLighting> = _photoLighting.asStateFlow()

    private val _photoRatio = MutableStateFlow(AdRatio.SQUARE)
    val photoRatio: StateFlow<AdRatio> = _photoRatio.asStateFlow()

    private val _brandName = MutableStateFlow("L'ÉLIXIR PARIS")
    val brandName: StateFlow<String> = _brandName.asStateFlow()

    private val _badgeText = MutableStateFlow("NOUVEAU")
    val badgeText: StateFlow<String> = _badgeText.asStateFlow()

    private val _badgeStyle = MutableStateFlow(AdBadgeStyle.RUBY_RED)
    val badgeStyle: StateFlow<AdBadgeStyle> = _badgeStyle.asStateFlow()

    private val _badgePosition = MutableStateFlow(AdBadgePosition.TOP_LEFT)
    val badgePosition: StateFlow<AdBadgePosition> = _badgePosition.asStateFlow()

    private val _headline = MutableStateFlow("L'Élégance Absolue")
    val headline: StateFlow<String> = _headline.asStateFlow()

    private val _sloganSuggestions = MutableStateFlow<List<String>>(emptyList())
    val sloganSuggestions: StateFlow<List<String>> = _sloganSuggestions.asStateFlow()

    private val _isGeneratingSlogans = MutableStateFlow(false)
    val isGeneratingSlogans: StateFlow<Boolean> = _isGeneratingSlogans.asStateFlow()

    private val _photoState = MutableStateFlow<PhotoGenState>(PhotoGenState.Idle)
    val photoState: StateFlow<PhotoGenState> = _photoState.asStateFlow()

    // --- Video Ad Studio State ---
    private val _videoBrief = MutableStateFlow("Boisson énergisante naturelle aux baies sauvages pour sportifs urbains")
    val videoBrief: StateFlow<String> = _videoBrief.asStateFlow()

    private val _videoCategory = MutableStateFlow(AdCategory.FOOD)
    val videoCategory: StateFlow<AdCategory> = _videoCategory.asStateFlow()

    private val _videoAudience = MutableStateFlow("18-35 ans, sportifs & créatifs")
    val videoAudience: StateFlow<String> = _videoAudience.asStateFlow()

    private val _videoTone = MutableStateFlow("Énergique, captivant, moderne")
    val videoTone: StateFlow<String> = _videoTone.asStateFlow()

    private val _videoState = MutableStateFlow<VideoGenState>(VideoGenState.Idle)
    val videoState: StateFlow<VideoGenState> = _videoState.asStateFlow()

    // --- Video Simulator State & Real Audio Voiceover ---
    private val _playerState = MutableStateFlow(VideoPlayerState())
    val playerState: StateFlow<VideoPlayerState> = _playerState.asStateFlow()
    private var playbackJob: Job? = null

    private var tts: TextToSpeech? = null
    private var isTtsReady = false
    private val _isVoiceoverAudioEnabled = MutableStateFlow(true)
    val isVoiceoverAudioEnabled: StateFlow<Boolean> = _isVoiceoverAudioEnabled.asStateFlow()

    init {
        try {
            tts = TextToSpeech(application) { status ->
                if (status == TextToSpeech.SUCCESS) {
                    val result = tts?.setLanguage(Locale.FRENCH)
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        val fallback = tts?.setLanguage(Locale.getDefault())
                        isTtsReady = fallback != TextToSpeech.LANG_MISSING_DATA && fallback != TextToSpeech.LANG_NOT_SUPPORTED
                    } else {
                        isTtsReady = true
                    }
                }
            }
        } catch (e: Throwable) {
            Log.e(TAG, "Erreur TTS", e)
        }
        viewModelScope.launch {
            val loadedPhotos = portfolioStore.loadPhotos()
            if (loadedPhotos.isNotEmpty()) {
                _savedPhotos.update { current ->
                    (current + loadedPhotos).distinctBy { it.id }
                }
            }
            val loadedCampaigns = portfolioStore.loadCampaigns()
            if (loadedCampaigns.isNotEmpty()) {
                _savedCampaigns.update { current ->
                    (current + loadedCampaigns).distinctBy { it.id }
                }
            }
        }
    }

    // --- Saved Creations ---
    private val _savedPhotos = MutableStateFlow<List<GeneratedPhotoAd>>(emptyList())
    val savedPhotos: StateFlow<List<GeneratedPhotoAd>> = _savedPhotos.asStateFlow()

    private val _savedCampaigns = MutableStateFlow<List<VideoAdCampaign>>(emptyList())
    val savedCampaigns: StateFlow<List<VideoAdCampaign>> = _savedCampaigns.asStateFlow()

    // Setters
    fun updatePhotoPrompt(prompt: String) { _photoPrompt.value = prompt }
    fun updatePhotoCategory(cat: AdCategory) { _photoCategory.value = cat }
    fun updatePhotoLighting(lighting: AdLighting) { _photoLighting.value = lighting }
    fun updatePhotoRatio(ratio: AdRatio) { _photoRatio.value = ratio }
    fun updateBrandName(name: String) { _brandName.value = name }
    fun updateBadgeText(badge: String) { _badgeText.value = badge }
    fun updateBadgeStyle(style: AdBadgeStyle) { _badgeStyle.value = style }
    fun updateBadgePosition(pos: AdBadgePosition) { _badgePosition.value = pos }
    fun updateHeadline(text: String) { _headline.value = text }

    fun updateVideoBrief(brief: String) { _videoBrief.value = brief }
    fun updateVideoCategory(cat: AdCategory) { _videoCategory.value = cat }
    fun updateVideoAudience(aud: String) { _videoAudience.value = aud }
    fun updateVideoTone(tone: String) { _videoTone.value = tone }
    fun setPlaybackRatio(ratio: AdRatio) { _playerState.update { it.copy(playbackRatio = ratio) } }

    fun toggleVoiceoverAudio() {
        _isVoiceoverAudioEnabled.update { !it }
        if (!_isVoiceoverAudioEnabled.value) {
            tts?.stop()
        }
    }

    fun generateMarketingSlogans() {
        viewModelScope.launch {
            _isGeneratingSlogans.value = true
            try {
                val slogans = repository.generateCatchySlogans(_photoPrompt.value, _photoCategory.value)
                _sloganSuggestions.value = slogans
            } catch (e: Exception) {
                Log.e(TAG, "Erreur génération slogans", e)
            } finally {
                _isGeneratingSlogans.value = false
            }
        }
    }

    // --- Account & Pro Plan Management ---
    fun openProPaywall() { _showProPaywall.value = true }
    fun closeProPaywall() { _showProPaywall.value = false }

    fun activateProSubscription() {
        userPrefs.isPro = true
        _isPro.value = true
        _showWatermark.value = false
        userPrefs.showWatermark = false
        _creditsRemaining.value = 999
        _showProPaywall.value = false
    }

    fun saveCustomApiKey(key: String) {
        userPrefs.customApiKey = key.trim()
        _customApiKey.value = key.trim()
        _creditsRemaining.value = userPrefs.getDailyCredits()
    }

    fun toggleWatermark(enabled: Boolean) {
        userPrefs.showWatermark = enabled
        _showWatermark.value = enabled
    }

    fun addBonusCredits(amount: Int) {
        userPrefs.addBonusCredits(amount)
        _creditsRemaining.value = userPrefs.getDailyCredits()
    }

    fun applyPreset(preset: AdCampaignPreset, targetStudio: String = "photo") {
        if (targetStudio == "photo") {
            _photoPrompt.value = preset.prompt
            _photoCategory.value = preset.category
            _photoRatio.value = preset.suggestedRatio
            _headline.value = preset.title
            _brandName.value = preset.title.uppercase()
        } else {
            _videoBrief.value = preset.description
            _videoCategory.value = preset.category
        }
    }

    fun generatePhotoAd() {
        val prompt = _photoPrompt.value.trim()
        if (prompt.isBlank()) return

        if (!userPrefs.consumeCredit()) {
            _showProPaywall.value = true
            return
        }
        _creditsRemaining.value = userPrefs.getDailyCredits()

        val category = _photoCategory.value
        val lighting = _photoLighting.value
        val ratio = _photoRatio.value
        val brand = _brandName.value
        val badge = _badgeText.value
        val style = _badgeStyle.value
        val pos = _badgePosition.value
        val head = _headline.value

        viewModelScope.launch {
            _photoState.value = PhotoGenState.Generating("Sublimation du prompt publicitaire studio...")
            try {
                val enhancedPrompt = repository.buildEnhancedAdPrompt(prompt, category, lighting, ratio)
                _photoState.value = PhotoGenState.Generating("Génération de la photo publicitaire par l'IA (30-60s)...")

                val bitmap = repository.generateAdPhoto(enhancedPrompt)
                val photoAd = GeneratedPhotoAd(
                    prompt = prompt,
                    enhancedPrompt = enhancedPrompt,
                    category = category,
                    ratio = ratio,
                    bitmap = bitmap,
                    brandName = brand,
                    badgeText = badge,
                    badgeStyle = style,
                    badgePosition = pos,
                    headline = head
                )
                _photoState.value = PhotoGenState.Success(photoAd)
                var updatedList: List<GeneratedPhotoAd> = emptyList()
                _savedPhotos.update { cur ->
                    (listOf(photoAd) + cur).also { updatedList = it }
                }
                portfolioStore.savePhotos(updatedList)
            } catch (e: TimeoutCancellationException) {
                Log.e(TAG, "Génération photo expirée", e)
                userPrefs.refundCredit()
                _creditsRemaining.value = userPrefs.getDailyCredits()
                _photoState.value = PhotoGenState.Error("Délai dépassé (60s). Veuillez réessayer.")
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "Erreur génération photo pub", e)
                userPrefs.refundCredit()
                _creditsRemaining.value = userPrefs.getDailyCredits()
                _photoState.value = PhotoGenState.Error(e.localizedMessage ?: "Erreur de génération d'image.")
            }
        }
    }

    fun generateVideoCampaign() {
        val brief = _videoBrief.value.trim()
        if (brief.isBlank()) return

        if (!userPrefs.consumeCredit()) {
            _showProPaywall.value = true
            return
        }
        _creditsRemaining.value = userPrefs.getDailyCredits()

        val category = _videoCategory.value
        val audience = _videoAudience.value
        val tone = _videoTone.value

        viewModelScope.launch {
            _videoState.value = VideoGenState.Generating("Écriture du script et réalisation du storyboard IA...")
            try {
                val campaign = repository.generateVideoCampaign(brief, category, audience, tone)
                _videoState.value = VideoGenState.Success(campaign)
                var updatedCampaigns: List<VideoAdCampaign> = emptyList()
                _savedCampaigns.update { cur ->
                    (listOf(campaign) + cur).also { updatedCampaigns = it }
                }
                portfolioStore.saveCampaigns(updatedCampaigns)
                resetVideoPlayback()
            } catch (e: TimeoutCancellationException) {
                Log.e(TAG, "Génération vidéo expirée", e)
                userPrefs.refundCredit()
                _creditsRemaining.value = userPrefs.getDailyCredits()
                _videoState.value = VideoGenState.Error("Délai de réflexion dépassé (60s). Veuillez réessayer.")
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "Erreur génération vidéo pub", e)
                userPrefs.refundCredit()
                _creditsRemaining.value = userPrefs.getDailyCredits()
                _videoState.value = VideoGenState.Error(e.localizedMessage ?: "Échec de création du scénario vidéo.")
            }
        }
    }

    fun generateSceneVisual(sceneIndex: Int) {
        val currentState = _videoState.value
        if (currentState !is VideoGenState.Success) return

        val campaignId = currentState.campaign.id
        val initialScene = currentState.campaign.scenes.getOrNull(sceneIndex) ?: return

        viewModelScope.launch {
            updateSceneAt(campaignId, sceneIndex) { it.copy(isGeneratingBitmap = true) }

            try {
                val bitmap = repository.generateSceneKeyframe(initialScene.aiVideoPrompt)
                updateSceneAt(campaignId, sceneIndex) {
                    it.copy(sceneBitmap = bitmap, isGeneratingBitmap = false)
                }
                val latestCampaign = (_videoState.value as? VideoGenState.Success)?.campaign
                if (latestCampaign != null && latestCampaign.id == campaignId) {
                    var updatedList: List<VideoAdCampaign> = emptyList()
                    _savedCampaigns.update { cur ->
                        cur.map { if (it.id == campaignId) latestCampaign else it }.also { updatedList = it }
                    }
                    portfolioStore.saveCampaigns(updatedList, updatedCampaignId = campaignId)
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "Échec visuel de scène", e)
                updateSceneAt(campaignId, sceneIndex) { it.copy(isGeneratingBitmap = false) }
            }
        }
    }

    private fun updateSceneAt(
        campaignId: String,
        sceneIndex: Int,
        transform: (VideoScene) -> VideoScene
    ) {
        _videoState.update { state ->
            if (state is VideoGenState.Success && state.campaign.id == campaignId && sceneIndex in state.campaign.scenes.indices) {
                val updatedScenes = state.campaign.scenes.toMutableList()
                updatedScenes[sceneIndex] = transform(updatedScenes[sceneIndex])
                VideoGenState.Success(state.campaign.copy(scenes = updatedScenes))
            } else {
                state
            }
        }
    }

    // --- Dynamic Video Ad Player Simulation Controls ---
    fun togglePlayPause() {
        if (_playerState.value.isPlaying) {
            pauseVideoPlayback()
        } else {
            startVideoPlayback()
        }
    }

    private fun speakCurrentScene(scene: VideoScene) {
        if (_isVoiceoverAudioEnabled.value && isTtsReady && scene.voiceover.isNotBlank()) {
            tts?.speak(scene.voiceover, TextToSpeech.QUEUE_FLUSH, null, "scene_${scene.sceneNumber}")
        }
    }

    fun startVideoPlayback() {
        val currentCampaign = (_videoState.value as? VideoGenState.Success)?.campaign ?: return
        if (currentCampaign.scenes.isEmpty()) return

        playbackJob?.cancel()
        _playerState.update { it.copy(isPlaying = true) }

        // Speak initial scene
        val initialScene = currentCampaign.scenes.getOrNull(_playerState.value.currentSceneIndex)
        if (initialScene != null) {
            speakCurrentScene(initialScene)
        }

        playbackJob = viewModelScope.launch {
            while (isActive && _playerState.value.isPlaying) {
                val activeCampaign = (_videoState.value as? VideoGenState.Success)?.campaign ?: break
                val sceneIndex = _playerState.value.currentSceneIndex
                val scene = activeCampaign.scenes.getOrNull(sceneIndex) ?: break
                val sceneDurationMs = (scene.durationSeconds.coerceAtLeast(2)) * 1000L
                val intervalMs = 50L
                val step = intervalMs.toFloat() / sceneDurationMs.toFloat()

                var soughtExternally = false
                while (isActive && _playerState.value.isPlaying) {
                    val current = _playerState.value
                    if (current.currentSceneIndex != sceneIndex) {
                        soughtExternally = true
                        break
                    }
                    if (current.sceneProgress >= 1f) break
                    delay(intervalMs)
                    val afterDelay = _playerState.value
                    if (!afterDelay.isPlaying) break
                    if (afterDelay.currentSceneIndex != sceneIndex) {
                        soughtExternally = true
                        break
                    }
                    val nextProgress = (afterDelay.sceneProgress + step).coerceAtMost(1f)
                    _playerState.update { it.copy(sceneProgress = nextProgress) }
                }

                if (!_playerState.value.isPlaying) break
                if (soughtExternally) continue

                val nextIndex = if (sceneIndex + 1 < activeCampaign.scenes.size) sceneIndex + 1 else 0
                _playerState.update {
                    it.copy(
                        currentSceneIndex = nextIndex,
                        sceneProgress = 0f
                    )
                }
                activeCampaign.scenes.getOrNull(nextIndex)?.let { speakCurrentScene(it) }
            }
        }
    }

    fun pauseVideoPlayback() {
        playbackJob?.cancel()
        tts?.stop()
        _playerState.update { it.copy(isPlaying = false) }
    }

    fun resetVideoPlayback() {
        playbackJob?.cancel()
        tts?.stop()
        _playerState.update {
            it.copy(
                isPlaying = false,
                currentSceneIndex = 0,
                sceneProgress = 0f
            )
        }
    }

    fun seekToScene(index: Int) {
        val currentCampaign = (_videoState.value as? VideoGenState.Success)?.campaign ?: return
        if (index in currentCampaign.scenes.indices) {
            _playerState.update {
                it.copy(
                    currentSceneIndex = index,
                    sceneProgress = 0f
                )
            }
            if (_playerState.value.isPlaying) {
                val scene = currentCampaign.scenes[index]
                speakCurrentScene(scene)
            }
        }
    }

    fun loadPhotoIntoStudio(photoAd: GeneratedPhotoAd) {
        _photoPrompt.value = photoAd.prompt
        _photoCategory.value = photoAd.category
        _photoRatio.value = photoAd.ratio
        _brandName.value = photoAd.brandName
        _badgeText.value = photoAd.badgeText
        _badgeStyle.value = photoAd.badgeStyle
        _badgePosition.value = photoAd.badgePosition
        _headline.value = photoAd.headline
        _photoState.value = PhotoGenState.Success(photoAd)
    }

    fun loadCampaignIntoStudio(campaign: VideoAdCampaign) {
        _videoState.value = VideoGenState.Success(campaign)
        resetVideoPlayback()
    }

    fun removeSavedPhoto(id: String) {
        val updated = _savedPhotos.value.filterNot { item -> item.id == id }
        _savedPhotos.value = updated
        viewModelScope.launch {
            portfolioStore.savePhotos(updated)
        }
    }

    fun removeSavedCampaign(id: String) {
        val updated = _savedCampaigns.value.filterNot { item -> item.id == id }
        _savedCampaigns.value = updated
        viewModelScope.launch {
            portfolioStore.saveCampaigns(updated)
        }
    }

    override fun onCleared() {
        super.onCleared()
        playbackJob?.cancel()
        tts?.stop()
        tts?.shutdown()
    }

    companion object {
        fun Factory(application: Application): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                AdViewModel(application)
            }
        }
    }
}
