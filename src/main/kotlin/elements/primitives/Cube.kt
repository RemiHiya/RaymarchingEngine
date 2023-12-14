package elements.primitives

import api.math.max
import api.math.transformBy
import editor.Debug
import elements.PrimitiveObject
import utils.Rotator3
import utils.Transform4
import utils.Vector3
import utils.Vector4
import kotlin.math.max
import kotlin.math.min

class Cube(transform: Transform4) : PrimitiveObject(transform) {

    override fun collider(pos: Vector4): Float {
        val q = pos.toVector3().abs() - v2.toVector3()
        return max(q, Vector3()).length() + min(max(q.x, max(q.y, q.z)), 0f)
    }

    override fun simplifiedCollider(pos: Vector4): Float {
        val size = v2.toVector3().length()
        return pos.toVector3().length() - size
    }

    override fun getShaderCall(v1: String, v2: String, extra: String): String {
        val a = "$v1.xyz"
        val b = "$v2.xyz"
        return shaderCall.replace("v1", a).replace("v2", b)
    }

    override fun debug(transform: Transform4) {
        val tmp = Transform4(v1, ro).transformBy(transform)
        Debug.drawCube(tmp.location.toVector3(), v2.toVector3(), tmp.rotation.toRotator3())
    }

    init {
        setShader("primitives/sd3Box.glsl")
        setShaderCall("sd3Box(v1, v2)") // vec3 p, vec3 b
    }
}