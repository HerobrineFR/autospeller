package fr.herobrine.autospeller.client

import fr.herobrine.autospeller.Autospeller
import fr.herobrine.autospeller.client.config.MOD_CONFIG_HANDLER
import fr.herobrine.autospeller.service.LintingService

object AutospellerClient {
    lateinit var service: LintingService

	fun create() {
		Autospeller.logger.info("[Autospeller] Client started")

		MOD_CONFIG_HANDLER.load()
		service = LintingService(
			MOD_CONFIG_HANDLER.instance()
		)
	}
}
