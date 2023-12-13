package utils

import kotlin.math.abs

class Matrix4(vararg elements: Float) {

    val matrix: Array<FloatArray>

    init {
        require(elements.size == 16) { "Matrix4 must be initialized with 16 elements." }
        matrix = Array(4) { i -> FloatArray(4) { j -> elements[i * 4 + j] } }
    }


    /**
     * Calcule et retourne la matrice inverse de la matrice courante en utilisant la méthode
     * d'élimination de Gauss-Jordan.
     * @return La matrice inverse, si elle existe.
     */
    fun inverse(): Matrix4 {
        val augmentedMatrix = Array(4) { i ->
            FloatArray(8) { j ->
                if (j < 4) matrix[i][j] else if (i == j - 4) 1.0f else 0.0f
            }
        }

        for (i in 0 until 4) {
            var pivotRow = i
            for (j in i + 1 until 4) {
                if (abs(augmentedMatrix[j][i]) > abs(augmentedMatrix[pivotRow][i])) {
                    pivotRow = j
                }
            }

            val temp = augmentedMatrix[i]
            augmentedMatrix[i] = augmentedMatrix[pivotRow]
            augmentedMatrix[pivotRow] = temp

            val pivotElement = augmentedMatrix[i][i]
            for (j in i until 8) {
                augmentedMatrix[i][j] /= pivotElement
            }

            for (j in 0 until 4) {
                if (j != i) {
                    val factor = augmentedMatrix[j][i]
                    for (k in i until 8) {
                        augmentedMatrix[j][k] -= factor * augmentedMatrix[i][k]
                    }
                }
            }
        }

        val inverseMatrix = Array(4) { i ->
            FloatArray(4) { j ->
                augmentedMatrix[i][j + 4]
            }
        }

        return Matrix4(*inverseMatrix.flatMap { it.asIterable() }.toFloatArray())
    }
}