package fr.herobrine.autospeller.client.config

import dev.isxander.yacl3.api.ConfigCategory
import dev.isxander.yacl3.api.ListOption
import dev.isxander.yacl3.api.Option
import dev.isxander.yacl3.api.OptionGroup
import dev.isxander.yacl3.api.YetAnotherConfigLib
import dev.isxander.yacl3.api.controller.ColorControllerBuilder
import dev.isxander.yacl3.api.controller.EnumControllerBuilder
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder
import dev.isxander.yacl3.api.controller.StringControllerBuilder
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler
import dev.isxander.yacl3.config.v2.api.SerialEntry
import dev.isxander.yacl3.gui.controllers.ColorController
import dev.isxander.yacl3.gui.controllers.slider.IntegerSliderController
import fr.herobrine.autospeller.client.linting.LanguageToolInputProcessor
import fr.herobrine.autospeller.config.LinterConfigurationInterface
import fr.herobrine.autospeller.ext.asLiteral
import fr.herobrine.autospeller.ext.asTranslatable
import fr.herobrine.autospeller.ignore.IgnoreList
import fr.herobrine.autospeller.language.Language
import fr.herobrine.autospeller.language.LanguageLevel
import fr.herobrine.autospeller.language.WordElement
import fr.herobrine.autospeller.language.WordSet
import fr.herobrine.autospeller.service.IgnoreFilter
import fr.herobrine.autospeller.service.InputProcessor
import net.minecraft.client.gui.screens.Screen
import java.awt.Color
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

class AutospellerConfiguration: LinterConfigurationInterface {
    @SerialEntry(value = "enable_mod")
    override var enableMod = true

    @SerialEntry(value = "ignored_words")
    var ignoredWords = emptyList<String>()

	@SerialEntry(value = "builtin_dictionary")
	var builtinDictionary = emptyList<String>()

    @SerialEntry(value = "underline_color")
    override var underlineColor = Color.RED

    @SerialEntry(value = "language")
    override var language = Language.ENGLISH

	@SerialEntry(value = "debounce_delay")
	override var debounceDelay: Int = 200

	@SerialEntry(value = "max_suggestions")
	override var maxSuggestions: Int = 4

	@SerialEntry(value = "language_level")
	override var languageLevel: LanguageLevel = LanguageLevel.DEFAULT

    private var inputProcessor: LanguageToolInputProcessor? = null

    companion object {
        val HANDLER = ConfigClassHandler.createBuilder(
            AutospellerConfiguration::class.java
        )
    }

    fun generateScreen(parentScreen: Screen): Screen {
        return with(YetAnotherConfigLib.createBuilder()) {
            title("text.config.autospeller.title".asTranslatable())

            category(with(ConfigCategory.createBuilder()) {
                name("text.config.autospeller.category.general".asTranslatable())

                option(
                    with(Option.createBuilder<Boolean>()) {
                        name("text.config.autospeller.option.enable_mod".asTranslatable())
                        controller(TickBoxControllerBuilder::create)
                        binding(true, this@AutospellerConfiguration::enableMod, {value -> this@AutospellerConfiguration.enableMod = value})
                        build()
                    }
                )

                group(with(OptionGroup.createBuilder()) {
					name("text.config.autospeller.group.language".asTranslatable())
					option(
						with(Option.createBuilder<Language>()) {
							name("text.config.autospeller.selected_language".asTranslatable())
							controller({ opt -> EnumControllerBuilder.create(opt).enumClass(Language::class.java) })
							binding(Language.ENGLISH, this@AutospellerConfiguration::language, { lang -> this@AutospellerConfiguration.language = lang })
							build()
						}
					)

					option(
						with(Option.createBuilder<LanguageLevel>()) {
							name("text.config.autospeller.language_level".asTranslatable())
							controller({ opt -> EnumControllerBuilder.create(opt).enumClass(LanguageLevel::class.java) })
							binding(LanguageLevel.DEFAULT, this@AutospellerConfiguration::languageLevel, { lang -> this@AutospellerConfiguration.languageLevel = lang })
							build()
						}
					)

					build()
				})

				option(
					with(ListOption.createBuilder<String>()) {
						name("text.config.autospeller.option.ignored_words".asTranslatable())
						binding(listOf<String>(), this@AutospellerConfiguration::ignoredWords, { value -> this@AutospellerConfiguration.ignoredWords = value })
						controller(StringControllerBuilder::create)
						initial("")
						build()
					}
				)

                build()
            })

            category(with(ConfigCategory.createBuilder()) {
                name("text.config.autospeller.category.gui".asTranslatable())
                tooltip("text.config.autospeller.category.gui.description".asTranslatable())

				option(
					with(Option.createBuilder<Int>()) {
						name("text.config.autospeller.option.max_suggestions".asTranslatable())
						controller({ opt -> IntegerSliderControllerBuilder.create(opt).range(1, 5).step(1) })
						binding(4, this@AutospellerConfiguration::maxSuggestions, { max -> this@AutospellerConfiguration.maxSuggestions = max })
						build()
					}
				)

				option(
					with(Option.createBuilder<Int>()) {
						name("text.config.autospeller.option.debounce_delay".asTranslatable())
						controller({ opt -> IntegerSliderControllerBuilder.create(opt).range(100, 900).step(50).formatValue { value ->
							return@formatValue "$value ms".asLiteral()
						} })

						binding(200, this@AutospellerConfiguration::debounceDelay, { delay -> this@AutospellerConfiguration.debounceDelay = delay })
						build()
					}
				)

                option(with(Option.createBuilder<Color>()) {
                    controller(ColorControllerBuilder::create)
                    name("text.config.autospeller.option.underline_color".asTranslatable())
                    binding(Color.RED, this@AutospellerConfiguration::underlineColor, { color -> this@AutospellerConfiguration.underlineColor = color })

                    build()
                })

                build()
            })

            save({
                MOD_CONFIG_HANDLER.save()
				this@AutospellerConfiguration.inputProcessor?.languageLevel = this@AutospellerConfiguration.languageLevel

				if(this@AutospellerConfiguration.inputProcessor?.language != this@AutospellerConfiguration.language) {
					this@AutospellerConfiguration.inputProcessor?.language = this@AutospellerConfiguration.language
				}
            })

            return@with build().generateScreen(parentScreen)
        }
    }

    override fun ignoreFilter(): IgnoreFilter = IgnoreFilter(
        IgnoreList(WordSet(
            ignoredWords.map { WordElement(it) }
        ))
    )

    override fun createInputProcessor(): LanguageToolInputProcessor {
        return when(this.inputProcessor == null) {
            false -> this.inputProcessor!!
            else -> {
                this.inputProcessor = LanguageToolInputProcessor(ignoreFilter = this.ignoreFilter(), languageLevel = this.languageLevel)

                return with(this.inputProcessor!!) {
                    language = this@AutospellerConfiguration.language

                    this
                }
            }
        }

    }

	override fun getIgnoreFilter(): IgnoreFilter? {
		return IgnoreFilter(
			IgnoreList(
				WordSet(this.ignoredWords.map { w -> WordElement(w) }.plus(
					this.builtinDictionary.map { w -> WordElement(w) }
				))
			)
		)
	}
}
