package elements

import  com.badlogic.gdx.Gdx
import misc.PATH
import utils.MarcherOperator
import utils.Rotator4
import utils.Vector4

abstract class Primitive {
    private var material: String = "vec3(.3)"
    lateinit var v1: Vector4
    lateinit var v2: Vector4
    lateinit var ro: Rotator4
    var extra: Float = 0.0f

    var index = 0
    lateinit var operator: MarcherOperator

    private lateinit var shader: String
    lateinit var shaderCall: String

    fun getShader() = shader
    fun getMaterial() = material

    /**
     * Calcule la distance signée entre la position spécifiée [pos] et l'objet.
     * Le collider est calculé dans un espace local.
     * @param pos La position locale pour laquelle calculer la distance.
     * @return La distance signée entre [pos] et l'objet.
     */
    abstract fun collider(pos: Vector4): Float

    /**
     * Calcule une version simplifiée de la distance signée entre
     * la position spécifiée [pos] et l'objet.
     * Le collider est calculé dans un espace local.
     * @param pos La position locale pour laquelle calculer la distance.
     * @return La distance signée entre [pos] et l'objet.
     */
    abstract fun simplifiedCollider(pos: Vector4): Float

    abstract fun getShaderCall(v1: String, v2: String, extra: String): String

    fun setShader(new : String): String {
        shader = new
        return new
    }

    fun setShaderCall(new: String): String {
        shaderCall = new
        return new
    }

    fun setMaterial(new: String, isFile: Boolean = false): String {
        material = if (isFile) {
            Gdx.files.internal(PATH + new).readString()
        } else {
            new
        }
        return material
    }

    fun setPrimitiveParameters(v2: Vector4, extra: Float) {
        this.v2 = v2
        this.extra = extra
    }

    fun setObjectIndex(new: Int) {
        index = new
    }

    fun setObjectOperator(new: MarcherOperator) {
        operator = new
    }
}