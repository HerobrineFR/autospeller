package fr.herobrine.autospeller.client.util

import com.mojang.blaze3d.platform.InputConstants
import net.minecraft.client.KeyMapping
import net.minecraft.resources.Identifier
//? if neoforge
//import net.neoforged.neoforge.client.settings.KeyConflictContext
import org.lwjgl.glfw.GLFW

var KEYBIND_CATEGORY = KeyMapping.Category(
	Identifier.parse("autospeller:keybinds")
)

val MAPPING_QUICK_ADD = KeyMapping(
	"key.autospeller.quick_add",
	//? if neoforge
	//KeyConflictContext.GUI,
	InputConstants.Type.KEYSYM,
	GLFW.GLFW_KEY_LEFT_ALT,
	KEYBIND_CATEGORY
)
