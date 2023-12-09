package api.math

import utils.Transform4
import utils.Vector4


fun Transform4.transformBy(other: Transform4): Transform4 {

    val newLocation = other.location+location.rotate(other.rotation)
    val relativeRotation = other.rotation + rotation
    // TODO: Fix la rotation

    val relativeScale = Vector4(
        scale.x / other.scale.x,
        scale.y / other.scale.y,
        scale.z / other.scale.z,
        scale.w / other.scale.w
    )

    return Transform4(newLocation, relativeRotation, relativeScale)

}