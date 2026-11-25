package fr.herobrine.autospeller.platform.neoforge;

//? neoforge {

/*import fr.herobrine.autospeller.Autospeller;
import fr.herobrine.autospeller.client.config.AutospellerConfiguration;
import fr.herobrine.autospeller.client.integration.AutospellerNeoforgeMenuIntegration;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(Autospeller.MOD_ID)
public class NeoforgeEntrypoint {

	public NeoforgeEntrypoint() {
		Autospeller.INSTANCE.onInitialize();
		ModLoadingContext.get().registerExtensionPoint(IConfigScreenFactory.class, () -> AutospellerNeoforgeMenuIntegration.INSTANCE::createMenu);
	}
}
*///?}
