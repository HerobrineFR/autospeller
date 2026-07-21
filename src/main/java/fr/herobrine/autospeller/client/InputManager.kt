package fr.herobrine.autospeller.client

import com.mojang.blaze3d.platform.InputConstants
import fr.herobrine.autospeller.client.ext.getKey
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper
import net.minecraft.client.KeyMapping
import net.minecraft.client.Minecraft
import net.minecraft.resources.Identifier
import org.lwjgl.glfw.GLFW

object InputManager {

	lateinit var keybindCategory: KeyMapping.Category
	lateinit var addWordKeyMapping: KeyMapping

	fun registerKeybinds() {
		keybindCategory = KeyMapping.Category.register(
			Identifier.parse("autospeller:keybinds")
		)

		addWordKeyMapping = KeyMappingHelper.registerKeyMapping(
			KeyMapping(
				"key.autospeller.quick_add",
				InputConstants.Type.KEYSYM,
				GLFW.GLFW_KEY_LEFT_ALT,
				keybindCategory
			)
		)
	}

	fun isAddingWord(): Boolean {
		val windowHandle = Minecraft.getInstance().getWindow().handle();
		return GLFW.glfwGetKey(windowHandle, addWordKeyMapping.getKey().value) == GLFW.GLFW_PRESS;
	}
}
