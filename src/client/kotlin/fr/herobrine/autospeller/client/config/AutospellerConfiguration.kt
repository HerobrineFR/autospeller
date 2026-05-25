package fr.herobrine.autospeller.client.config

import fr.herobrine.autospeller.Autospeller
import fr.herobrine.autospeller.client.linting.LanguageToolInputProcessor
import fr.herobrine.autospeller.config.LinterConfigurationInterface
import fr.herobrine.autospeller.ignore.IgnoreList
import fr.herobrine.autospeller.language.WordElement
import fr.herobrine.autospeller.language.WordSet
import fr.herobrine.autospeller.service.IgnoreFilter
import fr.herobrine.autospeller.service.InputProcessor
import me.shedaniel.autoconfig.ConfigData
import me.shedaniel.autoconfig.annotation.Config

@Config(name = Autospeller.MOD_ID)
class AutospellerConfiguration: ConfigData, LinterConfigurationInterface {
    override var enableMod = true
    var ignoredWords = emptyList<String>()

    override suspend fun ignoreFilter(): IgnoreFilter = IgnoreFilter(
        IgnoreList(WordSet(
            ignoredWords.map { WordElement(it) }
        ))
    )

    override fun createInputProcessor(): InputProcessor {
        return LanguageToolInputProcessor()
    }
}