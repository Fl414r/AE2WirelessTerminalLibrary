buildscript {
    repositories {
        mavenCentral()
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }
    }
}

plugins {
    id("fabric-loom") version "1.3-SNAPSHOT"
    id("com.diffplug.spotless") version "6.21.0"
    id("maven-publish")
    id("io.github.juuxel.loom-vineflower") version "1.11.0"
    java
    idea
}

val modVersion: String by project
val modloader: String by project
val minecraftVersion: String by project
val parchmentMinecraftVersion: String by project
val parchmentVersion: String by project
val fabricLoaderVersion: String by project
val fabricApiVersion: String by project
val trinketsVersion: String by project
val ccaVersion: String by project
val clothVersion: String by project
val modMenuVersion: String by project
val ae2Version: String by project
val architecturyVersion: String by project
val runtimeItemlistMod: String by project
val jeiMinecraftVersion: String by project
val jeiVersion: String by project
val reiVersion: String by project
val emiVersion: String by project

version = "$modVersion-SNAPSHOT"

val pr = System.getenv("PR_NUMBER") ?: ""
if (pr != "") {
    version = "$modVersion+pr$pr"
}

val tag = System.getenv("TAG") ?: ""
if (tag != "") {
    if (!tag.contains(modloader)) {
        throw GradleException("Tags for the $modloader version should contain ${modloader}: $tag")
    }
    version = tag
}

dependencies {
    minecraft("com.mojang:minecraft:${minecraftVersion}")
    mappings(loom.layered {
        officialMojangMappings()
        parchment("org.parchmentmc.data:parchment-${parchmentMinecraftVersion}:${parchmentVersion}@zip")
    })

    modImplementation("net.fabricmc:fabric-loader:${fabricLoaderVersion}")
    modApi("net.fabricmc.fabric-api:fabric-api:${fabricApiVersion}")
    modCompileOnly("dev.emi:trinkets:${trinketsVersion}") { isTransitive = false }
    //modRuntimeOnly("dev.emi:trinkets:${trinketsVersion}")
    modCompileOnly("dev.onyxstudios.cardinal-components-api:cardinal-components-base:${ccaVersion}")
    modRuntimeOnly("me.shedaniel.cloth:cloth-config-${modloader}:${clothVersion}")
    modCompileOnly("com.terraformersmc:modmenu:${modMenuVersion}")
    modCompileOnly("dev.architectury:architectury-${modloader}:${architecturyVersion}")
    modImplementation("appeng:appliedenergistics2-${modloader}:${ae2Version}") {
        exclude(group = "mezz.jei")
        exclude(group = "me.shedaniel")
        exclude(group = "net.fabricmc.fabric-api")
    }

    modCompileOnly("me.shedaniel:RoughlyEnoughItems-${modloader}:${reiVersion}") {
        exclude(group = "net.fabricmc.fabric-api")
    }
    modCompileOnly("mezz.jei:jei-${jeiMinecraftVersion}-${modloader}:${jeiVersion}") {
        exclude(group = "mezz.jei")
    }

    when (runtimeItemlistMod) {
        "rei" -> modRuntimeOnly("me.shedaniel:RoughlyEnoughItems-${modloader}:${reiVersion}") {
            exclude(group = "net.fabricmc.fabric-api")
        }

        "jei" -> modRuntimeOnly("mezz.jei:jei-${jeiMinecraftVersion}-${modloader}:${jeiVersion}") {
            exclude(group = "mezz.jei")
        }

        "emi" -> {
            modRuntimeOnly("dev.emi:emi-${modloader}:${emiVersion}+${minecraftVersion}")
            modRuntimeOnly("mezz.jei:jei-${jeiMinecraftVersion}-${modloader}:${jeiVersion}") {
                exclude(group = "mezz.jei")
            }
        }
    }

    implementation("com.google.code.findbugs:jsr305:3.0.2")
}

repositories {
    mavenLocal()
    mavenCentral()
    maven {
        url = uri("https://modmaven.dev/")
        content {
            includeGroup("net.fabricmc.fabric-api")
            includeGroup("appeng")
            includeGroup("mezz.jei")
        }
    }
    maven {
        url = uri("https://maven.bai.lol")
        content {
            includeGroup("mcp.mobius.waila")
            includeGroup("lol.bai")
        }
    }
    maven {
        url = uri("https://maven.shedaniel.me/")
        content {
            includeGroup("me.shedaniel")
            includeGroup("me.shedaniel.cloth")
            includeGroup("dev.architectury")
        }
    }
    maven {
        url = uri("https://maven.terraformersmc.com/")
        content {
            includeGroup("com.terraformersmc")
            includeGroup("dev.emi")
        }
    }
    maven {
        url = uri("https://maven.ladysnake.org/releases")
        content {
            includeGroup("dev.onyxstudios.cardinal-components-api")
        }
    }
    maven {
        url = uri("https://maven.parchmentmc.net/")
        content {
            includeGroup("org.parchmentmc.data")
        }
    }
    maven {
        url = uri("https://maven.theillusivec4.top/")
        content {
            includeGroup("top.theillusivec4.curios")
        }
    }
    maven {
        url = uri("https://api.modrinth.com/maven")
        content {
            includeGroup("maven.modrinth")
        }
    }
}

java {
    withSourcesJar()
}

tasks {
    jar {
        finalizedBy("remapJar")
    }

    processResources {
        val resourceTargets = "fabric.mod.json"

        val replaceProperties = mapOf(
                "version" to version as String,
                "ae2_version" to ae2Version
        )

        inputs.properties(replaceProperties)
        filesMatching(resourceTargets) {
            expand(replaceProperties)
        }
    }
    withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.release.set(17)
    }
}

configure<com.diffplug.gradle.spotless.SpotlessExtension> {
    java {
        target("/src/*/java/**/*.java")

        endWithNewline()
        indentWithSpaces()
        removeUnusedImports()
        toggleOffOn()
        eclipse().configFile("codeformat/codeformat.xml")
        importOrderFile("codeformat/ae2wtlib.importorder")
    }
}
