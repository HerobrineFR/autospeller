package fr.herobrine.autospeller.client

import fr.herobrine.autospeller.client.ext.getKey
import fr.herobrine.autospeller.client.util.KEYBIND_CATEGORY
import fr.herobrine.autospeller.client.util.MAPPING_QUICK_ADD
//? if fabric
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper
import net.minecraft.client.KeyMapping
import net.minecraft.client.Minecraft
import net.minecraft.resources.Identifier
//? if >= 26.3
import org.lwjgl.sdl.SDLKeyboard

//? if < 26.3
//import org.lwjgl.glfw.GLFW

object InputManager {

	lateinit var keybindCategory: KeyMapping.Category
	lateinit var addWordKeyMapping: KeyMapping

	fun registerKeybinds() {
		//? if fabric {
		keybindCategory = KeyMapping.Category.register(
			Identifier.parse("autospeller:keybinds")
		)

		KEYBIND_CATEGORY = keybindCategory

		addWordKeyMapping = KeyMappingHelper.registerKeyMapping(
			MAPPING_QUICK_ADD
		)
		//? } else if neoforge {
		/*keybindCategory = KEYBIND_CATEGORY
		addWordKeyMapping = MAPPING_QUICK_ADD
		*///? }
	}

	fun isAddingWord(): Boolean {
		//? if < 26.3 {
		/*val windowHandle = Minecraft.getInstance().window.handle();
		return GLFW.glfwGetKey(windowHandle, addWordKeyMapping.getKey().value) == GLFW.GLFW_PRESS;
		*///? } else {
		val state = SDLKeyboard.SDL_GetKeyboardState();

		return state?.get(addWordKeyMapping.getKey().value)?.toInt() != 0;
		//? }
	}
}
