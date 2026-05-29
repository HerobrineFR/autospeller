package fr.herobrine.autospeller.config

import fr.herobrine.autospeller.language.Language
import fr.herobrine.autospeller.service.IgnoreFilter
import fr.herobrine.autospeller.service.InputProcessor
import java.awt.Color

interface LinterConfigurationInterface {
    /**
     * Whether the mod is enabled.
     */
    var enableMod: Boolean

	val maxSuggestions: Int

    /**
     * Syntax highlighting color.
     */
    var underlineColor: Color

    /**
     * Selected language.
     */
    var language: Language

    /**
     * Filter of words that should be ignored by the linter.
     */
    fun ignoreFilter(): IgnoreFilter

    /**
     * Creates an input processor.
     */
    fun createInputProcessor(): InputProcessor

	fun getIgnoreFilter(): IgnoreFilter?
}
