package fr.herobrine.autospeller.linting

import fr.herobrine.autospeller.language.TokenInputElement
import java.time.Duration

data class LintingTicket(
    val input: TokenInputElement,
    val debounceDuration: Duration,
) {
	var prefixLength = 0

	init {
		var foundAlphanumeric = false
		input.input.forEach { character ->
			if(!foundAlphanumeric) {
				when(character.isLetterOrDigit()) {
					true -> foundAlphanumeric = true
					else -> prefixLength++
				}
			}
		}
	}
}
