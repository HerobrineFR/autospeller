package fr.herobrine.autospeller.service

import fr.herobrine.autospeller.Autospeller
import fr.herobrine.autospeller.config.LinterConfigurationInterface
import fr.herobrine.autospeller.linting.LintingResult
import fr.herobrine.autospeller.linting.LintingSession
import fr.herobrine.autospeller.linting.LintingTicket
import java.time.Duration
import java.util.concurrent.CompletableFuture
import kotlin.time.Clock
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.toJavaDuration

data class LintingService(
    var linterConfiguration: LinterConfigurationInterface,
    var debounce: Duration = 200.milliseconds.toJavaDuration(),
) {
    val inputProcessor = linterConfiguration.createInputProcessor()

    init {
        Autospeller.logger.info("[Autospeller] Linting service created")
    }

    fun processTicket(
        ticket: LintingTicket,
        session: LintingSession
    ): CompletableFuture<LintingResult?> {
        return CompletableFuture.supplyAsync({
            when (session.shouldLint(ticket)) {
                false -> {
                    null
                }
                true -> {
                    val result = inputProcessor.process(ticket.input)
                    session.lastCheck = Clock.System.now()
                    session.lastInput = ticket.input

                    return@supplyAsync result
                }
            }
        })
    }
}
