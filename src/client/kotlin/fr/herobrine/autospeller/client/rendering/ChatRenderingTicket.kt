package fr.herobrine.autospeller.client.rendering

import fr.herobrine.autospeller.client.linting.ChatLintingSession
import fr.herobrine.autospeller.linting.TextSuggestion

data class ChatRenderingTicket(
    val lintingSession: ChatLintingSession,
    val textSuggestions: List<TextSuggestion>,
    )
