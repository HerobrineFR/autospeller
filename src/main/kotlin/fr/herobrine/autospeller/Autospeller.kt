package fr.herobrine.autospeller

import fr.herobrine.autospeller.client.AutospellerClient
import fr.herobrine.autospeller.client.AutospellerClient.create
import fr.herobrine.autospeller.platform.Platform
//? fabric {
import fr.herobrine.autospeller.platform.fabric.FabricPlatform
//?} neoforge {
/*import fr.herobrine.autospeller.platform.neoforge.NeoforgePlatform
*///?}
import org.slf4j.LoggerFactory

object Autospeller {
    const val MOD_ID = /*$ mod_id*/"autospeller";
	const val MOD_VERSION = /*$ mod_version*/"1.4";
	const val MOD_FRIENDLY_NAME =   /*$ mod_name*/"Autospeller";

	private val PLATFORM: Platform = createPlatformInstance()

    val logger = LoggerFactory.getLogger(MOD_ID)

	fun onInitialize() {
		logger.info("Initializing {} on {}", MOD_ID, this.PLATFORM)
	}

	fun onInitializeClient() {
		logger.info("Initializing {} Client on {}", MOD_ID, this.PLATFORM)
		AutospellerClient.create()
	}

	private fun createPlatformInstance(): Platform {
		//? fabric {
		return FabricPlatform()
		//?} neoforge {
		/*return NeoforgePlatform()
		*///?}
	}
}
