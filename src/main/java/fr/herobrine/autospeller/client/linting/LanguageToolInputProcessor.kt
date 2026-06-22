package fr.herobrine.autospeller.client.linting

import fr.herobrine.autospeller.Autospeller.logger
import fr.herobrine.autospeller.language.Language
import fr.herobrine.autospeller.language.LanguageLevel
import fr.herobrine.autospeller.language.TokenInputElement
import fr.herobrine.autospeller.linting.LintingResult
import fr.herobrine.autospeller.linting.TextSuggestion
import fr.herobrine.autospeller.service.IgnoreFilter
import fr.herobrine.autospeller.service.InputProcessor
import org.languagetool.JLanguageTool
import org.languagetool.ResultCache
import org.languagetool.UserConfig
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
	var languageLevel: LanguageLevel,
	private val ignoreFilter: IgnoreFilter?
): InputProcessor {
    private var ready = false

    var language: Language = Language.ENGLISH
        set(value) {
            field = value
            this.loadProcessor()
        }

    override fun loadProcessor() {
        CompletableFuture.runAsync {
            this.ready = false
			this.languageTool = null

            try {
                logger.info("[Linter] Creating LT instance.")
                val languageTool = JLanguageTool(
                    when(this.language) {
                        Language.FRENCH -> French.getInstance()
                        else -> AmericanEnglish.getInstance()
                    },
					ResultCache(512),
					UserConfig(this.ignoreFilter?.ignoreList?.wordSet?.getElements()?.map { el -> el.word.lowercase() } ?: emptyList())
                )

                languageTool.check("The apples are green")

                this.languageTool = languageTool

                logger.info("[Linter] LanguageTool was loaded successfully.")
				this.ready = true
            }catch(e: Exception){
                logger.error("[Linter] Failed to load LanguageTool.", e)
            }
        }
    }

    /**
     * Processes the input.
     * @return the result of the linting process.
     */
    override fun process(input: TokenInputElement, languageLevel: LanguageLevel, maxSuggestions: Int): LintingResult {
        val suggestions = arrayListOf<TextSuggestion>()

        if(this.languageTool == null) {
            logger.info("[Linter] LanguageTool is not loaded yet.")
            return LintingResult(emptyList())
        }

        try {
            val check = languageTool?.check(input.input, languageLevel.level)
            check?.forEach { match ->
                var replacements = match.suggestedReplacements
                if(replacements.size > maxSuggestions) {
                    replacements = replacements.slice(0..<maxSuggestions)
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

    override fun isReady() = this.languageTool != null
	override fun isPending(): Boolean = !this.ready
}
