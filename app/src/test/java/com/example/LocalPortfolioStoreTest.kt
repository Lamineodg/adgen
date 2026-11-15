package com.example

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import androidx.test.core.app.ApplicationProvider
import com.example.data.AdBadgePosition
import com.example.data.AdBadgeStyle
import com.example.data.AdCategory
import com.example.data.AdRatio
import com.example.data.GeneratedPhotoAd
import com.example.data.LocalPortfolioStore
import com.example.data.VideoAdCampaign
import com.example.data.VideoScene
import java.io.File
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class LocalPortfolioStoreTest {

    private lateinit var context: Context
    private lateinit var store: LocalPortfolioStore

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        File(context.filesDir, "portfolio").deleteRecursively()
        store = LocalPortfolioStore(context)
    }

    private fun createTestBitmap(color: Int = Color.MAGENTA): Bitmap {
        return Bitmap.createBitmap(64, 64, Bitmap.Config.ARGB_8888).apply {
            eraseColor(color)
        }
    }

    @Test
    fun `save and reload photos persists metadata, bitmap, and cleans deleted files`() = runTest {
        val photo1 = GeneratedPhotoAd(
            id = "photo_1",
            prompt = "Beurre de karité pur du Burkina",
            enhancedPrompt = "Studio 8K beurre de karité",
            category = AdCategory.WELLNESS,
            ratio = AdRatio.SQUARE,
            bitmap = createTestBitmap(Color.YELLOW),
            brandName = "KARITÉ FASO",
            badgeText = "100% PUR",
            badgeStyle = AdBadgeStyle.ROYAL_GOLD,
            badgePosition = AdBadgePosition.TOP_RIGHT,
            headline = "Or Jaune Naturel",
            createdAt = 1700000000000L
        )
        val photo2 = GeneratedPhotoAd(
            id = "photo_2",
            prompt = "Faso Dan Fani haute couture",
            enhancedPrompt = "Studio 8K Faso Dan Fani",
            category = AdCategory.STREETWEAR,
            ratio = AdRatio.STORY_REEL,
            bitmap = createTestBitmap(Color.BLUE)
        )

        store.savePhotos(listOf(photo1, photo2))

        val newStoreInstance = LocalPortfolioStore(context)
        val loaded = newStoreInstance.loadPhotos()

        assertEquals(2, loaded.size)
        assertEquals("photo_1", loaded[0].id)
        assertEquals("KARITÉ FASO", loaded[0].brandName)
        assertEquals("100% PUR", loaded[0].badgeText)
        assertEquals(AdCategory.WELLNESS, loaded[0].category)
        assertEquals(AdBadgeStyle.ROYAL_GOLD, loaded[0].badgeStyle)
        assertEquals(AdBadgePosition.TOP_RIGHT, loaded[0].badgePosition)
        assertEquals(64, loaded[0].bitmap.width)

        // Remove photo_1 and verify orphan image file is cleaned up
        newStoreInstance.savePhotos(listOf(photo2))
        val afterDelete = newStoreInstance.loadPhotos()
        assertEquals(1, afterDelete.size)
        assertEquals("photo_2", afterDelete[0].id)
        val deletedImg = File(context.filesDir, "portfolio/images/photo_photo_1.jpg")
        assertFalse(deletedImg.exists())
    }

    @Test
    fun `save and reload video campaigns persists scenes and generated keyframe bitmaps`() = runTest {
        val scene1 = VideoScene(
            sceneNumber = 1,
            timeCode = "00:00 - 00:03",
            durationSeconds = 3,
            visualDescription = "Gros plan flacon bissap glacé",
            cameraMovement = "Dolly-in",
            aiVideoPrompt = "Cinematic close-up iced hibiscus bottle",
            voiceover = "Découvrez la fraîcheur du Faso.",
            sfxAndMusic = "Afrobeat doux",
            textOverlay = "BISSAP FRAIS",
            sceneBitmap = createTestBitmap(Color.RED)
        )
        val scene2 = VideoScene(
            sceneNumber = 2,
            timeCode = "00:03 - 00:06",
            durationSeconds = 3,
            visualDescription = "Sourire client au soleil",
            cameraMovement = "Tracking shot",
            aiVideoPrompt = "Smiling youth drinking hibiscus juice",
            voiceover = "Naturel et énergisant.",
            sfxAndMusic = "Percussion",
            textOverlay = "100% NATUREL",
            sceneBitmap = null
        )

        val campaign = VideoAdCampaign(
            id = "camp_1",
            title = "Bissap Royal",
            brandName = "FASO DRINK",
            hook = "La fraîcheur authentique !",
            targetAudience = "Jeunes actifs",
            tone = "Dynamique",
            scenes = listOf(scene1, scene2),
            ctaText = "Commandez sur WhatsApp"
        )

        store.saveCampaigns(listOf(campaign))

        val reloaded = LocalPortfolioStore(context).loadCampaigns()
        assertEquals(1, reloaded.size)
        val loadedCamp = reloaded[0]
        assertEquals("Bissap Royal", loadedCamp.title)
        assertEquals(2, loadedCamp.scenes.size)
        assertNotNull(loadedCamp.scenes[0].sceneBitmap)
        assertEquals(64, loadedCamp.scenes[0].sceneBitmap?.width)
        assertTrue(loadedCamp.scenes[1].sceneBitmap == null)

        // Regenerate scene 1 with a GREEN bitmap and verify it overwrites the old RED file
        val regeneratedCamp = campaign.copy(
            scenes = listOf(scene1.copy(sceneBitmap = createTestBitmap(Color.GREEN)), scene2)
        )
        store.saveCampaigns(listOf(regeneratedCamp), updatedCampaignId = regeneratedCamp.id)
        val reloadedAfterRegen = LocalPortfolioStore(context).loadCampaigns()
        val pixel = reloadedAfterRegen[0].scenes[0].sceneBitmap?.getPixel(32, 32)
        assertNotNull(pixel)
        assertTrue("Expected green channel dominant after regeneration", Color.green(pixel!!) > 200 && Color.red(pixel) < 50)

        // Delete campaign and verify scene file cleanup
        store.saveCampaigns(emptyList())
        val sceneFile = File(context.filesDir, "portfolio/images/scene_camp_1_1.jpg")
        assertFalse(sceneFile.exists())
        assertTrue(LocalPortfolioStore(context).loadCampaigns().isEmpty())
    }
}
