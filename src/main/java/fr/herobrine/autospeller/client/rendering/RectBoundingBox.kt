package fr.herobrine.autospeller.client.rendering

import org.joml.Vector2i
import java.awt.Dimension
import java.awt.Point
import java.awt.Rectangle
import kotlin.math.abs

/**
 * Rectangular bounding box that defines the shape of a tooltip.
 */
data class RectBoundingBox(
    val x1: Int,
    val y1: Int,
    val x2: Int,
    val y2: Int,
) {

    /**
     * Verifies if a [Vector2i] is located in the box.
     *
     * @param position Position to check,
     */
    fun contains(
        position: Vector2i
    ): Boolean {
        return Rectangle(
            Point(x1, y1),
            Dimension(
                abs(x2-x1),  abs(y2-y1)/3
            )
        ).contains(position.x, position.y)
    }
}
