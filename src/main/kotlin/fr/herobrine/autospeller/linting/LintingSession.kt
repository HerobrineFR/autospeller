package fr.herobrine.autospeller.linting

import fr.herobrine.autospeller.language.TokenInputElement
import kotlin.time.Instant

interface LintingSession {
    var lastCheck: Instant?
    var lastInput: TokenInputElement?

    fun shouldLint(ticket: LintingTicket): Boolean
}