plugins {
    kotlin("jvm") version "1.6.0"
}

group = "com.spicean"
version = "1.0"

repositories {
    mavenCentral()
    maven("https://repo.purpurmc.org/snapshots")
}

dependencies {
    compileOnly("org.purpurmc.purpur:purpur-api:1.18.1-R0.1-SNAPSHOT")
    implementation("net.dv8tion:JDA:5.0.0-alpha.2") {
        exclude("opus-java")
    }
    implementation(kotlin("stdlib"))
}
