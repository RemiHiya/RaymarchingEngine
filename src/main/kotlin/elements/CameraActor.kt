package elements

class CameraActor: Actor() {

    var fov = 110f
    var nearClip = 0.1f
    var farClip = 100f

    override fun construct() {
        super.construct()
        displayName = "Camera"
    }
}