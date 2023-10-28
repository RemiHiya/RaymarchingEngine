package elements

import utils.Material
import utils.Vector3

data class SceneObject(
    val position: Vector3,
    val material: Material
)

data class Light(
    val position: Vector3,
    val intensity: Float
)