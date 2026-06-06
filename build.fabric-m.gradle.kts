plugins {
	id("mod-platform")
	id("net.fabricmc.fabric-loom")
}

kotlin {
	compilerOptions {
		jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_25)
	}
}

stonecutter {
	val (version, loader) = current.project.split('-', limit = 2)
	properties.tags(version, loader)

	replacements.string(current.parsed >= "1.21.11") {
		replace("ResourceLocation", "Identifier")
		replace("location()", "identifier()")
	}

	replacements.string(current.parsed >= "26.1.2") {
		replace("FabricDataOutput", "FabricPackOutput")
	}
}



platform {
	loader = "fabric-m"
	dependencies {
		required("minecraft") {
			fabricLikeVersionRange = prop("deps.minecraft").replace("-pre-", "-pre.")
		}
		required("fabric-api") {
			slug("fabric-api")
			fabricLikeVersionRange = ">=${prop("deps.fabric-api")}"
		}
		required("fabricloader") {
			fabricLikeVersionRange = ">=${prop("deps.fabric-loader")}"
			modrinth = "yacl"
		}
		required("yet_another_config_lib_v3") {
			fabricLikeVersionRange = ">=${prop("deps.yacl")}-fabric"
		}
		required("fabric-language-kotlin") {
			fabricLikeVersionRange = ">=${prop("deps.fabric-language-kotlin")}"
			modrinth = "fabric-language-kotlin"
		}
		optional("modmenu") {
			modrinth = "modmenu"
		}
	}
}

loom {
	accessWidenerPath = rootProject.file("src/main/resources/aw/${stonecutter.current.version}.accesswidener")
	runs.named("client") {
		client()
		ideConfigGenerated(true)
		runDir = "run/"
		environment = "client"
		programArgs("--username=Dev")
		configName = "Fabric Client"
	}
	runs.named("server") {
		server()
		ideConfigGenerated(true)
		runDir = "run/"
		environment = "server"
		configName = "Fabric Server"
	}
}

fabricApi {
	configureDataGeneration {
		outputDirectory = file("${rootDir}/versions/datagen/${sc.current.version.split("-")[0]}/src/main/generated")
		client = true
	}
}

repositories {
	mavenCentral()
	strictMaven("https://maven.terraformersmc.com/", "com.terraformersmc") { name = "TerraformersMC" }
	strictMaven("https://api.modrinth.com/maven", "maven.modrinth") { name = "Modrinth" }
}

val transitiveInclude by configurations.creating {
	isCanBeConsumed = false
	isCanBeResolved = true
}

configurations.implementation.get().extendsFrom(transitiveInclude)

dependencies {
	minecraft("com.mojang:minecraft:${prop("deps.minecraft")}")
	implementation("net.fabricmc:fabric-loader:${prop("deps.fabric-loader")}")
	// implementation(libs.moulberry.mixinconstraints)
	// include(libs.moulberry.mixinconstraints)
	implementation("net.fabricmc.fabric-api:fabric-api:${prop("deps.fabric-api")}")
	implementation("dev.isxander:yet-another-config-lib:${prop("deps.yacl")}-fabric")
	implementation("com.terraformersmc:modmenu:${prop("deps.modmenu")}")
	implementation("net.fabricmc:fabric-language-kotlin:${prop("deps.fabric-language-kotlin")}")
	localRuntime("net.fabricmc:fabric-language-kotlin:${prop("deps.fabric-language-kotlin")}")
	localRuntime("com.terraformersmc:modmenu:${prop("deps.modmenu")}")

	transitiveInclude("org.languagetool:language-en:6.6") {
		exclude("it.unimi.dsi")
		exclude("org.apache.commons", "commons-lang3")
	}

	transitiveInclude("org.languagetool:language-fr:6.6") {
		exclude("it.unimi.dsi")
		exclude("org.apache.commons", "commons-lang3")
	}

	transitiveInclude("org.languagetool:languagetool-core:6.6") {
		exclude("it.unimi.dsi")
		exclude("org.apache.commons", "commons-lang3")
	}
}

afterEvaluate {
	transitiveInclude.resolvedConfiguration.resolvedArtifacts.forEach { artifact ->
		val dep = artifact.moduleVersion.id
		dependencies.add("include", "${dep.group}:${dep.name}:${dep.version}")
	}
}
