package fr.herobrine.autospeller.client.linting

import fr.herobrine.autospeller.language.TokenInputElement
import fr.herobrine.autospeller.linting.LintingSession
import fr.herobrine.autospeller.linting.LintingTicket
import net.minecraft.client.gui.components.EditBox
import java.time.Duration
import kotlin.time.Clock
import kotlin.time.Instant
import kotlin.time.toKotlinDuration

/**
 * Current chat window. Holds the linting session's state.
 */
data class ChatLintingSession(
    override var lastCheck: Instant? = null,
    override var lastInput: TokenInputElement?,
    val editBox: EditBox
): LintingSession {
    override fun shouldLint(ticket: LintingTicket): Boolean = ticket.input.input.length > 1 && !shouldDebounce(ticket.debounceDuration) && ticket.input.input != lastInput?.input

    /**
     * Verifies if the linter should debounce.
     */
    private fun shouldDebounce(
        debounceTime: Duration
    ): Boolean {
        when(lastCheck) {
            null -> return false
            else -> with(lastCheck) {
                val now = Clock.System.now()
                val afterDebounce = this!!.plus(debounceTime.toKotlinDuration())

                return now < afterDebounce
            }
        }
    }
}
