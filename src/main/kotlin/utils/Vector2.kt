package utils

import kotlin.math.sqrt

data class Vector2(val x: Float=0f, val y: Float=0f) {
    operator fun plus(other: Vector2) = Vector2(x + other.x, y + other.y)
    operator fun minus(other: Vector2) = Vector2(x - other.x, y - other.y)
    operator fun times(scalar: Float) = Vector2(x * scalar, y * scalar)
    operator fun times(other: Vector2) = Vector2(x * other.x, y * other.y)
    operator fun div(scalar: Float) = Vector2(x / scalar, y / scalar)

    fun length() = sqrt(x * x + y * y)
    fun normalize() = this / length()
    fun dot(other: Vector2) = x * other.x + y * other.y
    fun abs() = Vector2(kotlin.math.abs(x), kotlin.math.abs(y))

    fun toVector3() = Vector3(x, y, 0f)

    override fun toString(): String {
        return "vec2($x, $y)"
    }

}