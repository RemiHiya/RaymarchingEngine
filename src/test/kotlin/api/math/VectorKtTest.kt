package api.math

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import utils.Rotator4
import utils.Vector4
import kotlin.math.roundToInt

class VectorKtTest {

    // Fonction permettant d'arrondir pour éviter les erreurs de précision des Float
    private fun Vector4.round(): Vector4 {
        val x = (x*1000f).roundToInt() / 1000f
        val y = (y*1000f).roundToInt() / 1000f
        val z = (z*1000f).roundToInt() / 1000f
        val w = (w*1000f).roundToInt() / 1000f
        return Vector4(x, y, z, w)
    }


    private fun rotateTest(rot: Rotator4, result: Vector4, vec: Vector4 = Vector4(1f, 0f, 0f, 0f)) {
        assertEquals(result, vec.rotate(rot).round())
    }

    @Test
    fun rotationsTest() {
        rotateTest(Rotator4(0f, 0f, 0f, 0f), Vector4(1f, 0f, 0f, 0f))

        rotateTest(Rotator4(0f, 0f, 90f, 0f), Vector4(0f, -1f, 0f, 0f))
        rotateTest(Rotator4(0f, 0f, -90f, 0f), Vector4(0f, 1f, 0f, 0f))

        rotateTest(Rotator4(0f, 0f, 0f, 90f), Vector4(0f, 0f, 0f, 1f))
        rotateTest(Rotator4(0f, 0f, 0f, -90f), Vector4(0f, 0f, 0f, -1f))
        rotateTest(Rotator4(0f, 0f, 0f, 180f), Vector4(-1f, 0f, 0f, 0f))
    }
}