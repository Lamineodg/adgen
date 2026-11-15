package com.example.data

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import android.util.Log
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

private const val TAG = "SecureKeyStore"
private const val ANDROID_KEYSTORE = "AndroidKeyStore"
private const val KEY_ALIAS = "adgen_studio_api_key_aes"
private const val TRANSFORMATION = "AES/GCM/NoPadding"
private const val GCM_TAG_LENGTH = 128

/**
 * Hardware-backed Android Keystore AES-256-GCM encryption helper.
 * Protects user's personal Gemini API key at rest without external dependencies.
 * Works on all Android devices including Huawei P60 Pro (EMUI/HarmonyOS without GMS).
 */
object SecureKeyStore {

    private fun getOrCreateSecretKey(): SecretKey? {
        return try {
            val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE)
            keyStore.load(null)
            val existingKey = keyStore.getKey(KEY_ALIAS, null) as? SecretKey
            if (existingKey != null) return existingKey

            val keyGenerator = KeyGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_AES,
                ANDROID_KEYSTORE
            )
            val spec = KeyGenParameterSpec.Builder(
                KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setKeySize(256)
                .build()
            keyGenerator.init(spec)
            keyGenerator.generateKey()
        } catch (e: Throwable) {
            safeLog("AndroidKeyStore indisponible dans cet environnement: ${e.message}")
            null
        }
    }

    private fun safeLog(message: String, error: Throwable? = null) {
        try {
            if (error != null) Log.e(TAG, message, error) else Log.w(TAG, message)
        } catch (_: Throwable) {
            // Ignore unmocked android.util.Log in plain JVM unit tests
        }
    }

    fun encrypt(plainText: String): String {
        if (plainText.isBlank()) return ""
        val key = getOrCreateSecretKey() ?: return plainText
        return try {
            val cipher = Cipher.getInstance(TRANSFORMATION)
            cipher.init(Cipher.ENCRYPT_MODE, key)
            val iv = cipher.iv
            val encrypted = cipher.doFinal(plainText.toByteArray(Charsets.UTF_8))
            val ivB64 = Base64.encodeToString(iv, Base64.NO_WRAP)
            val encB64 = Base64.encodeToString(encrypted, Base64.NO_WRAP)
            "v1:$ivB64:$encB64"
        } catch (e: Throwable) {
            safeLog("Erreur de chiffrement", e)
            plainText
        }
    }

    fun decrypt(storedValue: String): String {
        if (storedValue.isBlank()) return ""
        if (!storedValue.startsWith("v1:")) {
            // Backward compatibility with legacy unencrypted value
            return storedValue
        }
        val key = getOrCreateSecretKey() ?: return ""
        return try {
            val parts = storedValue.split(":")
            if (parts.size != 3) return ""
            val iv = Base64.decode(parts[1], Base64.NO_WRAP)
            val cipherText = Base64.decode(parts[2], Base64.NO_WRAP)
            val cipher = Cipher.getInstance(TRANSFORMATION)
            val spec = GCMParameterSpec(GCM_TAG_LENGTH, iv)
            cipher.init(Cipher.DECRYPT_MODE, key, spec)
            String(cipher.doFinal(cipherText), Charsets.UTF_8)
        } catch (e: Throwable) {
            safeLog("Erreur de déchiffrement", e)
            ""
        }
    }
}
