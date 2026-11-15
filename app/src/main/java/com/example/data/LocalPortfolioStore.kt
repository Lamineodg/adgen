package com.example.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

private const val TAG = "LocalPortfolioStore"

/**
 * Persistent on-device storage for generated Photo Ads and Video Ad Campaigns.
 * Stores metadata as JSON and rendered bitmaps as JPEG files inside app-private filesDir,
 * requiring zero external permissions, zero cloud database, and 100% Huawei P60 Pro compatibility.
 */
class LocalPortfolioStore(private val context: Context) {

    private val mutex = Mutex()

    private val portfolioDir: File by lazy {
        File(context.filesDir, "portfolio").apply { mkdirs() }
    }
    private val imagesDir: File by lazy {
        File(portfolioDir, "images").apply { mkdirs() }
    }
    private val photosIndexFile: File by lazy {
        File(portfolioDir, "photos_index.json")
    }
    private val campaignsIndexFile: File by lazy {
        File(portfolioDir, "campaigns_index.json")
    }

    suspend fun loadPhotos(): List<GeneratedPhotoAd> = withContext(Dispatchers.IO) {
        mutex.withLock {
            if (!photosIndexFile.exists()) return@withLock emptyList()
            try {
                val arr = JSONArray(photosIndexFile.readText())
                val result = mutableListOf<GeneratedPhotoAd>()
                for (i in 0 until arr.length()) {
                    val obj = arr.optJSONObject(i) ?: continue
                    val id = obj.optString("id")
                    if (id.isBlank()) continue
                    val imgFile = File(imagesDir, "photo_$id.jpg")
                    if (!imgFile.exists()) continue
                    val bitmap = BitmapFactory.decodeFile(imgFile.absolutePath) ?: continue

                    result.add(
                        GeneratedPhotoAd(
                            id = id,
                            prompt = obj.optString("prompt"),
                            enhancedPrompt = obj.optString("enhancedPrompt"),
                            category = enumValueOrDefault(obj.optString("category"), AdCategory.LUXURY),
                            ratio = enumValueOrDefault(obj.optString("ratio"), AdRatio.SQUARE),
                            bitmap = bitmap,
                            brandName = obj.optString("brandName"),
                            badgeText = obj.optString("badgeText"),
                            badgeStyle = enumValueOrDefault(obj.optString("badgeStyle"), AdBadgeStyle.RUBY_RED),
                            badgePosition = enumValueOrDefault(obj.optString("badgePosition"), AdBadgePosition.TOP_LEFT),
                            headline = obj.optString("headline"),
                            createdAt = obj.optLong("createdAt", System.currentTimeMillis())
                        )
                    )
                }
                result
            } catch (e: Exception) {
                safeLog("Erreur lecture photos locales", e)
                emptyList()
            }
        }
    }

    suspend fun savePhotos(photos: List<GeneratedPhotoAd>) = withContext(Dispatchers.IO) {
        mutex.withLock {
            try {
                val validIds = photos.map { "photo_${it.id}.jpg" }.toSet()
                imagesDir.listFiles()?.forEach { file ->
                    if (file.name.startsWith("photo_") && file.name !in validIds) {
                        file.delete()
                    }
                }

                val arr = JSONArray()
                photos.forEach { photo ->
                    val imgFile = File(imagesDir, "photo_${photo.id}.jpg")
                    if (!imgFile.exists()) {
                        saveBitmapToFile(photo.bitmap, imgFile)
                    }
                    arr.put(JSONObject().apply {
                        put("id", photo.id)
                        put("prompt", photo.prompt)
                        put("enhancedPrompt", photo.enhancedPrompt)
                        put("category", photo.category.name)
                        put("ratio", photo.ratio.name)
                        put("brandName", photo.brandName)
                        put("badgeText", photo.badgeText)
                        put("badgeStyle", photo.badgeStyle.name)
                        put("badgePosition", photo.badgePosition.name)
                        put("headline", photo.headline)
                        put("createdAt", photo.createdAt)
                    })
                }
                photosIndexFile.writeText(arr.toString())
            } catch (e: Exception) {
                safeLog("Erreur sauvegarde photos locales", e)
            }
        }
    }

