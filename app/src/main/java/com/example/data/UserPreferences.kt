package com.example.data

import android.content.Context
import android.content.SharedPreferences
import java.util.Calendar

class UserPreferences(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("adgen_user_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_IS_PRO = "key_is_pro"
        private const val KEY_CREDITS = "key_credits"
        private const val KEY_LAST_RESET_DAY = "key_last_reset_day"
        private const val KEY_CUSTOM_API_KEY = "key_custom_api_key"
        private const val KEY_SHOW_WATERMARK = "key_show_watermark"
        private const val KEY_EXPORT_RESOLUTION = "key_export_resolution"
        const val MAX_DAILY_CREDITS = 5
    }

    var isPro: Boolean
        get() = prefs.getBoolean(KEY_IS_PRO, false)
        set(value) = prefs.edit().putBoolean(KEY_IS_PRO, value).apply()

    var customApiKey: String
        get() {
            val stored = prefs.getString(KEY_CUSTOM_API_KEY, "") ?: ""
            return SecureKeyStore.decrypt(stored)
        }
        set(value) {
            val trimmed = value.trim()
            val encrypted = if (trimmed.isEmpty()) "" else SecureKeyStore.encrypt(trimmed)
            prefs.edit().putString(KEY_CUSTOM_API_KEY, encrypted).apply()
        }

    var showWatermark: Boolean
        get() = prefs.getBoolean(KEY_SHOW_WATERMARK, !isPro)
        set(value) = prefs.edit().putBoolean(KEY_SHOW_WATERMARK, value).apply()

    var exportResolution: String
        get() = prefs.getString(KEY_EXPORT_RESOLUTION, "1080p Standard") ?: "1080p Standard"
        set(value) = prefs.edit().putString(KEY_EXPORT_RESOLUTION, value).apply()

    /**
     * Retrieves remaining credits for today, resetting to MAX_DAILY_CREDITS if a new day has arrived
     */
    fun getDailyCredits(): Int {
        if (isPro || customApiKey.isNotBlank()) {
            return 999 // Unlimited
        }

        val cal = Calendar.getInstance()
        val today = cal.get(Calendar.YEAR) * 1000 + cal.get(Calendar.DAY_OF_YEAR)
        val lastReset = prefs.getInt(KEY_LAST_RESET_DAY, -1)

        if (today != lastReset) {
            // New day: recharge credits
            prefs.edit()
                .putInt(KEY_LAST_RESET_DAY, today)
                .putInt(KEY_CREDITS, MAX_DAILY_CREDITS)
                .apply()
            return MAX_DAILY_CREDITS
        }

        return prefs.getInt(KEY_CREDITS, MAX_DAILY_CREDITS)
    }

    /**
     * Consumes one credit. Returns true if allowed, false if no credits remaining
     */
    fun consumeCredit(): Boolean {
        if (isPro || customApiKey.isNotBlank()) {
            return true
        }

        val current = getDailyCredits()
        if (current > 0) {
            prefs.edit().putInt(KEY_CREDITS, current - 1).apply()
            return true
        }
        return false
    }

    /**
     * Refunds one credit when an unexpected generation failure occurs on free tier
     */
    fun refundCredit() {
        if (isPro || customApiKey.isNotBlank()) return
        val current = getDailyCredits()
        prefs.edit().putInt(KEY_CREDITS, (current + 1).coerceAtMost(MAX_DAILY_CREDITS)).apply()
    }

    /**
     * Grants bonus credits (e.g., reward or promotion)
     */
    fun addBonusCredits(amount: Int) {
        val current = getDailyCredits()
        prefs.edit().putInt(KEY_CREDITS, current + amount).apply()
    }
}
