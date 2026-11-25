package fr.herobrine.autospeller.client.util

import com.mojang.blaze3d.platform.InputConstants
import net.minecraft.client.KeyMapping
import net.minecraft.resources.Identifier

//? if neoforge
//import net.neoforged.neoforge.client.settings.KeyConflictContext

//? if < 26.3 {
//import org.lwjgl.glfw.GLFW
//? } else {
import org.lwjgl.sdl.SDLScancode
//? }

var KEYBIND_CATEGORY = KeyMapping.Category(
	Identifier.parse("autospeller:keybinds")
)

val MAPPING_QUICK_ADD = KeyMapping(
	"key.autospeller.quick_add",
	//? if neoforge
	//KeyConflictContext.GUI,
	//? if < 26.3 {
	/*InputConstants.Type.KEYSYM,
	GLFW.GLFW_KEY_LEFT_ALT,
	*///? } else {
	InputConstants.Type.KEYBOARD,
	SDLScancode.SDL_SCANCODE_LALT,
	//? }
	KEYBIND_CATEGORY
)
