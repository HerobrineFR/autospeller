package fr.herobrine.autospeller.config

import fr.herobrine.autospeller.service.IgnoreFilter
import fr.herobrine.autospeller.service.InputProcessor

interface LinterConfigurationInterface {
    var enableMod: Boolean

    suspend fun ignoreFilter(): IgnoreFilter
    fun createInputProcessor(): InputProcessor
}