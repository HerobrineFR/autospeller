package fr.herobrine.autospeller.service

import fr.herobrine.autospeller.Autospeller
import fr.herobrine.autospeller.client.config.MOD_CONFIG_HANDLER
import fr.herobrine.autospeller.config.LinterConfigurationInterface
import fr.herobrine.autospeller.language.WordElement
import fr.herobrine.autospeller.linting.LintingResult
import fr.herobrine.autospeller.linting.LintingSession
import fr.herobrine.autospeller.linting.LintingTicket
import java.time.Duration
import java.util.concurrent.CompletableFuture
import kotlin.time.Clock
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.toJavaDuration

/**
 * Base service of the mod. Contains the mod's config and debounce rate, and is
 * used to submit tickets to the input processor.
 */
data class LintingService(
    var linterConfiguration: LinterConfigurationInterface,
    var debounce: Duration = linterConfiguration.debounceDelay.milliseconds.toJavaDuration(),
) {
	val dynamicDictionary = arrayListOf<WordElement>()

    init {
        Autospeller.logger.info("[Autospeller] Linting service created")
    }

	fun addWord(str: String) {
		this.dynamicDictionary.add(WordElement(str))
		this.linterConfiguration.appendDynamicDictionary(this.dynamicDictionary)

		MOD_CONFIG_HANDLER.save()
	}

    fun getInputProcessor() = linterConfiguration.createInputProcessor()

    /**
     * Submits a [LintingTicket] to the [InputProcessor] in order to get suggestions based on the processor's backend rules.
     *
     * @param ticket Payload that should be checked by the processor.
     * @param session Session that handles the user's interactions with the service.
     */
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
                    val result = this.getInputProcessor().process(input = ticket.input, languageLevel = this.linterConfiguration.languageLevel, maxSuggestions = this.linterConfiguration.maxSuggestions, dynamicDictionary = this.dynamicDictionary)
                    session.lastCheck = Clock.System.now()
                    session.lastInput = ticket.input

                    return@supplyAsync result
                }
            }
        })
    }
}
