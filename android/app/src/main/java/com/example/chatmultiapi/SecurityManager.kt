package com.example.chatmultiapi

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONObject
import java.io.File
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

/**
 * BLOCK: SecurityManager
 * PURPOSE: Seed phrase generation, AES key derivation, encrypted config handling.
 * SAFE: comment only
 */
object SecurityManager {

    private const val PREFS_NAME = "security_prefs"
    private const val KEY_SEED_PHRASE_SAVED = "seed_phrase_saved"
    private const val KEY_IV = "aes_iv"
    private const val SALT = "ChatMultiAPI_Salt"
    private const val ITERATIONS = 100_000
    private const val KEY_LENGTH = 256 // bits

    private val wordList = listOf(
        "alpha","bravo","charlie","delta","echo","foxtrot","gamma","omega",
        "pixel","quantum","rocket","syntax","vector","neon","binary","cipher",
        "matrix","orbit","signal","turbo","vertex","zenith","nova","fusion"
    )

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun generateSeedPhrase(): String {
        val rnd = SecureRandom()
        val words = mutableListOf<String>()
        repeat(12) {
            val idx = rnd.nextInt(wordList.size)
            words.add(wordList[idx])
        }
        return words.joinToString(" ")
    }

    private fun deriveKeyFromSeed(seedPhrase: String): SecretKeySpec {
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val spec = PBEKeySpec(
            seedPhrase.toCharArray(),
            SALT.toByteArray(),
            ITERATIONS,
            KEY_LENGTH
        )
        val tmp = factory.generateSecret(spec)
        val keyBytes = tmp.encoded
        return SecretKeySpec(keyBytes, "AES")
    }

    private fun getOrCreateIv(context: Context): IvParameterSpec {
        val prefs = getPrefs(context)
        val existing = prefs.getString(KEY_IV, null)
        return if (existing != null) {
            IvParameterSpec(existing.decodeHex())
        } else {
            val rnd = SecureRandom()
            val ivBytes = ByteArray(16)
            rnd.nextBytes(ivBytes)
            prefs.edit().putString(KEY_IV, ivBytes.toHex()).apply()
            IvParameterSpec(ivBytes)
        }
    }

    fun encryptAndSaveConfig(context: Context, seedPhrase: String, configJson: JSONObject) {
        val key = deriveKeyFromSeed(seedPhrase)
        val iv = getOrCreateIv(context)

        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, key, iv)
        val encBytes = cipher.doFinal(configJson.toString().toByteArray())

        val file = File(context.filesDir, "config.enc")
        file.writeBytes(encBytes)

        getPrefs(context).edit()
            .putBoolean(KEY_SEED_PHRASE_SAVED, true)
            .apply()
    }

    fun loadAndDecryptConfig(context: Context, seedPhrase: String): JSONObject? {
        val file = File(context.filesDir, "config.enc")
        if (!file.exists()) return null

        val key = deriveKeyFromSeed(seedPhrase)
        val iv = getOrCreateIv(context)

        val encBytes = file.readBytes()

        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.DECRYPT_MODE, key, iv)
        val plainBytes = cipher.doFinal(encBytes)
        val text = String(plainBytes)

        return JSONObject(text)
    }

    fun buildSampleConfig(): JSONObject {
        val obj = JSONObject()
        obj.put("default_provider", "openai")
        obj.put("providers", JSONObject().apply {
            put("openai", JSONObject().apply {
                put("api_key", "sk-REPLACE_ME")
                put("model", "gpt-4.1-mini")
            })
            put("anthropic", JSONObject().apply {
                put("api_key", "sk-ANTHROPIC")
                put("model", "claude-3-5-sonnet")
            })
        })
        return obj
    }

    fun resetEncryption(context: Context) {
        val file = File(context.filesDir, "config.enc")
        if (file.exists()) file.delete()

        getPrefs(context).edit()
            .remove(KEY_IV)
            .putBoolean(KEY_SEED_PHRASE_SAVED, false)
            .apply()
    }

    /**
     * BLOCK: backupEncryptedConfig()
     * PURPOSE: Copy config.enc → config.bak (raw encrypted backup).
     * SAFE: comment only
     */
    fun backupEncryptedConfig(context: Context) {
        val src = File(context.filesDir, "config.enc")
        if (!src.exists()) return

        val dst = File(context.filesDir, "config.bak")
        dst.writeBytes(src.readBytes())
    }

    /**
     * BLOCK: restoreEncryptedConfig()
     * PURPOSE: Copy config.bak → config.enc and decrypt using seedPhrase.
     * SAFE: comment only
     */
    fun restoreEncryptedConfig(context: Context, seedPhrase: String): JSONObject? {
        val bak = File(context.filesDir, "config.bak")
        if (!bak.exists()) return null

        val dst = File(context.filesDir, "config.enc")
        dst.writeBytes(bak.readBytes())

        return loadAndDecryptConfig(context, seedPhrase)
    }

    private fun ByteArray.toHex(): String {
        return joinToString("") { "%02x".format(it) }
    }

    private fun String.decodeHex(): ByteArray {
        val len = length
        val data = ByteArray(len / 2)
        var i = 0
        while (i < len) {
            data[i / 2] = (
                (Character.digit(this[i], 16) shl 4) +
                Character.digit(this[i + 1], 16)
            ).toByte()
            i += 2
        }
        return data
    }
}

/* ========================================================================
   METADATA FOOTER — SecurityManager.kt
   version: 1.0.0
   local_timestamp: 07/06/2026 10:56 AM EDT
   utc_timestamp: 2026-07-06T14:56:00Z

   ML TAGS
   - ml_tags: ["crypto_engine", "seed_phrase", "aes_encryption"]

   BLUEPRINT SECTION
   - section: "6.1 — SecurityManager.kt"

   SECTION PURPOSE
   - Implements seed phrase generation, AES key derivation, IV management.
   - Encrypts/decrypts config.enc using AES‑CBC.
   - Provides backup/restore helpers for encrypted config.

   DEPENDENCIES
   - uses: [
       "SecurityActivity.kt",
       "config.enc",
       "config.bak",
       "app_settings SharedPreferences"
     ]

   NOTES
   - Fully regenerated to restore conformity.
   - Non-executable metadata footer.
   ========================================================================
   END OF FILE :: CHATMULTIAPI
*/
