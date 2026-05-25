package fr.herobrine.autospeller.client.integration

import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import fr.herobrine.autospeller.client.config.AutospellerConfiguration
import me.shedaniel.autoconfig.AutoConfigClient

object AutospellerModMenuIntegration: ModMenuApi {

    override fun getModConfigScreenFactory(): ConfigScreenFactory<*> {
        return { parent ->
            AutoConfigClient.getConfigScreen(
                AutospellerConfiguration::class.java,
                parent
            ).get()
        }
    }
}