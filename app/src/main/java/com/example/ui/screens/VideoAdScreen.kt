package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.material.icons.filled.Check
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.AdCategory
import com.example.data.AdRatio
import com.example.data.VideoAdCampaign
import com.example.data.VideoScene
import com.example.ui.AdViewModel
import com.example.ui.VideoGenState
import com.example.ui.VideoPlayerState
import com.example.ui.theme.AmberGold
import com.example.ui.theme.BadgeGold
import com.example.ui.theme.BadgeRed
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.ElectricViolet
import com.example.ui.theme.StudioBorder
import com.example.ui.theme.StudioCard
import com.example.ui.theme.StudioCardElevated
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun VideoAdScreen(
    viewModel: AdViewModel,
    modifier: Modifier = Modifier
) {
    val brief by viewModel.videoBrief.collectAsStateWithLifecycle()
    val category by viewModel.videoCategory.collectAsStateWithLifecycle()
    val audience by viewModel.videoAudience.collectAsStateWithLifecycle()
    val tone by viewModel.videoTone.collectAsStateWithLifecycle()
    val state by viewModel.videoState.collectAsStateWithLifecycle()
    val playerState by viewModel.playerState.collectAsStateWithLifecycle()
    val isVoiceoverAudioEnabled by viewModel.isVoiceoverAudioEnabled.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = StudioCard),
            border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(ElectricViolet.copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Videocam,
                            contentDescription = null,
                            tint = ElectricViolet
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Directeur Vidéo Pub IA",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Scénario, plans de caméra, voix-off & prompts vidéo IA",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }
            }
        }

        // Brief Input
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = StudioCard),
            border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Description de la campagne vidéo",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = brief,
                    onValueChange = { viewModel.updateVideoBrief(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("video_brief_input"),
                    minLines = 2,
                    maxLines = 4,
                    placeholder = {
                        Text(
                            "Ex: Publicité courte pour une boisson énergisante naturelle aux fruits rouges pour sportifs urbains...",
                            color = TextSecondary.copy(alpha = 0.6f)
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ElectricViolet,
                        unfocusedBorderColor = StudioBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedContainerColor = StudioCardElevated,
                        unfocusedContainerColor = StudioCardElevated
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Quick suggestions
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val ideas = listOf(
                        "Jus Frais Bissap",
                        "Promo Faso Dan Fani",
                        "Boutique Tech Ouaga",
                        "Karité Bio du Burkina"
                    )
                    ideas.forEach { idea ->
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable {
                                    val full = when (idea) {
                                        "Jus Frais Bissap" -> "Publicité fraîche et dynamique pour des jus naturels de bissap et gingembre livrés à Ouagadougou"
                                        "Promo Faso Dan Fani" -> "Teaser mode élégant pour nouvelle collection en pagne tissé Faso Dan Fani moderne"
                                        "Boutique Tech Ouaga" -> "Spot pub court pour smartphones et accessoires avec paiement Mobile Money et livraison rapide"
                                        else -> "Publicité sensorielle et naturelle pour du beurre de karité pur du Burkina Faso"
                                    }
                                    viewModel.updateVideoBrief(full)
                                },
                            color = StudioCardElevated,
                            border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder)
                        ) {
                            Text(
                                text = "+ $idea",
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                style = MaterialTheme.typography.bodySmall,
                                color = ElectricViolet
                            )
                        }
                    }
                }
            }
        }

        // Category Selector (wired to ViewModel videoCategory)
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = StudioCard),
            border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Secteur de la campagne",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AdCategory.entries.forEach { cat ->
                        val isSelected = category == cat
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .clickable { viewModel.updateVideoCategory(cat) },
                            color = if (isSelected) ElectricViolet else StudioCardElevated,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) ElectricViolet else StudioBorder
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                }
                                Text(
                                    text = cat.displayName,
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) Color.White else TextPrimary
                                )
                            }
                        }
                    }
                }
            }
        }

        // Audience & Tone
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = StudioCard),
            border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Cible & Tonalité",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = audience,
                        onValueChange = { viewModel.updateVideoAudience(it) },
                        label = { Text("Public cible") },
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ElectricViolet,
                            unfocusedBorderColor = StudioBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = tone,
                        onValueChange = { viewModel.updateVideoTone(it) },
                        label = { Text("Ton de la pub") },
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ElectricViolet,
                            unfocusedBorderColor = StudioBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        singleLine = true
                    )
                }
            }
        }

        // Action Button
        Button(
            onClick = { viewModel.generateVideoCampaign() },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .testTag("generate_video_button"),
            colors = ButtonDefaults.buttonColors(
                containerColor = ElectricViolet,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp),
            enabled = state !is VideoGenState.Generating
        ) {
            Icon(
                imageVector = Icons.Default.Movie,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (state is VideoGenState.Generating) "Écriture & Réalisation en cours..." else "Générer la Vidéo Publicitaire IA",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }

        // Status / Loading / Result
        when (val curr = state) {
            is VideoGenState.Generating -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = StudioCard),
                    border = androidx.compose.foundation.BorderStroke(1.dp, ElectricViolet),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(color = ElectricViolet)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = curr.stepMessage,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = TextPrimary,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Création du storyboard shot-by-shot, des prompts pour moteurs vidéo et de la voix-off avec AdGen Studio & Gemini IA",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            is VideoGenState.Error -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = BadgeRed.copy(alpha = 0.15f)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BadgeRed),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.ErrorOutline, contentDescription = null, tint = BadgeRed)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Erreur lors de la réalisation", fontWeight = FontWeight.Bold, color = BadgeRed)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = curr.message, style = MaterialTheme.typography.bodySmall, color = TextPrimary)
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedButton(
                            onClick = { viewModel.generateVideoCampaign() },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = ElectricViolet),
                            border = androidx.compose.foundation.BorderStroke(1.dp, ElectricViolet)
                        ) {
                            Text("Réessayer")
                        }
                    }
                }
            }

            is VideoGenState.Success -> {
                val campaign = curr.campaign

                // Section 1: Dynamic Video Player Simulator
                DynamicVideoPlayerCard(
                    campaign = campaign,
                    playerState = playerState,
                    isVoiceoverAudioEnabled = isVoiceoverAudioEnabled,
                    onTogglePlay = { viewModel.togglePlayPause() },
                    onToggleAudio = { viewModel.toggleVoiceoverAudio() },
                    onPrevScene = {
                        val prev = (playerState.currentSceneIndex - 1).coerceAtLeast(0)
                        viewModel.seekToScene(prev)
                    },
                    onNextScene = {
                        val next = (playerState.currentSceneIndex + 1).coerceAtMost(campaign.scenes.size - 1)
                        viewModel.seekToScene(next)
                    },
                    onReset = { viewModel.resetVideoPlayback() },
                    onRatioChange = { viewModel.setPlaybackRatio(it) }
                )

                // Section 2: Detailed Shot-by-Shot Storyboard Grid
                StoryboardScenesList(
                    campaign = campaign,
                    onGenerateVisual = { sceneIndex -> viewModel.generateSceneVisual(sceneIndex) },
                    onCopyScenePrompt = { prompt ->
                        clipboardManager.setText(AnnotatedString(prompt))
                        Toast.makeText(context, "Prompt vidéo copié pour Veo / Sora !", Toast.LENGTH_SHORT).show()
                    }
                )
            }

            is VideoGenState.Idle -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = StudioCard.copy(alpha = 0.5f)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "🎬 Production Vidéo Complète",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = ElectricViolet
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "L'application génère un scénario minute par minute pour formats courts (TikTok, Reels, Shorts), les mouvements de caméra cinéma, les prompts techniques pour générateurs de vidéo IA (Veo, Sora, Runway) et un simulateur de visionnage interactif.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

