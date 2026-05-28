package fr.herobrine.autospeller.client.rendering

import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner
import org.joml.Vector2i
import org.joml.Vector2ic
import kotlin.math.max

class TextSuggestionTooltipPositioner: ClientTooltipPositioner {
    override fun positionTooltip(
        screenWidth: Int,
        screenHeight: Int,
        x: Int,
        y: Int,
        tooltipWidth: Int,
        tooltipHeight: Int
    ): Vector2ic {
        val result = Vector2i(
            x,
            y,
        )

        this.positionTooltip(
            screenWidth = screenWidth,
            screenHeight = screenHeight,
            result = result,
            tooltipWidth = tooltipWidth,
            tooltipHeight = tooltipHeight,
        )

        return result
    }

    fun positionTooltip(
        screenWidth: Int,
        screenHeight: Int,
        result: Vector2i,
        tooltipWidth: Int,
        tooltipHeight: Int,
    ) {
        if(result.x + tooltipWidth > screenWidth) {
            result.x = max(result.x - 24 - tooltipWidth, 4)
        }

        val paddedHeight = tooltipHeight + 3
        if(result.y + paddedHeight > screenHeight) {
            result.y = screenHeight - paddedHeight
        }
    }

}