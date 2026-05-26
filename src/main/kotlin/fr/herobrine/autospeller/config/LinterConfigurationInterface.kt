package fr.herobrine.autospeller.config

import fr.herobrine.autospeller.service.IgnoreFilter
import fr.herobrine.autospeller.service.InputProcessor

interface LinterConfigurationInterface {
    /**
     * Whether the mod is enabled.
     */
    var enableMod: Boolean

    /**
     * Filter of words that should be ignored by the linter.
     */
    suspend fun ignoreFilter(): IgnoreFilter

    /**
     * Creates an input processor.
     */
    fun createInputProcessor(): InputProcessor
}