package fr.herobrine.autospeller.platform.neoforge
//? if neoforge {
/*import fr.herobrine.autospeller.Autospeller
import fr.herobrine.autospeller.client.util.KEYBIND_CATEGORY
import fr.herobrine.autospeller.client.util.MAPPING_QUICK_ADD
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent

@EventBusSubscriber(modid = Autospeller.MOD_ID, value = [Dist.CLIENT])
object NeoforgeClientEventSubscriber {

	@JvmStatic
	@SubscribeEvent
	fun onCientSetup(event: FMLClientSetupEvent) {
		Autospeller.onInitializeClient()
	}

	@JvmStatic
	@SubscribeEvent
	fun registerBindings(event: RegisterKeyMappingsEvent) {
		with(event) {
			registerCategory(
				KEYBIND_CATEGORY
			)

			register(
				MAPPING_QUICK_ADD
			)
		}
	}

}
*///? }
