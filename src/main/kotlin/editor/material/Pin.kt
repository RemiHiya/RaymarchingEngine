package editor.material

import utils.Color

data class Pin(val name: String, val type: PinType)

enum class PinType(val color: Color) {
    FLOAT(Color(255, 0, 0)),
    INT(Color(0, 255, 0)),
    VECTOR3(Color(199, 199, 41))
}


