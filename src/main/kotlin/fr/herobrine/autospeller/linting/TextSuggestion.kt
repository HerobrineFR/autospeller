package fr.herobrine.autospeller.linting

/**
 * Suggestion to modify the text, based on mistakes detected by the linter.
 *
 * @param fromPosition Start index
 * @param toPosition End index
 * @param suggestedReplacements Replacements that the [fr.herobrine.autospeller.service.InputProcessor] found to be compatible with the rulesets and base input.
 */
data class TextSuggestion(
    val fromPosition: Int,
    val toPosition: Int,
    val suggestedReplacements: List<String>
) {
}