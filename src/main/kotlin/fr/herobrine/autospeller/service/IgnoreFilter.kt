package fr.herobrine.autospeller.service

import fr.herobrine.autospeller.ignore.IgnoreList
import fr.herobrine.autospeller.ignore.VocabularyIgnoration
import fr.herobrine.autospeller.language.WordElement

data class IgnoreFilter(
    val ignoreList: IgnoreList,
): VocabularyIgnoration<WordElement> by ignoreList {
}