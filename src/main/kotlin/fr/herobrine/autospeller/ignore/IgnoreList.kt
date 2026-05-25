package fr.herobrine.autospeller.ignore

import fr.herobrine.autospeller.language.WordElement
import fr.herobrine.autospeller.language.WordSet

data class IgnoreList(
    val wordSet: WordSet
): VocabularyIgnoration<WordElement> by wordSet