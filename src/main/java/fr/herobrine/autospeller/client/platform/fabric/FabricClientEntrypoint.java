package fr.herobrine.autospeller.client.platform.fabric;

//? fabric {

import dev.kikugie.fletching_table.annotation.fabric.Entrypoint;
import fr.herobrine.autospeller.Autospeller;
import net.fabricmc.api.ClientModInitializer;

@Entrypoint("client")
public class FabricClientEntrypoint implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		Autospeller.INSTANCE.onInitializeClient();
	}

}
//?}
