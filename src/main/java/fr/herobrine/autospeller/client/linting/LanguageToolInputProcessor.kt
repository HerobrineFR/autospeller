package fr.herobrine.autospeller.client.linting

import fr.herobrine.autospeller.Autospeller.logger
import fr.herobrine.autospeller.language.Language
import fr.herobrine.autospeller.language.TokenInputElement
import fr.herobrine.autospeller.linting.LintingResult
import fr.herobrine.autospeller.linting.TextSuggestion
import fr.herobrine.autospeller.service.InputProcessor
import org.languagetool.JLanguageTool
import org.languagetool.language.AmericanEnglish
import org.languagetool.language.French
import java.util.concurrent.CompletableFuture

/**
 * Processes inputs through a LanguageTool instance.
 *
 * The processor and it's language sets must be initialized, which is done async but can take quite a while.
 */
data class LanguageToolInputProcessor(
    private var languageTool: JLanguageTool? = null,
): InputProcessor {
    private var ready = false

    var language: Language = Language.ENGLISH
        set(value) {
            field = value
            this.loadLanguage()
        }

    fun loadLanguage() {
        CompletableFuture.runAsync {
            this.ready = false

            try {
                logger.info("[Linter] Creating LT instance.")
                val languageTool = JLanguageTool(
                    when(this.language) {
                        Language.FRENCH -> French.getInstance()
                        else -> AmericanEnglish.getInstance()
                    },
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
