package fr.herobrine.autospeller.ext

import net.minecraft.network.chat.Component

fun String.asLiteral(): Component {
    return Component.literal(this)
}

fun String.asTranslatable(): Component {
    return Component.translatable(this)
}