package fr.herobrine.autospeller.language

import fr.herobrine.autospeller.ignore.VocabularyIgnoration

class WordSet(
    private val wordSet: List<WordElement>
): LanguageSet<WordElement>, VocabularyIgnoration<WordElement> {
    override fun contains(element: WordElement): Boolean {
        return wordSet.contains(element)
    }

    override fun getElements(): List<WordElement> {
        return wordSet
    }

    override fun isIgnored(element: WordElement): Boolean = wordSet.contains(element)

    companion object {
        fun of(wordSet: List<WordElement>): WordSet {
            return WordSet(wordSet)
        }
    }
}