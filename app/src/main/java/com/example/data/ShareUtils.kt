package com.example.data

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

/**
 * Shares a generated Photo Ad along with its rendered JPEG image via Android FileProvider
 * (zero storage permissions required, works across WhatsApp, Facebook, Telegram, etc.).
 */
fun sharePhotoAd(context: Context, photoAd: GeneratedPhotoAd) {
    val titleStr = if (photoAd.headline.isNotBlank()) {
        photoAd.headline
    } else {
        "Campagne ${photoAd.category.displayName}"
    }
    val shareText = buildString {
        appendLine("📢 PUBLICITÉ STUDIO : $titleStr")
        if (photoAd.brandName.isNotBlank()) appendLine("Marque : ${photoAd.brandName}")
        if (photoAd.badgeText.isNotBlank()) appendLine("Offre : [${photoAd.badgeText}]")
        appendLine("Format : ${photoAd.ratio.displayName}")
        appendLine()
        appendLine("Description : ${photoAd.prompt}")
        appendLine()
        appendLine("Prompt Studio 8K :")
        appendLine(photoAd.enhancedPrompt)
        appendLine()
        append("✨ Créé avec AdGen Studio")
    }

    try {
        val sharedDir = File(context.cacheDir, "shared").apply { mkdirs() }
        // Clean old temporary share files so cache doesn't grow unbounded
        sharedDir.listFiles()?.forEach { oldFile ->
            if (oldFile.lastModified() < System.currentTimeMillis() - 3600_000L) {
                oldFile.delete()
            }
        }
        val imageFile = File(sharedDir, "ad_${photoAd.id}.jpg")
        FileOutputStream(imageFile).use { out ->
            photoAd.bitmap.compress(Bitmap.CompressFormat.JPEG, 92, out)
        }
        val imageUri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            imageFile
        )

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "image/jpeg"
            putExtra(Intent.EXTRA_STREAM, imageUri)
            putExtra(Intent.EXTRA_SUBJECT, titleStr)
            putExtra(Intent.EXTRA_TEXT, shareText)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(shareIntent, "Partager la publicité via"))
    } catch (e: Exception) {
        try {
            val textIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, titleStr)
                putExtra(Intent.EXTRA_TEXT, shareText)
            }
            context.startActivity(Intent.createChooser(textIntent, "Partager la publicité via"))
        } catch (fallbackErr: Exception) {
            Toast.makeText(
                context,
                "Erreur de partage : ${fallbackErr.localizedMessage ?: "Action indisponible"}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
