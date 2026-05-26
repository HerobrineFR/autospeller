package fr.herobrine.autospeller.client.linting

import fr.herobrine.autospeller.Autospeller.logger
import fr.herobrine.autospeller.language.TokenInputElement
import fr.herobrine.autospeller.linting.LintingResult
import fr.herobrine.autospeller.linting.TextSuggestion
import fr.herobrine.autospeller.service.InputProcessor
import kotlinx.coroutines.delay
import org.languagetool.JLanguageTool
import org.languagetool.language.French
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit
import kotlin.time.Duration.Companion.minutes

/**
 * Processes inputs through a LanguageTool instance.
 *
 * The processor and it's language sets must be initialized, which is done async but can take quite a while.
 */
data class LanguageToolInputProcessor(
    private var languageTool: JLanguageTool? = null
): InputProcessor {
    private var ready = false

    init {

        CompletableFuture.runAsync {
            val french = French.getInstance()
            TimeUnit.MINUTES.sleep(5)
            try {
                logger.info("[Linter] Creating LT instance with classLoader.")
                val languageTool = JLanguageTool(
                    french,
                )

                languageTool.check("")

                ready = true
                this.languageTool = languageTool

                logger.info("[Linter] LanguageTool was loaded successfully.")
            }catch(e: Exception){
                logger.error("[Linter] Failed to load LanguageTool.", e)
            }
        }
    }

    /**
     * Processes the input.
     * @return the result of the linting process.
     */
    override fun process(input: TokenInputElement): LintingResult {
        val suggestions = arrayListOf<TextSuggestion>()
        val maxReplacements = 4

        if(!this.ready || this.languageTool == null) {
            logger.info("[Linter] LanguageTool is not loaded yet.")
            return LintingResult(emptyList())
        }

        try {
            val check = languageTool?.check(input.input)
            check?.forEach { match ->
                var replacements = match.suggestedReplacements
                if(replacements.size > maxReplacements) {
                    replacements = replacements.slice(0..<maxReplacements)
                }

                suggestions.add(
                    TextSuggestion(
                        match.fromPos,
                        match.toPos,
                        replacements
                    )
                )
            }
        }catch (e: Exception){
            logger.error("Error during linting", e)
        }

        if(suggestions.isEmpty()) {
            logger.info("No suggestions were found")
        }

        return LintingResult(suggestions)
    }

    override fun isReady() = this.ready
}
