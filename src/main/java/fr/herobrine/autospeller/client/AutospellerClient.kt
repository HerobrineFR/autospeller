package fr.herobrine.autospeller.client

import fr.herobrine.autospeller.Autospeller
import fr.herobrine.autospeller.client.config.MOD_CONFIG_HANDLER
import fr.herobrine.autospeller.dictionary.BuiltinDictionary
import fr.herobrine.autospeller.dictionary.ConfigMigrator
import fr.herobrine.autospeller.dictionary.RemoteDictionaryUpdater
import fr.herobrine.autospeller.service.LintingService

object AutospellerClient {
    lateinit var service: LintingService

	fun create() {
		Autospeller.logger.info("[Autospeller] Client started")

		ConfigMigrator.migrateBuiltinDictionary()
		MOD_CONFIG_HANDLER.load()
		val config = MOD_CONFIG_HANDLER.instance()

		RemoteDictionaryUpdater.update(config.dictionaryUrl, BuiltinDictionary.load())

		service = LintingService(config)
		service.linterConfiguration.createInputProcessor()
	}
}
