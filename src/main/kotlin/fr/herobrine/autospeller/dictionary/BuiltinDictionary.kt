package fr.herobrine.autospeller.dictionary

import com.google.gson.Gson
import dev.isxander.yacl3.platform.YACLPlatform

data class BuiltinDictionary(
    val hash: String = "",
    val words: List<String> = emptyList()
) {
    companion object {
        private val filePath get() = YACLPlatform.getConfigDir().resolve("autospeller_builtin_dictionary.json")
        private val gson = Gson()

        fun load(): BuiltinDictionary {
            val file = filePath.toFile()
            if (!file.exists()) {
                val empty = BuiltinDictionary()
                save(empty)
                return empty
            }
            return try {
                gson.fromJson(file.readText(), BuiltinDictionary::class.java) ?: BuiltinDictionary()
            } catch (e: Exception) {
                BuiltinDictionary()
            }
        }

        fun save(dictionary: BuiltinDictionary) {
            filePath.toFile().writeText(gson.toJson(dictionary))
        }
    }
}
