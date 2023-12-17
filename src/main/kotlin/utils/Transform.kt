package utils

data class Transform3(
    private var location: Vector3 = Vector3(0f, 0f, 0f),
    var rotation: Rotator3 = Rotator3(0f, 0f, 0f),
    var scale: Vector3 = Vector3(1f, 1f, 1f)) {
}

data class Transform4(
    var location: Vector4 = Vector4(0f, 0f, 0f, 0f),
    var rotation: Rotator4 = Rotator4(0f, 0f, 0f, 0f),
    var scale: Vector4 = Vector4(1f, 1f, 1f, 1f)) {
}