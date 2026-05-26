package fr.herobrine.autospeller.client.rendering

import com.mojang.blaze3d.platform.cursor.CursorTypes
import fr.herobrine.autospeller.client.ext.getFont
import fr.herobrine.autospeller.client.ext.visibleText
import fr.herobrine.autospeller.linting.TextSuggestion
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.components.EditBox
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent
import net.minecraft.network.chat.Style
import net.minecraft.resources.Identifier
import net.minecraft.util.FormattedCharSequence
import org.joml.Vector2i

object ChatRenderer {

    fun replaceInput(
        tooltip: TooltipWidget,
        editBox: EditBox,
    ) {
        val currentText = editBox.value
        val newText = currentText.substring(0, tooltip.suggestion.fromPosition) + tooltip.text + currentText.substring(tooltip.suggestion.toPosition)
        editBox.value = newText
    }

    fun submitUnderlinesTicket(
        graphics: GuiGraphicsExtractor,
        renderingTicket: ChatRenderingTicket,
        pointerPosition: Vector2i,
        renderTooltips: Boolean
    ): TooltipWidget? {
        val textInput = renderingTicket.lintingSession.lastInput?.input ?: return null

        val editBox = renderingTicket.lintingSession.editBox
        val displayedText = editBox.visibleText()

        var highlightedReplacement: TooltipWidget? = null

        fun textWidth(text: String): Int {
            return editBox.getFont().width(text)
        }

        val renderingQueue = ArrayList<Runnable>()

        renderingTicket.textSuggestions.forEach { lintSuggestion ->
            val from = lintSuggestion.fromPosition
            val to = lintSuggestion.toPosition

            if(from < textInput.length && to <= textInput.length) {
                val textToken = textInput.substring(from, to)
                val textIndex = displayedText.indexOf(textToken)

                if(textIndex < 0) {
                    return@forEach
                }

                val previousRenderedText = displayedText.substring(0, textIndex)

                val underlineStartX = textWidth(previousRenderedText) + editBox.x
                val underlineEndX = underlineStartX + textWidth(textToken)

                // Draws red underline beneath text.
                graphics.horizontalLine(
                    underlineStartX,
                    underlineEndX,
                    (editBox.y + editBox.height) - 4,
                    -43691
                )

                if(!renderTooltips) {
                    return@forEach
                }

                val tooltipPositioner = TextSuggestionTooltipPositioner()
                var currentIteration = 1

                fun originVec(): Vector2i {
                    return positionVec(
                        underlineStartX,
                        currentIteration,
                        editBox
                    )
                }

                lintSuggestion.suggestedReplacements.forEach { suggestion ->
                    var tooltipStyle: Identifier? = null
                    val tooltipWidth = textWidth(suggestion)

                    fun createTooltipWidget(): TooltipWidget = TooltipWidget(
                        suggestion,
                        lintSuggestion
                    )

                    val screen = Minecraft.getInstance().screen
                    if (screen != null) {

                        with(screen) {
                            val originVector = originVec()

                            tooltipPositioner.positionTooltip(
                                width,
                                height,
                                originVector,
                                tooltipWidth,
                                12
                            )

                            val tooltipInfo = TooltipRenderingInfo(
                                originVector,
                                Vector2i(
                                    tooltipWidth,
                                    15
                                )
                            )

                            val boundingBox = tooltipInfo.createBoundingBox()

                            if(boundingBox.contains(pointerPosition)) {
                                highlightedReplacement = createTooltipWidget()
                            }
                        }
                    }

                    val or = originVec()

                    renderingQueue.add {
                        if(highlightedReplacement == createTooltipWidget()) {
                            graphics.requestCursor(CursorTypes.POINTING_HAND)
                            tooltipStyle = Identifier.parse("autospeller:active")
                        }

                        graphics.tooltip(
                            editBox.getFont(),
                            listOf(ClientTooltipComponent.create(
                                FormattedCharSequence.forward(suggestion, Style.EMPTY)
                            )),
                            or.x,
                            or.y,
                            tooltipPositioner,
                            tooltipStyle
                        )
                    }

                    currentIteration++
                }
            }
        }

        renderingQueue.forEach { renderFunction ->
            renderFunction.run()
        }

        return highlightedReplacement
    }

    fun positionVec(underlineStartX: Int, currentIteration: Int, editBox: EditBox): Vector2i {
        val tooltipBoxBottonMargin = 6
        val origin = Vector2i(
            underlineStartX,
            editBox.y - editBox.height - (currentIteration * 12) + tooltipBoxBottonMargin - (4*currentIteration)
        )

        return origin
    }

    data class TooltipWidget(
        val text: String,
        val suggestion: TextSuggestion
    ) {

    }
}