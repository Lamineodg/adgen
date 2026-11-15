package com.example

import com.example.data.AdAiRepository
import com.example.data.AdCategory
import com.example.data.AdLighting
import com.example.data.AdRatio
import com.example.data.CampaignPresets
import com.example.data.SecureKeyStore
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleRobolectricTest {

    @Test
    fun `test buildEnhancedAdPrompt produces professional studio keywords`() {
        val repo = AdAiRepository()
        val prompt = repo.buildEnhancedAdPrompt(
            rawDescription = "Faso Dan Fani or et indigo",
            category = AdCategory.STREETWEAR,
            lighting = AdLighting.STUDIO_SOFTBOX,
            ratio = AdRatio.SQUARE
        )

        assertTrue(prompt.contains("Faso Dan Fani or et indigo"))
        assertTrue(prompt.contains("Hasselblad"))
        assertTrue(prompt.contains("8k"))
        assertTrue(prompt.contains("Commercial"))
    }

    @Test
    fun `test category count and ratios`() {
        assertEquals(6, AdCategory.entries.size)
        assertEquals(4, AdRatio.entries.size)
        assertTrue(CampaignPresets.presets.any { it.id == "faso_dan_fani" })
        assertTrue(CampaignPresets.presets.any { it.id == "karite_burkina" })
    }

    @Test
    fun `test Gemini API key format validation`() {
        val repo = AdAiRepository()
        assertFalse(repo.isValidApiKeyFormat(""))
        assertFalse(repo.isValidApiKeyFormat("short_key"))
        assertFalse(repo.isValidApiKeyFormat("AIzaSy with spaces inside 123456"))
        assertTrue(repo.isValidApiKeyFormat("AIzaSyDummyValidKeyFormat1234567890"))
    }

    @Test
    fun `test SecureKeyStore encrypt decrypt graceful fallback on JVM`() {
        val sample = "AIzaSyDummyValidKeyFormat1234567890"
        val encrypted = SecureKeyStore.encrypt(sample)
        assertEquals(sample, SecureKeyStore.decrypt(encrypted))
    }
}
