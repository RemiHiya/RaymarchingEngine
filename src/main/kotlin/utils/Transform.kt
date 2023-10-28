package utils

class Transform(
    private var location: Vector3 = Vector3(0f, 0f, 0f),
    var rotation: Rotator = Rotator(0f, 0f, 0f),
    var scale: Vector3 = Vector3(1f, 1f, 1f)) {

    fun getLocation() = location


}