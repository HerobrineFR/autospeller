package fr.herobrine.autospeller.service

import fr.herobrine.autospeller.language.LanguageLevel
import fr.herobrine.autospeller.language.TokenInputElement
import fr.herobrine.autospeller.linting.LintingResult

/**
 * Interface used to process inputs based on a backend implementation.
 */
interface InputProcessor {

    /**
     * Processes an input language element, and returns a compatible result.
     *
     * **Warning** : The processing can get extremely heavy and, as such, this method **should not** be executed on the main thread.
     *
     * @return A result which can include a list of suggestions.
     */
    fun process(input: TokenInputElement, languageLevel: LanguageLevel = LanguageLevel.DEFAULT, maxSuggestions: Int = 4): LintingResult

    /**
     * Tells if the input processor is ready.
     */
    fun isReady(): Boolean

	/**
	 * Tells if the processor is in a pending state.
	 */
	fun isPending(): Boolean
}
