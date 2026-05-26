package fr.herobrine.autospeller.language

/**
 * Set of language elements.
 */
interface LanguageSet<T: LanguageElement> {
    /**
     * Returns it an element is present in the collection.
     * @param element Object to check the presence of.
     */
    fun contains(element: T): Boolean

    /**
     * Returns the list of [LanguageElement]
     */
    fun getElements(): List<T>
}