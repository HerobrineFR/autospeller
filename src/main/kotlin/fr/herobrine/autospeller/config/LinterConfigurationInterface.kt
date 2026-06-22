package fr.herobrine.autospeller.config

import fr.herobrine.autospeller.language.Language
import fr.herobrine.autospeller.language.LanguageLevel
import fr.herobrine.autospeller.service.IgnoreFilter
import fr.herobrine.autospeller.service.InputProcessor
import java.awt.Color
import kotlin.time.Duration

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
	 * Debounce delay of the linter, in milliseconds.
	 */
	var debounceDelay: Int

	/**
	 * The language level used by the linter for checks.
	 */
	var languageLevel: LanguageLevel

    /**
     * Selected language.
     */
    var language: Language

    /**
     * Creates an input processor.
     */
    fun createInputProcessor(): InputProcessor

	fun getIgnoreFilter(): IgnoreFilter?
}
