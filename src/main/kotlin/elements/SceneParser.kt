package elements

import api.getPrimitives
import api.math.transformBy
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import misc.MAX_OBJECTS
import misc.PATH
import utils.Transform4
import utils.Vector4
import java.io.File
import kotlin.math.PI

fun readFile(path: String) = File(path).readText()

class SceneParser(private val scene : Scene) {

    /*
    Cette classe sert à convertir la scène en GLSL
    Récupère chaque objet référencé, permet ensuite d'être envoyé au shader
     */

    private var shaders: Array<String> = arrayOf()
    private var shadersMap: Map<String, Int> = mapOf()
    private var shaderCalls: Map<Int, String> = mapOf()
    private var materialCalls: Array<String> = arrayOf()
    private var materialMap: Map<Int, Int> = mapOf()

    private var objects: Array<PrimitiveObject> = arrayOf()

    fun initialize() = "#version 330 core\n" +
            "#define MAX_OBJECTS ${MAX_OBJECTS}\n" +
            "uniform int SCENE_SIZE; \n" +
            readFile(PATH + "shaders/header.glsl") +
            readFile(PATH + "shaders/operators.glsl")

    /*
    TODO : Chercher récursivement les références de chaque shader qui va être utilisé
     */

    fun rebuildObjects() {
        objects = arrayOf()
        for (i in scene.actors) {
            for (j in i.getPrimitives()) {
                val relative = Transform4(j.v1, j.ro, Vector4(1f,1f,1f,1f))
                val new = relative.transformBy(i.transform)
                val clone = j::class.java.constructors[0].newInstance(new) as PrimitiveObject

                clone.v2 = j.v2
                clone.setShader(j.getShader())
                clone.setMaterial(j.getMaterial())
                clone.extra = j.extra
                clone.operator = j.operator

                objects += clone
            }
        }
    }

    private fun getShaders() {
        var index = 0
        for (i in objects) {
            val s = "shaders/" + i.getShader()
            if (s !in shaders) {

                shaders += "shaders/" + i.getShader()
                shaderCalls += Pair(
                    index,
                    i.getShaderCall("p", "objects[i].v2", "objects[i].extra")
                )

                // Update la map type -> index
                val t = i::class.qualifiedName
                if (t != null) {
                    shadersMap += Pair(t, index)
                }
                index ++
            }
        }
    }

    fun computeScene(): String {
        getShaders()
        var out = ""
        for (i in shaders) {
            out += readFile(PATH + i)
        }
        return out
    }


    //Ajoute les materials au shader (évite les répétitions)
    fun computeMaterials(): String {
        var out = ""
        var index = 0
        for (i in 0 until objects.size) {
            val obj = objects[i].getMaterial()
            if (obj !in materialCalls) {
                materialCalls += obj
                out += "vec3 material$index(){return ${obj};}"
                index ++
            }
            materialMap += Pair(i, index)
        }
        return out
    }

    fun computeMapper(): String {
        var out = "marcher map(vec4 ro) { \n" +
                "float m=10000;" +
                "vec3 color; vec3 c;"

        // Loop sur tout les objets
        out += "for(int i=0; i<SCENE_SIZE; i++){ \n"
        out += "    float d;\n" +
                "vec4 p = rot(objects[i].v1 - ro, objects[i].rot);"

        // Loop des materials
        /*
        TODO : optimiser cette merde
         */
        val tmp: MutableList<Int> = mutableListOf()
        for (i in materialMap) {
            if (i.value-1 !in tmp){
                val switcher = if(i.key==0) "if" else "else if"
                out += "$switcher(objects[i].material == ${i.value-1}) {c = material${i.value-1}();}\n"
                tmp += i.value-1
            }

        }

        // Switch (tout les else if) en fonction du type
        for (i in shaderCalls.keys) {
            val switcher = if(i==0) "if" else "else if"
            out += "    $switcher(objects[i].shader == $i) { \n" +
                    "       d = ${shaderCalls[i]}; \n" +
                    "   } \n"
        }
        // Gestion des materials
        out += "color = colorOp(objects[i].operator, marcher(m,color), marcher(d,c), objects[i].smoothness);"

        // Opérations d'union/sub/inter
        out += "m = op(d, m, objects[i].operator, objects[i].smoothness);"

        out += "}"
        out += "return marcher(m, color);}"
        return out
    }

    private fun getShader(obj: PrimitiveObject) = shaders.indexOf("shaders/" + obj.getShader())
    private fun getMaterial(obj: PrimitiveObject) = materialCalls.indexOf(obj.getMaterial())

    fun updateShaderObjects(sp: ShaderProgram) {
        rebuildObjects()
        for ((index, i) in objects.withIndex()) {
            val v1 = floatArrayOf(i.v1.x, i.v1.y, i.v1.z, i.v1.w)
            val v2 = floatArrayOf(i.v2.x, i.v2.y, i.v2.z, i.v2.w)
            val ro = floatArrayOf(i.ro.roll.toRad(), i.ro.pitch.toRad(), i.ro.yaw.toRad(), i.ro.w.toRad())
            sp.setUniform4fv("objects[$index].v1", v1, 0, 4)
            sp.setUniform4fv("objects[$index].v2", v2, 0, 4)
            sp.setUniform4fv("objects[$index].rot", ro, 0, 4)
            sp.setUniformf("objects[$index].extra", i.extra)
            sp.setUniformi("objects[$index].shader", getShader(i))
            sp.setUniformi("objects[$index].material", getMaterial(i))
            sp.setUniformi("objects[$index].operator", i.operator.operator.value)
            sp.setUniformf("objects[$index].smoothness", i.operator.smoothness)
        }
    }


    private fun Float.toRad() = this * PI.toFloat() /180
}