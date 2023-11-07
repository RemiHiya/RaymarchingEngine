package elements

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import misc.MAX_OBJECTS
import misc.PATH

class SceneParser(private val scene : Scene) {

    /*
    Cette classe sert à convertir la scène en GLSL
    Récupère chaque objet référencé, permet ensuite d'être envoyé au shader
     */

    private var shaders: Array<String> = arrayOf()
    private var shadersMap: Map<String, Int> = mapOf()
    private var shaderCalls: Map<Int, String> = mapOf()

    fun initialize() = "#version 330 core\n" +
            "#define MAX_OBJECTS ${MAX_OBJECTS}\n" +
            "uniform int SCENE_SIZE; \n" +
            Gdx.files.internal(PATH + "shaders/header.glsl").readString() +
            Gdx.files.internal(PATH + "shaders/operators.glsl").readString()

    /*
    TODO : Chercher récursivement les références de chaque shader qui va être utilisé
     */

    private fun getShaders() {
        var index = 0
        for (i in scene.getObjects()) {
            if (i != null) {
                val s = "shaders/" + i.getShader()
                if (s !in shaders) {

                    shaders += "shaders/" + i.getShader()
                    shaderCalls += Pair(
                        index,
                        i.getShaderCall("objects[i].v1", "objects[i].v2", "objects[i].extra")
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
    }

    fun computeScene(): String {
        getShaders()
        var out = ""
        for (i in shaders) {
            out += Gdx.files.internal(PATH + i).readString()
        }
        return out
    }

    fun computeMaterials(): String {
        var out = ""
        for (i in 0 until scene.getObjects().size) {
            val a = scene.getObjects()[i]
            if (a != null) {
                out += "vec3 material$i(){return ${a.getMaterial()};}"
            }

        }
        return out
    }

    fun computeMapper(): String {
        var out = "marcher map(vec4 ro) { \n" +
                "float m=10000;" +
                "vec3 color; vec3 c;"

        // Loop sur tout les objets
        out += "for(int i=0; i<SCENE_SIZE; i++){ \n"
        out += "    float d;\n"

        // Switch (tout les else if) en fonction du type
        for (i in shaderCalls.keys) {
            val switcher = if(i==0) "if" else "else if"
            out += "    $switcher(objects[i].shader == $i) { \n" +
                    "       d = ${shaderCalls[i]}; \n" +
                    "       c = material$i();" +
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
    private fun getMaterial(obj: PrimitiveObject) = 0

    fun updateShaderObjects(sp: ShaderProgram?) {
        var index = 0
        for (i in scene.getObjects()) {
            if (i != null) {
                val v1 = floatArrayOf(i.v1.x, i.v1.y, i.v1.z, i.v1.w)
                val v2 = floatArrayOf(i.v2.x, i.v2.y, i.v2.z, i.v2.w)
                sp?.setUniform4fv("objects[$index].v1", v1, 0, 4)
                sp?.setUniform4fv("objects[$index].v2", v2, 0, 4)
                sp?.setUniformf("objects[$index].extra", i.extra)
                sp?.setUniformi("objects[$index].shader", getShader(i))
                sp?.setUniformi("objects[$index].material", getMaterial(i))
                sp?.setUniformi("objects[$index].operator", i.operator.operator.value)
                sp?.setUniformf("objects[$index].smoothness", i.operator.smoothness)
                index ++
            }
        }
    }
}