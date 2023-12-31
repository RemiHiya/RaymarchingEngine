package utils

data class Color(val r: Int, val g: Int, val b: Int) {
    init {
        require(r in 0..255) { "Red component must be between 0 and 255." }
        require(g in 0..255) { "Green component must be between 0 and 255." }
        require(b in 0..255) { "Blue component must be between 0 and 255." }
    }

    operator fun times(other: Float) = Color(
        (r*other).toInt().coerceIn(0, 255),
        (g*other).toInt().coerceIn(0, 255),
        (b*other).toInt().coerceIn(0, 255)
    )

    operator fun plus(other: Int) = Color(
        (r+other).coerceIn(0, 255),
        (g+other).coerceIn(0, 255),
        (b+other).coerceIn(0, 255)
    )

    fun toInt(): Int {
        return (255 and 0xFF) shl 24 or ((b and 0xFF) shl 16) or ((g and 0xFF) shl 8) or (r and 0xFF)
    }
}