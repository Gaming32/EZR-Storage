plugins {
    id("fabric-loom") version "1.14.10"
}

version = "1.1.2"
group = "io.github.gaming32"

repositories {
    maven("https://jitpack.io")
    exclusiveContent {
        forRepository {
            maven("https://api.modrinth.com/maven") {
                name = "Modrinth"
            }
        }
        filter {
            includeGroup("maven.modrinth")
        }
    }
    maven("https://maven.terraformersmc.com/releases") {
        name = "TerraformersMC"
    }
    maven("https://maven.parchmentmc.org") {
        name = "ParchmentMC"
    }

    // Create stuff
    maven("https://mvn.devos.one/releases")
    maven("https://mvn.devos.one/snapshots")
    maven("https://maven.tterrag.com/")
    maven("https://maven.jamieswhiteshirt.com/libs-release")
    maven("https://raw.githubusercontent.com/Fuzss/modresources/main/maven/") {
        name = "Fuzs Mod Resources"
    }
    maven("https://maven.createmod.net/")
}

dependencies {
    minecraft("com.mojang:minecraft:1.20.1")
    @Suppress("UnstableApiUsage")
    mappings(loom.layered {
        officialMojangMappings()
        parchment("org.parchmentmc.data:parchment-1.20.1:2023.09.03@zip")
    })
    modImplementation("net.fabricmc:fabric-loader:0.18.4")

    modCompileOnly("com.simibubi.create:create-fabric:6.0.8.1+build.1744-mc1.20.1")
    modCompileOnly("maven.modrinth:create-fabric:6.0.8.1+build.1744-mc1.20.1")
    modCompileOnly("dev.emi:emi-fabric:1.1.22+1.20.1")

    modImplementation("net.fabricmc.fabric-api:fabric-api:0.92.6+1.20.1")
}

loom {
    accessWidenerPath = file("src/main/resources/ezrstorage.accessWidener")
}

tasks.processResources {
    inputs.property("version", project.version)
    filteringCharset = "UTF-8"

    filesMatching("fabric.mod.json") {
        expand("version" to project.version)
    }
}

tasks.withType<JavaCompile> {
    // ensure that the encoding is set to UTF-8, no matter what the system default is
    // this fixes some edge cases with special characters not displaying correctly
    // see http://yodaconditions.net/blog/fix-for-java-file-encoding-problems-with-gradle.html
    // If Javadoc is generated, this must be specified in that task too.
    options.encoding = "UTF-8"
    options.release = 17
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(21)
    // Loom will automatically attach sourcesJar to a RemapSourcesJar task and to the "build" task
    // if it is present.
    // If you remove this line, sources will not be generated.
    withSourcesJar()
}

tasks.jar {
    archiveBaseName = "ezrstorage"
    from("LICENSE") {
        rename { "${it}_${archiveBaseName.get()}" }
    }
}
