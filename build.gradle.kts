import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.github.jengelman.gradle.plugins.shadow.tasks.ConfigureShadowRelocation

plugins {
    java
    kotlin("jvm") version "1.6.0"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "com.spicean"
version = "1.0"

repositories {
    mavenCentral()
    maven("https://repo.purpurmc.org/snapshots")
}

val include by configurations.creating
dependencies {
    compileOnly("org.purpurmc.purpur:purpur-api:1.18.1-R0.1-SNAPSHOT")

    implementation("net.dv8tion:JDA:5.0.0-alpha.2") {
        exclude("opus-java")
    }
    include("net.dv8tion:JDA:5.0.0-alpha.2") {
        exclude("opus-java")
    }

    implementation(kotlin("stdlib"))
    include(kotlin("stdlib"))
}

tasks.register(
    "relocateShadowJar",
    ConfigureShadowRelocation::class.java
) {
    target = tasks["shadowJar"] as ShadowJar?
    prefix = "spicesw.lib"
}

tasks.withType<ShadowJar> {
    this.archiveBaseName.set("ipbot")
    this.configurations = mutableListOf(include) as List<FileCollection>?
}
