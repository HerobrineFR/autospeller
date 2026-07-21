package fr.herobrine.autospeller.client.rendering

import com.mojang.blaze3d.platform.cursor.CursorTypes
import fr.herobrine.autospeller.client.AutospellerClient
import fr.herobrine.autospeller.client.ext.getFont
import fr.herobrine.autospeller.client.ext.visibleText
import fr.herobrine.autospeller.ext.asTranslatable
import fr.herobrine.autospeller.linting.SessionMode
import fr.herobrine.autospeller.linting.TextSuggestion
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.TextAlignment
import net.minecraft.client.gui.components.EditBox
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.Style
import net.minecraft.resources.Identifier
import net.minecraft.util.CommonColors
import net.minecraft.util.FormattedCharSequence
import org.joml.Vector2i

/**
 * Singleton object used to render the chat suggestion widgets and to
 * handle some interactions.
 */
object ChatRenderer {

    fun displayInputProcessorPendingState(
        graphics: GuiGraphicsExtractor
    ) {
        val textRenderer = graphics.textRenderer()

        val screenX = graphics.guiWidth() - 10
        val screenY = (graphics.guiHeight() / 1.12).toInt()
        textRenderer.accept(
            TextAlignment.RIGHT,
            screenX,
            screenY,
            Component.empty().withColor(CommonColors.GRAY).append(
                "text.config.autospeller.gui.loading_text".asTranslatable()
            )
        )
    }

    /**
     * Replaces in the input in an [EditBox] with the replacement held by a [TooltipWidget].
     *
     * @param tooltip Selected widget, holding the replacement
     * @param editBox Text field of which the input is changed
     */
    fun replaceInput(
        tooltip: TooltipWidget,
        editBox: EditBox,
    ) {
        val currentText = editBox.value
        val newText = currentText.substring(0, tooltip.suggestion.fromPosition) + tooltip.text + currentText.substring(tooltip.suggestion.toPosition)
        editBox.value = newText
    }

    /**
     * Renders a rendering ticket, along with its replacement suggestions and underlines.
     *
     * @param graphics Graphics extractor used
     * @param renderingTicket Ticket holding the text suggestions and session
     * @param pointerPosition Position of the user's pointer
     * @param renderTooltips Defines if the tooltips should be rendered (they can be excluded from rendering, eg. with chat drafts)
     */
    fun submitUnderlinesTicket(
        graphics: GuiGraphicsExtractor,
        renderingTicket: ChatRenderingTicket,
        pointerPosition: Vector2i,
        renderTooltips: Boolean
    ): ChatTooltipWidget? {
        val textInput = renderingTicket.lintingSession.lastInput?.input ?: return null

        val editBox = renderingTicket.lintingSession.editBox
        val displayedText = editBox.visibleText()

        var highlightedReplacement: ChatTooltipWidget? = null

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

                val tooltipPositioner = TextSuggestionTooltipPositioner()
                var currentIteration = 1

                fun originVec(): Vector2i {
                    return positionVec(
                        underlineStartX,
                        currentIteration,
                        editBox
                    )
                }

				if(renderingTicket.lintingSession.sessionMode == SessionMode.DICTIONARY_ADDING) {
					var additionStyle = Identifier.parse("autospeller:addition")
					val originVector = Vector2i(underlineStartX, editBox.y)
					val tooltipWidth = textWidth(textToken)

					val tooltipInfo = TooltipRenderingInfo(
						originVector,
						Vector2i(
							tooltipWidth,
							15
						)
					)

					val widgetBoundingBox = tooltipInfo.createBoundingBox()
					if(widgetBoundingBox.contains(pointerPosition)) {
						highlightedReplacement = AdditionTooltipWidget(textToken)
						graphics.requestCursor(CursorTypes.POINTING_HAND)

						additionStyle = Identifier.parse("autospeller:addition_active")
					}

					renderingQueue.add {
						graphics.tooltip(
							editBox.getFont(),
							listOf(ClientTooltipComponent.create(
								FormattedCharSequence.forward(textToken, Style.EMPTY)
							)),
							underlineStartX, editBox.y,
							tooltipPositioner,
							additionStyle
						)
					}
				}

				if(renderingTicket.lintingSession.sessionMode == SessionMode.LINTING) {
					if(!renderTooltips) {
						return@forEach
					}

					// Draws red underline beneath text.
					graphics.horizontalLine(
						underlineStartX,
						underlineEndX,
						(editBox.y + editBox.height) - 4,
						AutospellerClient.service.linterConfiguration.underlineColor.rgb
					)

					lintSuggestion.suggestedReplacements.forEach { suggestion ->
						var tooltipStyle: Identifier? = null
						val tooltipWidth = textWidth(suggestion)

						fun createTooltipWidget(): TooltipWidget = TooltipWidget(
							suggestion,
							lintSuggestion
						)

						//? if >= 26.2 {
						/*val screen = Minecraft.getInstance().gui.screen()
						*///? } else {
						val screen = Minecraft.getInstance().screen
						//? }
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
        }

        renderingQueue.forEach { renderFunction ->
            renderFunction.run()
        }

        return highlightedReplacement
    }

    /**
     * Returns the origin position that should be applied to a tooltip.
     */
    fun positionVec(underlineStartX: Int, currentIteration: Int, editBox: EditBox): Vector2i {
        val tooltipBoxBottonMargin = 6
        val origin = Vector2i(
            underlineStartX,
            editBox.y - editBox.height - (currentIteration * 12) + tooltipBoxBottonMargin - (4*currentIteration)
        )

        return origin
    }

    /**
     * A clickable suggestion widget.
     *
     * @param text Suggested replacement
     * @param suggestion Suggestion from which the widget comes from
     */
    data class TooltipWidget(
        override val text: String,
        val suggestion: TextSuggestion
    ): ChatTooltipWidget {

    }

	data class AdditionTooltipWidget(
		override val text: String
	): ChatTooltipWidget {
	}
}
