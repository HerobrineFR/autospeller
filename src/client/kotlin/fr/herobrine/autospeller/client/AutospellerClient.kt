package fr.herobrine.autospeller.client

import fr.herobrine.autospeller.Autospeller
import fr.herobrine.autospeller.client.config.AutospellerConfiguration
import fr.herobrine.autospeller.service.LintingService
import me.shedaniel.autoconfig.AutoConfig
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents

@Environment(EnvType.CLIENT)
object AutospellerClient : ClientModInitializer {
    lateinit var service: LintingService

    override fun onInitializeClient() {
        Autospeller.logger.info("[Autospeller] Warming client")
        ClientLifecycleEvents.CLIENT_STARTED.register { _ ->
            Autospeller.logger.info("[Autospeller] Client started")
            AutoConfig.register(
                AutospellerConfiguration::class.java,
                ::GsonConfigSerializer
            )

            val modConfiguration = AutoConfig.getConfigHolder(AutospellerConfiguration::class.java)
            service = LintingService(
                modConfiguration.get()
            )
        }
    }
}
