package fr.herobrine.autospeller.client.config

import com.google.gson.GsonBuilder
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.resources.Identifier


val MOD_CONFIG_HANDLER = with(ConfigClassHandler.createBuilder(
    AutospellerConfiguration::class.java
)) {
    id(Identifier.fromNamespaceAndPath("autospeller", "main_configuration"))
    serializer({ config ->
        return@serializer with(GsonConfigSerializerBuilder.create(config)) {
            setPath(FabricLoader.getInstance().configDir.resolve("autospeller.json5"))
            appendGsonBuilder(GsonBuilder::setPrettyPrinting)
            setJson5(true)

            return@with build()
        }
    })
}.build()
