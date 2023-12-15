package elements.primitives

import api.math.transformBy
import editor.Debug
import elements.PrimitiveObject
import utils.Transform4
import utils.Vector3
import utils.Vector4

class Sphere(transform: Transform4) : PrimitiveObject(transform) {

    override fun collider(pos: Vector4): Float {
        return pos.toVector3().length() - extra
    }

    override fun simplifiedCollider(pos: Vector4): Float {
        return collider(pos)
    }

    override fun getShaderCall(v1: String, v2: String, extra: String): String {
        return shaderCall.replace("v1", "$v1.xyz").replace("extra", extra)
    }

    override fun debug(transform: Transform4) {
        val tmp = Transform4(v1, ro).transformBy(transform)
        Debug.drawSphere(tmp.location.toVector3(), Vector3(extra, extra, extra), tmp.rotation.toRotator3())
    }

    init {
        setShader("primitives/sd3Sphere.glsl")
        setShaderCall("sd3Sphere(v1, extra)") // vec3 v1, float extra
    }
}