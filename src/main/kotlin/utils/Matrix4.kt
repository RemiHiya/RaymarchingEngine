package utils

class Matrix4(vararg elements: Float) {

    val matrix: Array<FloatArray>

    init {
        require(elements.size == 16) { "Matrix4 must be initialized with 16 elements." }
        matrix = Array(4) { i -> FloatArray(4) { j -> elements[i * 4 + j] } }
    }


    override fun toString(): String {
        val builder = StringBuilder()
        for (i in 0..3) {
            for (j in 0..3) {
                builder.append(matrix[i][j]).append(" ")
            }
            builder.append("\n")
        }
        return builder.toString()
    }
}