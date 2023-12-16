package elements.primitives

import api.math.max
import api.math.transformBy
import editor.Debug
import editor.Debuggable
import elements.PrimitiveObject
import utils.Transform4
import utils.Vector4
import kotlin.math.min
import kotlin.math.max

class Cube4(transform: Transform4) : PrimitiveObject(transform), Debuggable {

    override fun collider(pos: Vector4): Float {
        val q = pos.abs() - v2
        return max(q, Vector4()).length() + min(max(max(q.x, q.y), max(q.z, q.w)), 0f)
    }

    override fun simplifiedCollider(pos: Vector4): Float {
        val size = v2.length()
        return pos.toVector3().length() - size
    }

    override fun getShaderCall(v1: String, v2: String, extra: String): String {
        val a = "$v1.xyzw"
        val b = "$v2.xyzw"
        return shaderCall.replace("v1", a).replace("v2", b)
    }

    override fun debug(transform: Transform4) {
        val tmp = Transform4(v1, ro).transformBy(transform)
        Debug.drawHyperCube(tmp.location, v2, tmp.rotation)
    }

    init {
        setShader("primitives/sd4Box.glsl")
        setShaderCall("sd4Box(v1, v2)") // vec3 p, vec3 b
    }

}