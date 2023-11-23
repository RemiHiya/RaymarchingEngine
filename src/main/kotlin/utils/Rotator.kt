package utils

import kotlin.math.PI

data class Rotator3(var roll: Float, var pitch: Float, var yaw: Float) {
    fun toRadians() = Rotator3(
        roll*PI.toFloat()/180,
        pitch*PI.toFloat()/180,
        yaw*PI.toFloat()/180)
}

data class Rotator4(var roll: Float, var pitch: Float, var yaw: Float, var w: Float) {
    fun toRadian() = Rotator4(roll*PI.toFloat()/180,
        pitch*PI.toFloat()/180,
        yaw*PI.toFloat()/180,
        w*PI.toFloat()/180)
}