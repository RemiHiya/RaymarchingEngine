//import com.badlogic.gdx.backends.lwjgl.LwjglApplication
//import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration

fun main(args: Array<String>) {
    val config = Lwjgl3ApplicationConfiguration()
    //config.setWindowSizeLimits()
    //config.width = 800
    //config.height = 600
    val s = SceneTest()
    Lwjgl3Application(App(s), config)
}