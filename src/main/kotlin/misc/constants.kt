package misc

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.ui.Skin

const val PATH = "./src/main/kotlin/"
const val RESOURCE_PATH = "./src/main/resources/"
const val MAX_OBJECTS = 128
const val SKIN_PATH = RESOURCE_PATH + "skins/metalui/metal-ui.json"
val SKIN =  Skin(Gdx.files.internal(SKIN_PATH))