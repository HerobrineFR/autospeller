package fr.herobrine.autospeller.client.integration
//? fabric {
import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import fr.herobrine.autospeller.client.config.MOD_CONFIG_HANDLER

object AutospellerModMenuKtIntegration: ModMenuApi {

    override fun getModConfigScreenFactory(): ConfigScreenFactory<*> {
        return { parent ->
            MOD_CONFIG_HANDLER.instance().generateScreen(parent)
        }
    }
}
//?}
