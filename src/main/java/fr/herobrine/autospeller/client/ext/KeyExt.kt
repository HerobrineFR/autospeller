package fr.herobrine.autospeller.client.ext

import fr.herobrine.autospeller.mixin.gui.KeyMappingAccessor
import net.minecraft.client.KeyMapping

fun KeyMapping.getKey() = this.asAccessor().key

fun KeyMapping.asAccessor(): KeyMappingAccessor {
	return this as KeyMappingAccessor
}
