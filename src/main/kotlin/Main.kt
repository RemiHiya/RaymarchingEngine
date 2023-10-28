import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration

fun main(args: Array<String>) {
    val config = LwjglApplicationConfiguration()
    config.width = 800
    config.height = 600
    val s = SceneTest()
    LwjglApplication(App(s), config)
}