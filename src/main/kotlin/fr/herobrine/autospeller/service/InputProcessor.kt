package fr.herobrine.autospeller.service

import fr.herobrine.autospeller.language.TokenInputElement
import fr.herobrine.autospeller.linting.LintingResult

interface InputProcessor {
    fun process(input: TokenInputElement): LintingResult
}