package fr.herobrine.autospeller.client.integration;

//? fabric {
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class AutospellerModMenuIntegration implements ModMenuApi {

	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return AutospellerModMenuKtIntegration.INSTANCE.getModConfigScreenFactory();
	}
}
//?}
