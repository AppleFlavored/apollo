plugins {
    id("java")
}

repositories {
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://repo.lunarclient.dev")
}

dependencies {
    implementation("net.minestom:minestom-snapshots:7320437640")
    implementation(project(":"))
}