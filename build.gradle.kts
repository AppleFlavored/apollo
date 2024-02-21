plugins {
    id("java-library")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "dev.flavored"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://repo.lunarclient.dev")
}

dependencies {
    compileOnly("net.minestom:minestom-snapshots:7320437640")
    api("com.lunarclient:apollo-api:1.0.9")
    api("com.lunarclient:apollo-common:1.0.9")
}