package fr.herobrine.autospeller.ignore

import fr.herobrine.autospeller.language.WordElement
import fr.herobrine.autospeller.language.WordSet

/**
 * List of ignored words.
 */
data class IgnoreList(
    val wordSet: WordSet
): VocabularyIgnoration<WordElement> by wordSet