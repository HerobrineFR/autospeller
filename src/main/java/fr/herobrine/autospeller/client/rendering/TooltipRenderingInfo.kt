package fr.herobrine.autospeller.client.rendering

import org.joml.Vector2i

/**
 * Information related to the rendering and geometrics of a generated suggestion tooltip.
 *
 * @param origin Origin point of the tooltip
 * @param size 2D size of the tooltip
 */
data class TooltipRenderingInfo(
    val origin: Vector2i,
    val size: Vector2i,
) {
    fun getPaddedWidth(): Int = size.x
    fun getPaddedHeight(): Int = size.y

    fun getScreenEnd(): Vector2i = origin.add(getPaddedWidth(), getPaddedHeight())

    /**
     * Generates the tooltip's [RectBoundingBox] based on its origin and size.
     */
    fun createBoundingBox(): RectBoundingBox {
        return RectBoundingBox(
            origin.x,
            origin.y,
            getScreenEnd().x,
            getScreenEnd().y,
        )
    }
}
