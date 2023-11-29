//import com.badlogic.gdx.backends.lwjgl.LwjglApplication
//import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import editor.Window

fun main(args: Array<String>) {
    //val config = Lwjgl3ApplicationConfiguration()
    //val s = SceneTest()
    //Lwjgl3Application(App(s), config)
    val window = Window.get()
    window?.run()
}