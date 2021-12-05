plugins {
    kotlin("jvm") version "1.6.0"
}

group = "com.spicean"
version = "1.0"

repositories {
    maven("https://repo.pl3x.net/")
}

dependencies {
    compileOnly("net.pl3x.purpur:purpur-api:1.17.1-R0.1-SNAPSHOT")
    implementation("net.dv8tion:JDA:5.0.0-alpha.2") {
        exclude("opus-java")
    }
}

