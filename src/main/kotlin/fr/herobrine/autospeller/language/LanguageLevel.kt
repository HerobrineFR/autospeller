package fr.herobrine.autospeller.language

import dev.isxander.yacl3.api.NameableEnum
import fr.herobrine.autospeller.ext.asTranslatable
import net.minecraft.network.chat.Component
import org.languagetool.JLanguageTool

enum class LanguageLevel(
	val translationKey: String,
	val level: JLanguageTool.Level,
): NameableEnum {
	DEFAULT("text.level.autospeller.default", JLanguageTool.Level.DEFAULT),
	PICKY("text.level.autospeller.picky", JLanguageTool.Level.PICKY),
	CREATIVE("text.level.autospeller.creative", JLanguageTool.Level.CREATIVE),
	ELEGANT("text.level.autospeller.elegant", JLanguageTool.Level.ELEGANT);

	override fun getDisplayName(): Component? {
		return this.translationKey.asTranslatable()
	}
}
