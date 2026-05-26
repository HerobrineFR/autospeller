package fr.herobrine.autospeller.service

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
    fun process(input: TokenInputElement): LintingResult

    /**
     * Tells if the input processor is ready, or still in a pending state.
     */
    fun isReady(): Boolean
}