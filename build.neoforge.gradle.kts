plugins {
	id("mod-platform")
	id("net.neoforged.moddev")
	kotlin("jvm") version "2.3.21"
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
}

platform {
	loader = "neoforge"
	dependencies {
		required("minecraft") {
			forgeLikeVersionRange = prop("deps.minecraft")
		}
		required("neoforge") {
			forgeLikeVersionRange.set("[1,)")
		}
		required("yet_another_config_lib_v3") {
			forgeLikeVersionRange = ">=${prop("deps.yacl")}-${loader}"
		}
		required("kotlinforforge") {
			forgeLikeVersionRange = ">=${prop("deps.kotlin-for-forge")}"
		}
	}
}

neoForge {
	version = prop("deps.neoforge")
	accessTransformers.from(rootProject.file("src/main/resources/aw/${stonecutter.current.version}.cfg"))
	validateAccessTransformers = true

	if (hasProperty("deps.parchment")) parchment {
		val (mc, ver) = prop("deps.parchment").split(':')
		mappingsVersion = ver
		minecraftVersion = mc
	}

	runs {
		register("client") {
			client()
			gameDirectory = file("run/")
			ideName = "NeoForge Client (${stonecutter.current.version})"
			programArgument("--username=Dev")
		}
		register("server") {
			server()
			gameDirectory = file("run/")
			ideName = "NeoForge Server (${stonecutter.current.version})"
		}
	}

	mods {
		register(prop("mod.id")) {
			sourceSet(sourceSets["main"])
		}
	}
	sourceSets["main"].resources.srcDir("${rootDir}/versions/datagen/${sc.current.version.split("-")[0]}/src/main/generated")
}

repositories {
	mavenCentral()
	strictMaven("https://api.modrinth.com/maven", "maven.modrinth") { name = "Modrinth" }
}

val transitiveInclude by configurations.creating {
	isCanBeConsumed = false
	isCanBeResolved = true
}

configurations.implementation.get().extendsFrom(transitiveInclude)

dependencies {
	// implementation(libs.moulberry.mixinconstraints)
	implementation("dev.isxander:yet-another-config-lib:${prop("deps.yacl")}-neoforge")

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
		dependencies.add("jarJar", "${dep.group}:${dep.name}:${dep.version}")
	}
}

tasks.named("createMinecraftArtifacts") {
	dependsOn(tasks.named("stonecutterGenerate"))
}
