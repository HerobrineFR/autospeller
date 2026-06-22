package fr.herobrine.autospeller.dictionary

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import dev.isxander.yacl3.platform.YACLPlatform
import fr.herobrine.autospeller.Autospeller

object ConfigMigrator {
    private val configPath get() = YACLPlatform.getConfigDir().resolve("autospeller.json5")
    private val gson = GsonBuilder().setPrettyPrinting().create()

    fun migrateBuiltinDictionary() {
        val file = configPath.toFile()
        if (!file.exists()) return

        val json = try {
            gson.fromJson(file.readText(), JsonObject::class.java) ?: return
        } catch (e: Exception) {
            Autospeller.logger.warn("[Autospeller] Could not parse config for migration: ${e.message}")
            return
        }

        if (!json.has("builtin_dictionary")) return

        val words = json.getAsJsonArray("builtin_dictionary").map { it.asString }
        if (words.isNotEmpty()) {
            val existing = BuiltinDictionary.load()
            if (existing.words.isEmpty()) {
                BuiltinDictionary.save(BuiltinDictionary(words = words))
                Autospeller.logger.info("[Autospeller] Migrated ${words.size} words to builtin dictionary file.")
            }
        }

        json.remove("builtin_dictionary")
        file.writeText(gson.toJson(json))
        Autospeller.logger.info("[Autospeller] Removed builtin_dictionary key from config.")
    }
}
