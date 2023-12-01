import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import editor.Window

fun main(args: Array<String>) {
    val config = Lwjgl3ApplicationConfiguration()//Lwjgl3ApplicationConfiguration()
    val s = SceneTest()
    Lwjgl3Application(App(s), config)
    //val window = Window.get()
    //window?.run()
}