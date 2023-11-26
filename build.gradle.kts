plugins {
    kotlin("jvm") version "1.8.0"
    application
}

group = "fr.magic"
version = "1.0-SNAPSHOT"
val visuiVersion = "1.5.2"

repositories {
    mavenCentral()
}

dependencies {
    implementation("junit:junit:4.13.1")
    implementation("junit:junit:4.13.1")
    testImplementation(kotlin("test"))

    implementation("com.badlogicgames.gdx:gdx:1.12.0")
    implementation("com.badlogicgames.gdx:gdx-backend-lwjgl:1.12.0")
    implementation("com.badlogicgames.gdx:gdx-platform:1.12.0:natives-desktop")

    implementation("com.kotcrab.vis:vis-ui:$visuiVersion")
    implementation(kotlin("reflect"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
}


tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(11)
}

application {
    mainClass.set("MainKt")
}