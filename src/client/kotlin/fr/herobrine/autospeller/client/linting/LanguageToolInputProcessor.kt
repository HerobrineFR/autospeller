package fr.herobrine.autospeller.client.linting

import fr.herobrine.autospeller.Autospeller.logger
import fr.herobrine.autospeller.language.TokenInputElement
import fr.herobrine.autospeller.linting.LintingResult
import fr.herobrine.autospeller.linting.TextSuggestion
import fr.herobrine.autospeller.service.InputProcessor
import org.languagetool.JLanguageTool
import org.languagetool.language.French
import java.util.concurrent.CompletableFuture

data class LanguageToolInputProcessor(
    private var languageTool: JLanguageTool? = null
): InputProcessor {
    private var ready = false

    init {

        CompletableFuture.runAsync {
            val french = French.getInstance()
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

}
