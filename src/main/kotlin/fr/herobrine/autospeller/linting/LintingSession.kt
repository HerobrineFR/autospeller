package fr.herobrine.autospeller.linting

import fr.herobrine.autospeller.language.TokenInputElement
import kotlin.time.Instant

/**
 * Session, of which the state allows to define the linting process information.
 */
interface LintingSession {
    /**
     * The last time the linter ran a check on an input.
     */
    var lastCheck: Instant?

    /**
     * The last input that was checked.
     */
    var lastInput: TokenInputElement?

    /**
     * Whether a ticket should be processed or not.
     */
    fun shouldLint(ticket: LintingTicket): Boolean
}