    suspend fun loadCampaigns(): List<VideoAdCampaign> = withContext(Dispatchers.IO) {
        mutex.withLock {
            if (!campaignsIndexFile.exists()) return@withLock emptyList()
            try {
                val arr = JSONArray(campaignsIndexFile.readText())
                val result = mutableListOf<VideoAdCampaign>()
                for (i in 0 until arr.length()) {
                    val obj = arr.optJSONObject(i) ?: continue
                    val campId = obj.optString("id")
                    if (campId.isBlank()) continue
                    val scenesArr = obj.optJSONArray("scenes") ?: JSONArray()
                    val scenes = mutableListOf<VideoScene>()

                    for (sIdx in 0 until scenesArr.length()) {
                        val sObj = scenesArr.optJSONObject(sIdx) ?: continue
                        val sceneNum = sObj.optInt("sceneNumber", sIdx + 1)
                        val sceneFile = File(imagesDir, "scene_${campId}_$sceneNum.jpg")
                        val bitmap = if (sceneFile.exists()) BitmapFactory.decodeFile(sceneFile.absolutePath) else null

                        scenes.add(
                            VideoScene(
                                sceneNumber = sceneNum,
                                timeCode = sObj.optString("timeCode"),
                                durationSeconds = sObj.optInt("durationSeconds", 3),
                                visualDescription = sObj.optString("visualDescription"),
                                cameraMovement = sObj.optString("cameraMovement"),
                                aiVideoPrompt = sObj.optString("aiVideoPrompt"),
                                voiceover = sObj.optString("voiceover"),
                                sfxAndMusic = sObj.optString("sfxAndMusic"),
                                textOverlay = sObj.optString("textOverlay"),
                                sceneBitmap = bitmap,
                                isGeneratingBitmap = false
                            )
                        )
                    }

                    result.add(
                        VideoAdCampaign(
                            id = campId,
                            title = obj.optString("title"),
                            brandName = obj.optString("brandName"),
                            hook = obj.optString("hook"),
                            targetAudience = obj.optString("targetAudience"),
                            tone = obj.optString("tone"),
                            scenes = scenes,
                            ctaText = obj.optString("ctaText"),
                            totalDurationSeconds = obj.optInt("totalDurationSeconds", scenes.sumOf { it.durationSeconds })
                        )
                    )
                }
                result
            } catch (e: Exception) {
                safeLog("Erreur lecture campagnes locales", e)
                emptyList()
            }
        }
    }

    suspend fun saveCampaigns(
        campaigns: List<VideoAdCampaign>,
        updatedCampaignId: String? = null
    ) = withContext(Dispatchers.IO) {
        mutex.withLock {
            try {
                val expectedSceneFiles = mutableSetOf<String>()
                val arr = JSONArray()

                campaigns.forEach { camp ->
                    val forceOverwrite = camp.id == updatedCampaignId
                    val scenesArr = JSONArray()
                    camp.scenes.forEach { scene ->
                        val fileName = "scene_${camp.id}_${scene.sceneNumber}.jpg"
                        val sceneFile = File(imagesDir, fileName)
                        if (scene.sceneBitmap != null) {
                            expectedSceneFiles.add(fileName)
                            if (forceOverwrite || !sceneFile.exists()) {
                                saveBitmapToFile(scene.sceneBitmap, sceneFile)
                            }
                        }
                        scenesArr.put(JSONObject().apply {
                            put("sceneNumber", scene.sceneNumber)
                            put("timeCode", scene.timeCode)
                            put("durationSeconds", scene.durationSeconds)
                            put("visualDescription", scene.visualDescription)
                            put("cameraMovement", scene.cameraMovement)
                            put("aiVideoPrompt", scene.aiVideoPrompt)
                            put("voiceover", scene.voiceover)
                            put("sfxAndMusic", scene.sfxAndMusic)
                            put("textOverlay", scene.textOverlay)
                        })
                    }

                    arr.put(JSONObject().apply {
                        put("id", camp.id)
                        put("title", camp.title)
                        put("brandName", camp.brandName)
                        put("hook", camp.hook)
                        put("targetAudience", camp.targetAudience)
                        put("tone", camp.tone)
                        put("ctaText", camp.ctaText)
                        put("totalDurationSeconds", camp.totalDurationSeconds)
                        put("scenes", scenesArr)
                    })
                }

                // Clean up deleted campaign scene images
                imagesDir.listFiles()?.forEach { file ->
                    if (file.name.startsWith("scene_") && file.name !in expectedSceneFiles) {
                        file.delete()
                    }
                }

                campaignsIndexFile.writeText(arr.toString())
            } catch (e: Exception) {
                safeLog("Erreur sauvegarde campagnes locales", e)
            }
        }
    }

    private fun saveBitmapToFile(bitmap: Bitmap, dest: File) {
        FileOutputStream(dest).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
        }
    }

    private fun safeLog(message: String, error: Throwable) {
        try {
            Log.e(TAG, message, error)
        } catch (_: Throwable) {
            // Ignore unmocked android.util.Log in plain JVM tests
        }
    }

    private inline fun <reified T : Enum<T>> enumValueOrDefault(name: String, default: T): T {
        return try {
            enumValueOf<T>(name)
        } catch (_: Exception) {
            default
        }
    }
}
