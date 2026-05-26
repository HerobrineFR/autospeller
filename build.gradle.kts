import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("net.fabricmc.fabric-loom")
    `maven-publish`
    id("org.jetbrains.kotlin.jvm") version "2.3.21"
}

version = providers.gradleProperty("mod_version").get()
group = providers.gradleProperty("maven_group").get()

val loaderVersion = providers.gradleProperty("loader_version").get()
val kotlinLoaderVersion = providers.gradleProperty("fabric_kotlin_version").get()
val minecraftVersion = providers.gradleProperty("minecraft_version").get()
val targetJavaVersion = 25

java {
    toolchain.languageVersion = JavaLanguageVersion.of(targetJavaVersion)
    // Loom will automatically attach sourcesJar to a RemapSourcesJar task and to the "build" task
    // if it is present.
    // If you remove this line, sources will not be generated.
    withSourcesJar()
}

loom {
    splitEnvironmentSourceSets()

    mods {
        register("autospeller") {
            sourceSet(sourceSets.main.get())
            sourceSet(sourceSets.getByName("client"))
        }
    }
}

fabricApi {
    configureDataGeneration {
        client = true
    }
}

repositories {
    maven { url = uri("https://maven.terraformersmc.com/") }
    maven { url = uri("https://maven.shedaniel.me/") }
    maven("https://maven.isxander.dev/releases") {
        name = "Xander Maven"
    }
}

val transitiveInclude by configurations.creating {
    isCanBeConsumed = false
    isCanBeResolved = true
}

configurations.implementation.get().extendsFrom(transitiveInclude)

dependencies {
    // To change the versions see the gradle.properties file
    minecraft("com.mojang:minecraft:${providers.gradleProperty("minecraft_version").get()}")

    implementation("net.fabricmc:fabric-loader:${providers.gradleProperty("loader_version").get()}")

    // Fabric API. This is technically optional, but you probably want it anyway.
    implementation("net.fabricmc.fabric-api:fabric-api:${providers.gradleProperty("fabric_api_version").get()}")
    implementation("net.fabricmc:fabric-language-kotlin:${providers.gradleProperty("fabric_kotlin_version").get()}")
    implementation("com.terraformersmc:modmenu:${providers.gradleProperty("modmenu_version").get()}")
    implementation("dev.isxander:yet-another-config-lib:${providers.gradleProperty("yacl_version").get()}")

    transitiveInclude("org.languagetool:language-fr:6.6") {
        exclude("it.unimi.dsi")
        exclude("org.apache.commons", "commons-lang3")
    }
    transitiveInclude("org.languagetool:languagetool-core:6.6") {
        exclude("it.unimi.dsi")
        exclude("org.apache.commons", "commons-lang3")
    }
}


tasks.processResources {
    val version = version
    val loaderVersion = loaderVersion
    val kotlinLoaderVersion = kotlinLoaderVersion
    val minecraftVersion = minecraftVersion

    inputs.property("version", version)
    inputs.property("loader_version", loaderVersion)
    inputs.property("kotlin_loader_version", kotlinLoaderVersion)
    inputs.property("minecraft_version", minecraftVersion)

    filesMatching("fabric.mod.json") {
        expand(
            mapOf(
                "version" to version,
                "loader_version" to loaderVersion,
                "kotlin_loader_version" to loaderVersion,
                "minecraft_version" to minecraftVersion
            )
        )
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.release = 25
    dependsOn(tasks.compileKotlin)
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_25
    }
}

java {
    // Loom will automatically attach sourcesJar to a RemapSourcesJar task and to the "build" task
    // if it is present.
    // If you remove this line, sources will not be generated.
    withSourcesJar()

    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25
}

afterEvaluate {
    transitiveInclude.resolvedConfiguration.resolvedArtifacts.forEach { artifact ->
        val dep = artifact.moduleVersion.id
        dependencies.add("include", "${dep.group}:${dep.name}:${dep.version}")
    }
}

tasks.jar {
    val projectName = project.name
    inputs.property("projectName", projectName)

    from(sourceSets.main.get().output)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

// configure the maven publication
publishing {
    publications {
        register<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }

    // See https://docs.gradle.org/current/userguide/publishing_maven.html for information on how to set up publishing.
    repositories {
        // Add repositories to publish to here.
        // Notice: This block does NOT have the same function as the block in the top level.
        // The repositories here will be used for publishing your artifact, not for
        // retrieving dependencies.
    }
}