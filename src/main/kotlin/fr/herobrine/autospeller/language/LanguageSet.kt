package fr.herobrine.autospeller.language

/**
 * Set of language elements.
 */
interface LanguageSet<T: LanguageElement> {
    fun contains(element: T): Boolean
    fun getElements(): List<T>
}