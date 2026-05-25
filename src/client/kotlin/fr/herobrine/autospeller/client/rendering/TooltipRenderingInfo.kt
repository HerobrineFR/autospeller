package fr.herobrine.autospeller.client.rendering

import org.joml.Vector2i

data class TooltipRenderingInfo(
    val origin: Vector2i,
    val size: Vector2i,
) {
    fun getPaddedWidth(): Int = size.x
    fun getPaddedHeight(): Int = size.y

    fun getScreenEnd(): Vector2i = origin.add(getPaddedWidth(), getPaddedHeight())

    fun createBoundingBox(): RectBoundingBox {
        return RectBoundingBox(
            origin.x,
            origin.y,
            getScreenEnd().x,
            getScreenEnd().y,
        )
    }
}
