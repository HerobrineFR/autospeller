package fr.herobrine.autospeller.linting

import fr.herobrine.autospeller.language.TokenInputElement
import java.time.Duration

data class LintingTicket(
    val input: TokenInputElement,
    val debounceDuration: Duration,
)
