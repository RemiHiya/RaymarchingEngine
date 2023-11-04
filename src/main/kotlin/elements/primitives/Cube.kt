package elements.primitives

import elements.Primitive
import elements.PrimitiveObject
import utils.Transform4
import utils.Vector3
import utils.Vector4

class Cube(transform: Transform4, material: String = "") : PrimitiveObject(transform, material) {

    /*
    override fun getShaderCall(v1: Vector4, v2: Vector4, extra: Float): String {
        val ve1 = v1.x.toString() + "," + v1.y.toString() + "," + v1.z.toString()
        val ve2 = v2.x.toString() + "," + v2.y.toString() + "," + v2.z.toString()
        return shaderCall.replace("v1", "vec3($ve1)").replace("v2", "vec3($ve2)")
    }*/

    override fun getShaderCall(v1: String, v2: String, extra: String): String {
        val a = "$v1.xyz"
        val b = "$v2.xyz"
        return shaderCall.replace("v1", a).replace("v2", b)
    }

    init {
        setShader("primitives/sd3Box.glsl")
        setShaderCall("sd3Box(v1, v2, ro)") // vec3 p, vec3 b
    }



}