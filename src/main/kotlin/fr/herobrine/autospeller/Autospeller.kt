package fr.herobrine.autospeller

import net.fabricmc.api.ModInitializer
import org.apache.logging.log4j.LogManager

object Autospeller : ModInitializer {
    const val MOD_ID = "autospeller"
    val logger = LogManager.getLogger(MOD_ID)


    override fun onInitialize() {
        logger.info("Initializing Autospeller")
    }
}
