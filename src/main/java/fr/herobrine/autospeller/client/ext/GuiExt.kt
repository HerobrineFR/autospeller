package fr.herobrine.autospeller.client.ext

import fr.herobrine.autospeller.mixin.gui.EditBoxAccessor
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.components.EditBox

/**
 * Gets the visible text string in the rendered EditBox.
 */
fun EditBox.visibleText(): String {
    val accessor = this as EditBoxAccessor
    return this.asAccessor().font.plainSubstrByWidth(
        this.value.substring(this.displayPosition), this.innerWidth
    )
}

fun EditBox.asAccessor(): EditBoxAccessor {
    return this as EditBoxAccessor
}

fun EditBox.getFont(): Font {
    return this.asAccessor().font
}