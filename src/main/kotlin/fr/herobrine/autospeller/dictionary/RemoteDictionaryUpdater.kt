package fr.herobrine.autospeller.dictionary

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import fr.herobrine.autospeller.Autospeller
import java.net.URI
import java.security.MessageDigest

object RemoteDictionaryUpdater {
    private val gson = Gson()

    fun update(url: String, current: BuiltinDictionary): BuiltinDictionary {
        if (url.isBlank()) return current

        return try {
            val connection = URI.create(url).toURL().openConnection()
            connection.useCaches = false
            connection.setRequestProperty("Cache-Control", "no-cache, no-store")
            val content = connection.inputStream.bufferedReader().readText()
            val hash = sha256(content)

            if (hash == current.hash) {
                Autospeller.logger.info("[Autospeller] Builtin dictionary is up to date.")
                return current
            }

            val words: List<String> = gson.fromJson(content, object : TypeToken<List<String>>() {}.type)
            val updated = BuiltinDictionary(hash = hash, words = words)
            BuiltinDictionary.save(updated)
            Autospeller.logger.info("[Autospeller] Builtin dictionary updated with ${words.size} words.")
            updated
        } catch (e: Exception) {
            Autospeller.logger.warn("[Autospeller] Failed to fetch builtin dictionary from '$url': ${e.message}")
            current
        }
    }

    private fun sha256(input: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(input.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}
