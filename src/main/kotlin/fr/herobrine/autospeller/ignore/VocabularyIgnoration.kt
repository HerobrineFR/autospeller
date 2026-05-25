package fr.herobrine.autospeller.ignore

import fr.herobrine.autospeller.language.LanguageElement

interface VocabularyIgnoration<T: LanguageElement> {
    fun isIgnored(element: T): Boolean
}