/**
 * Dynamic animated Video Ad Player Simulator with animated camera movement, captions, and ratio switcher
 */
@Composable
fun DynamicVideoPlayerCard(
    campaign: VideoAdCampaign,
    playerState: VideoPlayerState,
    isVoiceoverAudioEnabled: Boolean,
    onTogglePlay: () -> Unit,
    onToggleAudio: () -> Unit,
    onPrevScene: () -> Unit,
    onNextScene: () -> Unit,
    onReset: () -> Unit,
    onRatioChange: (AdRatio) -> Unit
) {
    val currentScene = campaign.scenes.getOrNull(playerState.currentSceneIndex) ?: return

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = StudioCard),
        border = androidx.compose.foundation.BorderStroke(1.dp, ElectricViolet),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header with campaign title and ratio toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                ) {
                    Text(
                        text = campaign.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = AmberGold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "Simulateur Vidéo Ad • ${campaign.totalDurationSeconds}s",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary
                    )
                }

                // Ratio selector
                Row(
                    modifier = Modifier
                        .background(StudioCardElevated, RoundedCornerShape(8.dp))
                        .padding(2.dp)
                ) {
                    val ratios = listOf(AdRatio.STORY_REEL, AdRatio.SQUARE, AdRatio.LANDSCAPE)
                    ratios.forEach { r ->
                        val isSelected = playerState.playbackRatio == r
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .clickable { onRatioChange(r) },
                            color = if (isSelected) ElectricViolet else Color.Transparent
                        ) {
                            Text(
                                text = when (r) {
                                    AdRatio.STORY_REEL -> "9:16"
                                    AdRatio.SQUARE -> "1:1"
                                    else -> "16:9"
                                },
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Color.White else TextSecondary
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // The Video Player Canvas with camera motion animation & studio viewfinder HUD
            val aspect = playerState.playbackRatio.widthRatio / playerState.playbackRatio.heightRatio
            val scaleAnim by animateFloatAsState(
                targetValue = if (playerState.isPlaying) 1.05f + (playerState.sceneProgress * 0.12f) else 1f,
                animationSpec = tween(durationMillis = 200),
                label = "camera_motion"
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(aspect.coerceIn(0.65f, 1.77f))
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Black)
            ) {
                // Background visual: either AI generated scene keyframe or stylized gradient
                if (currentScene.sceneBitmap != null) {
                    Image(
                        bitmap = currentScene.sceneBitmap!!.asImageBitmap(),
                        contentDescription = currentScene.visualDescription,
                        modifier = Modifier
                            .fillMaxSize()
                            .scale(scaleAnim),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    // Stylized dynamic commercial stage
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .scale(scaleAnim)
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(
                                        when (playerState.currentSceneIndex % 3) {
                                            0 -> ElectricViolet.copy(alpha = 0.5f)
                                            1 -> CyberCyan.copy(alpha = 0.4f)
                                            else -> AmberGold.copy(alpha = 0.45f)
                                        },
                                        StudioCardElevated,
                                        Color.Black
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Movie,
                                contentDescription = null,
                                tint = AmberGold.copy(alpha = 0.7f),
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Plan ${currentScene.sceneNumber} : ${currentScene.cameraMovement}",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = currentScene.visualDescription,
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary,
                                textAlign = TextAlign.Center,
                                maxLines = 3
                            )
                        }
                    }
                }

                // Authentic Studio Viewfinder Corner Brackets
                ViewfinderBrackets()

                // Top Vignette
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Black.copy(alpha = 0.8f), Color.Transparent)
                            )
                        )
                )

                // Top Status Bar: Scene indicator & Camera movement
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .align(Alignment.TopCenter),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = Color.Black.copy(alpha = 0.7f),
                        shape = RoundedCornerShape(6.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (playerState.isPlaying) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .background(BadgeRed, CircleShape)
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = "REC ",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = BadgeRed,
                                    fontSize = 9.sp
                                )
                            }
                            Text(
                                text = "PLAN ${currentScene.sceneNumber}/${campaign.scenes.size} • ${currentScene.timeCode}",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = AmberGold,
                                maxLines = 1
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Surface(
                        modifier = Modifier.weight(1f, fill = false),
                        color = ElectricViolet.copy(alpha = 0.85f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = currentScene.cameraMovement,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // Bottom Vignette
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .align(Alignment.BottomCenter)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.9f))
                            )
                        )
                )

                // Subtitles & Voiceover Box at bottom of player
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Overlay Headline
                    if (currentScene.textOverlay.isNotBlank()) {
                        Surface(
                            color = AmberGold,
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = currentScene.textOverlay.uppercase(),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.Black
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                    }

                    // Voiceover narration subtitles with audio state indicator
                    Row(
                        modifier = Modifier
                            .background(Color.Black.copy(alpha = 0.75f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (isVoiceoverAudioEnabled) Icons.Default.GraphicEq else Icons.AutoMirrored.Filled.VolumeOff,
                            contentDescription = null,
                            tint = if (isVoiceoverAudioEnabled) CyberCyan else TextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "\"${currentScene.voiceover}\"",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Segmented Progress Bar across scenes
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                campaign.scenes.forEachIndexed { idx, _ ->
                    val isPast = idx < playerState.currentSceneIndex
                    val isCurrent = idx == playerState.currentSceneIndex
                    val prog = when {
                        isPast -> 1f
                        isCurrent -> playerState.sceneProgress
                        else -> 0f
                    }
                    LinearProgressIndicator(
                        progress = { prog },
                        modifier = Modifier
                            .weight(1f)
                            .height(4.dp)
                            .clip(CircleShape),
                        color = AmberGold,
                        trackColor = StudioBorder
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Player Controls Row with Audio Mute/Unmute
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onPrevScene) {
                    Icon(imageVector = Icons.Default.SkipPrevious, contentDescription = "Plan précédent", tint = TextPrimary)
                }

                IconButton(
                    onClick = onTogglePlay,
                    modifier = Modifier
                        .size(48.dp)
                        .background(AmberGold, CircleShape)
                ) {
                    Icon(
                        imageVector = if (playerState.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (playerState.isPlaying) "Pause" else "Lecture",
                        tint = Color.Black,
                        modifier = Modifier.size(28.dp)
                    )
                }

                IconButton(onClick = onNextScene) {
                    Icon(imageVector = Icons.Default.SkipNext, contentDescription = "Plan suivant", tint = TextPrimary)
                }

                IconButton(onClick = onToggleAudio) {
                    Icon(
                        imageVector = if (isVoiceoverAudioEnabled) Icons.AutoMirrored.Filled.VolumeUp else Icons.AutoMirrored.Filled.VolumeOff,
                        contentDescription = if (isVoiceoverAudioEnabled) "Couper voix-off" else "Activer voix-off",
                        tint = if (isVoiceoverAudioEnabled) CyberCyan else TextSecondary
                    )
                }

                IconButton(onClick = onReset) {
                    Icon(imageVector = Icons.Default.Replay, contentDescription = "Recommencer", tint = TextSecondary)
                }
            }
        }
    }
}

/**
 * Storyboard Cards with scene details, AI video prompts, and keyframe generator
 */
@Composable
fun StoryboardScenesList(
    campaign: VideoAdCampaign,
    onGenerateVisual: (Int) -> Unit,
    onCopyScenePrompt: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Découpage & Prompts IA Vidéo",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Surface(
                color = ElectricViolet.copy(alpha = 0.2f),
                shape = RoundedCornerShape(6.dp)
            ) {
                Text(
                    text = "${campaign.scenes.size} Plans • ${campaign.totalDurationSeconds}s",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = ElectricViolet
                )
            }
        }

        campaign.scenes.forEachIndexed { index, scene ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = StudioCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    // Header of Scene
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                color = AmberGold,
                                shape = CircleShape,
                                modifier = Modifier.size(24.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = "${scene.sceneNumber}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = Color.Black
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Plan ${scene.sceneNumber} (${scene.timeCode})",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        }

                        Surface(
                            color = StudioCardElevated,
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "${scene.durationSeconds}s",
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSecondary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Visual description
                    Text(
                        text = "Visuel : ${scene.visualDescription}",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextPrimary
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Camera movement
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "🎥 Caméra : ",
                            style = MaterialTheme.typography.labelSmall,
                            color = AmberGold
                        )
                        Text(
                            text = scene.cameraMovement,
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Voiceover
                    Row(verticalAlignment = Alignment.Top) {
                        Text(
                            text = "🎙️ Voix-off : ",
                            style = MaterialTheme.typography.labelSmall,
                            color = CyberCyan
                        )
                        Text(
                            text = "\"${scene.voiceover}\"",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium,
                            color = TextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Sound / Music
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "🎵 Son : ",
                            style = MaterialTheme.typography.labelSmall,
                            color = ElectricViolet
                        )
                        Text(
                            text = scene.sfxAndMusic,
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // AI Prompt Box (Veo / Sora / Runway)
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = StudioCardElevated,
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Prompt Moteur Vidéo IA (Veo / Sora)",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = AmberGold
                                )
                                IconButton(
                                    onClick = { onCopyScenePrompt(scene.aiVideoPrompt) },
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ContentCopy,
                                        contentDescription = "Copier",
                                        tint = TextSecondary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = scene.aiVideoPrompt,
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Visual Keyframe Status / Action
                    if (scene.sceneBitmap != null) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "✓ Visuel IA généré pour ce plan",
                                style = MaterialTheme.typography.labelSmall,
                                color = AmberGold
                            )
                            OutlinedButton(
                                onClick = { onGenerateVisual(index) },
                                enabled = !scene.isGeneratingBitmap,
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = AmberGold)
                            ) {
                                if (scene.isGeneratingBitmap) {
                                    CircularProgressIndicator(color = AmberGold, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Mise à jour...", fontSize = 11.sp)
                                } else {
                                    Text("Régénérer", fontSize = 11.sp)
                                }
                            }
                        }
                    } else {
                        Button(
                            onClick = { onGenerateVisual(index) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = StudioCardElevated,
                                contentColor = AmberGold
                            ),
                            border = androidx.compose.foundation.BorderStroke(1.dp, AmberGold.copy(alpha = 0.5f)),
                            enabled = !scene.isGeneratingBitmap
                        ) {
                            if (scene.isGeneratingBitmap) {
                                CircularProgressIndicator(color = AmberGold, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Génération visuel IA du plan...", fontSize = 12.sp)
                            } else {
                                Icon(imageVector = Icons.Default.CameraAlt, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Générer l'image IA pour ce plan vidéo", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}
