package elements

import com.badlogic.gdx.Gdx
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
            "#define MAX_OBJECTS ${scene.getObjects().size}\n" +
            "struct obj {\n" +
            "    vec4 v1;\n" +
            "    vec4 v2;\n" +
            "    float extra;\n" +
            "    int shader;\n" +
            "    int material;\n" +
            "}; obj objects[MAX_OBJECTS]; \n"

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

    fun computeMapper(): String {
        var out = "float map(vec3 ro) { \n" +
                "float m=10000;"

        // Loop sur tout les objets
        out += "for(int i=0; i<objects.length(); i++){ \n"
        out += "    float d;\n"
        //out += "    vec4 v1 = objects[i].v1; vec4 v2 = objects[i].v2; float extra = objects[i].extra; \n"

        // Switch (tout les else if) en fonction du type
        for (i in shaderCalls.keys) {
            val switcher = if(i==0) "if" else "else if"
            out += "    $switcher(objects[i].shader == $i) { \n" +
                    "       d = ${shaderCalls[i]}; \n" +
                    "   } \n"
        }
        //out += "if(i==0){m=d;}"
        out += "m = min(d, m);"

        out += "}"

        out += "return m;}"
        return out
    }

    fun computeObjects(): String {
        var out = ""
        var index = 0
        for (i in scene.getObjects()) {
            if (i != null) {
                val t = i.getTransform().getLocation()
                out += "objects[$index] = obj(" +
                        "vec4(${t.x}, ${t.y}, ${t.z}, 0)," +
                        "vec4(4, 4, 4, 1)," +
                        "1," +
                        shaders.indexOf("shaders/" + i.getShader()) +"," +
                        "1" +
                        ");"

                index ++
            }
        }

        return out
    }
}