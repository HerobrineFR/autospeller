package fr.herobrine.autospeller.linting

/**
 * Result of a linting, contains a set of suggestions.
 */
data class LintingResult(
    val textSuggestions: List<TextSuggestion>
) {
}