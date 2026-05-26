package fr.herobrine.autospeller.ignore

import fr.herobrine.autospeller.language.LanguageElement

/**
 * Rule to check if a [LanguageElement] should be ignored by the [fr.herobrine.autospeller.service.InputProcessor].
 */
interface VocabularyIgnoration<T: LanguageElement> {

    /**
     * Returns whether the [LanguageElement] is ignored or not.
     */
    fun isIgnored(element: T): Boolean
}