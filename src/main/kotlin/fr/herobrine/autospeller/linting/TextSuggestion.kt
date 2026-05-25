package fr.herobrine.autospeller.linting

data class TextSuggestion(
    val fromPosition: Int,
    val toPosition: Int,
    val suggestedReplacements: List<String>
) {
}