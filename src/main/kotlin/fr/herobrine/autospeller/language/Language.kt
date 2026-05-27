package fr.herobrine.autospeller.language

import dev.isxander.yacl3.api.NameableEnum
import fr.herobrine.autospeller.ext.asTranslatable
import net.minecraft.network.chat.Component

enum class Language(
    val translationKey: String
): NameableEnum {
    FRENCH("text.language.autospeller.french"),
    ENGLISH("text.language.autospeller.english");

    override fun getDisplayName(): Component? {
        return this.translationKey.asTranslatable()
    }
}