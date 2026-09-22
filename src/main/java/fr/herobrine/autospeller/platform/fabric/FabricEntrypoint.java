package fr.herobrine.autospeller.platform.fabric;

//? fabric {

import dev.kikugie.fletching_table.annotation.fabric.Entrypoint;
import fr.herobrine.autospeller.Autospeller;
import net.fabricmc.api.ModInitializer;

@Entrypoint("main")
public class FabricEntrypoint implements ModInitializer {

	@Override
	public void onInitialize() {
		Autospeller.INSTANCE.onInitialize();
		FabricEventSubscriber.registerEvents();
	}
}
//?}
