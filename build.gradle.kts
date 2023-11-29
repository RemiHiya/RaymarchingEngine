plugins {
    kotlin("jvm") version "1.8.0"
    application
}

group = "fr.magic"
version = "1.0-SNAPSHOT"
val visuiVersion = "1.5.2"

val lwjglVersion = "3.3.3"
val lwjglNatives = "natives-windows"

repositories {
    mavenCentral()
}

dependencies {
    implementation("junit:junit:4.13.1")
    implementation("junit:junit:4.13.1")
    testImplementation(kotlin("test"))

    implementation("io.github.spair:imgui-java-app:1.86.11")
    api("io.github.spair:imgui-java-binding:1.86.11")
    api("io.github.spair:imgui-java-lwjgl3:1.86.11")
    api("io.github.spair:imgui-java-natives-linux:1.86.11")
    api("io.github.spair:imgui-java-natives-macos:1.86.11")
    api("io.github.spair:imgui-java-natives-windows:1.86.11")

    implementation("com.badlogicgames.gdx:gdx-backend-lwjgl3:1.12.0")

    //implementation("com.badlogicgames.gdx:gdx:1.12.0")
    //implementation("com.badlogicgames.gdx:gdx-backend-lwjgl:1.12.0")
    implementation("com.badlogicgames.gdx:gdx-platform:1.12.0:natives-desktop")

    implementation("com.kotcrab.vis:vis-ui:$visuiVersion")
    implementation(kotlin("reflect"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")


    // ImGui
    //implementation(fileTree("libs") {include("*.jar")})
    //implementation(fileTree("libs/lwjgl") {include("*.jar")})
    // LWJGL
    /*implementation(platform("org.lwjgl:lwjgl-bom:$lwjglVersion"))
    implementation("org.lwjgl", "lwjgl")
    implementation("org.lwjgl", "lwjgl-glfw")
    implementation("org.lwjgl", "lwjgl-opengl")
    runtimeOnly("org.lwjgl", "lwjgl", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-glfw", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-opengl", classifier = lwjglNatives)*/
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