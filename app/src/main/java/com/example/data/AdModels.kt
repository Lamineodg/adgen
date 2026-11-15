package com.example.data

import android.graphics.Bitmap

enum class AdCategory(val displayName: String, val iconName: String) {
    LUXURY("Luxe & Beauté", "Diamond"),
    STREETWEAR("Mode & Streetwear", "Checkroom"),
    FOOD("Food & Boissons", "Restaurant"),
    TECH("High-Tech & Gadgets", "Devices"),
    WELLNESS("Éco & Nature", "Spa"),
    AUTO("Auto & Sport", "Speed")
}

enum class AdRatio(val displayName: String, val ratioLabel: String, val widthRatio: Float, val heightRatio: Float) {
    SQUARE("Carré (1:1)", "Facebook / WhatsApp", 1f, 1f),
    STORY_REEL("Vertical (9:16)", "Statut WA / TikTok", 9f, 16f),
    LANDSCAPE("Paysage (16:9)", "YouTube / Bannières", 16f, 9f),
    PORTRAIT("Portrait (4:5)", "Posts Sponsorisés", 4f, 5f)
}

enum class AdLighting(val displayName: String) {
    STUDIO_SOFTBOX("Studio Softbox (Éclairage doux pro)"),
    NEON_CYBERPUNK("Néons Cyberpunk & Reflets"),
    GOLDEN_HOUR("Golden Hour (Lumière naturelle dorée)"),
    DARK_MOODY("Dark & Moody (Marbre & Ombres dramatiques)"),
    CLEAN_MINIMAL("Minimaliste Pur & Fond Blanc/Gris clair")
}

enum class AdBadgeStyle(val displayName: String, val bgColorHex: Long, val textColorHex: Long) {
    RUBY_RED("Rubis Flash", 0xFFEF4444, 0xFFFFFFFF),
    ROYAL_GOLD("Or Studio", 0xFFF59E0B, 0xFF000000),
    CYBER_VIOLET("Néon Violet", 0xFF8B5CF6, 0xFFFFFFFF),
    EMERALD_MINT("Menthe Éco", 0xFF10B981, 0xFF000000),
    OLED_DARK("Noir Minimal", 0xFF0F172A, 0xFFF59E0B)
}

enum class AdBadgePosition(val displayName: String) {
    TOP_LEFT("Haut Gauche"),
    TOP_RIGHT("Haut Droite"),
    BOTTOM_LEFT("Bas Gauche")
}

data class VideoScene(
    val sceneNumber: Int,
    val timeCode: String, // e.g. "00:00 - 00:03"
    val durationSeconds: Int,
    val visualDescription: String,
    val cameraMovement: String, // e.g. "Dolly-in rapide vers le produit"
    val aiVideoPrompt: String, // Optimisé pour Veo / Sora / Runway
    val voiceover: String, // Voix off en français
    val sfxAndMusic: String,
    val textOverlay: String, // Titre affiché à l'écran
    val sceneBitmap: Bitmap? = null,
    val isGeneratingBitmap: Boolean = false
)

data class VideoAdCampaign(
    val id: String = java.util.UUID.randomUUID().toString(),
    val title: String,
    val brandName: String,
    val hook: String,
    val targetAudience: String,
    val tone: String,
    val scenes: List<VideoScene>,
    val ctaText: String,
    val totalDurationSeconds: Int = scenes.sumOf { it.durationSeconds }
)

data class GeneratedPhotoAd(
    val id: String = java.util.UUID.randomUUID().toString(),
    val prompt: String,
    val enhancedPrompt: String,
    val category: AdCategory,
    val ratio: AdRatio,
    val bitmap: Bitmap,
    val brandName: String = "",
    val badgeText: String = "", // e.g. "NOUVEAU", "-30%", "ÉDITION LIMITÉE"
    val badgeStyle: AdBadgeStyle = AdBadgeStyle.RUBY_RED,
    val badgePosition: AdBadgePosition = AdBadgePosition.TOP_LEFT,
    val headline: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

data class AdCampaignPreset(
    val id: String,
    val title: String,
    val category: AdCategory,
    val description: String,
    val prompt: String,
    val suggestedRatio: AdRatio,
    val sampleDrawableName: String? = null
)
