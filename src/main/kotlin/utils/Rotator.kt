package utils

import kotlin.math.PI

data class Rotator3(var roll: Float, var pitch: Float, var yaw: Float) {
    fun toRadians() = Rotator3(
        roll*PI.toFloat()/180,
        pitch*PI.toFloat()/180,
        yaw*PI.toFloat()/180)

    operator fun rem(mod: Float) = Rotator3(roll%mod, pitch%mod, yaw%mod)
}

data class Rotator4(var roll: Float, var pitch: Float, var yaw: Float, var w: Float) {
    fun toRadians() = Rotator4(
        roll*PI.toFloat()/180,
        pitch*PI.toFloat()/180,
        yaw*PI.toFloat()/180,
        w*PI.toFloat()/180)

    fun toRotator3() = Rotator3(roll, pitch, yaw)
    operator fun rem(mod: Float) = Rotator4(roll%mod, pitch%mod, yaw%mod, w%mod)
}