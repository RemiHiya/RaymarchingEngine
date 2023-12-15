package editor

import api.math.rotation
import api.math.times
import utils.Rotator3
import utils.Rotator4
import utils.Vector3
import utils.Vector4
import kotlin.math.sqrt

abstract class DebugShapes {
    companion object {

        fun draw4D(shape: Pair<List<Vector4>, List<Pair<Int, Int>>>,
                   center: Vector4,
                   bounds: Vector4, rotation: Rotator4,
                   drawCall: (Vector4, Vector4) -> Unit) {
            for ((index1, index2) in shape.second) {
                var pos1 = (shape.first[index1] * bounds)
                var pos2 = (shape.first[index2] * bounds)
                pos1 = rotation(rotation.toRadians()) * pos1 + center
                pos2 = rotation(rotation.toRadians()) * pos2 + center
                drawCall(pos1, pos2)
            }
        }

        fun draw3D(shape: Pair<List<Vector3>, List<Pair<Int, Int>>>,
                   center: Vector3,
                   bounds: Vector3, rotation: Rotator3,
                   drawCall: (Vector3, Vector3) -> Unit) {
            for ((index1, index2) in shape.second) {
                var pos1 = (shape.first[index1] * bounds).toVector4()
                var pos2 = (shape.first[index2] * bounds).toVector4()
                pos1 = rotation(rotation.toRotator4().toRadians()) * pos1 + center.toVector4()
                pos2 = rotation(rotation.toRotator4().toRadians()) * pos2 + center.toVector4()
                drawCall(pos1.toVector3(), pos2.toVector3())
            }
        }


        val hyperCube: Pair<List<Vector4>, List<Pair<Int, Int>>> = Pair(
            listOf(Vector4(-1f, -1f, -1f, 1f), Vector4(1f, -1f, -1f, 1f),
                Vector4(-1f, 1f, -1f, 1f), Vector4(1f, 1f, -1f, 1f),
                Vector4(-1f, -1f, 1f, 1f), Vector4(1f, -1f, 1f, 1f),
                Vector4(-1f, 1f, 1f, 1f), Vector4(1f, 1f, 1f, 1f),
                Vector4(-1f, -1f, -1f, -1f), Vector4(1f, -1f, -1f, -1f),
                Vector4(-1f, 1f, -1f, -1f), Vector4(1f, 1f, -1f, -1f),
                Vector4(-1f, -1f, 1f, -1f), Vector4(1f, -1f, 1f, -1f),
                Vector4(-1f, 1f, 1f, -1f), Vector4(1f, 1f, 1f, -1f)
            ),
            listOf(Pair(0, 1), Pair(0, 2), Pair(1, 3), Pair(2, 3),
                Pair(4, 5), Pair(4, 6), Pair(5, 7), Pair(6, 7),
                Pair(0, 4), Pair(1, 5), Pair(2, 6), Pair(3, 7),
                Pair(0, 8), Pair(1, 9), Pair(2, 10), Pair(3, 11),
                Pair(4, 12), Pair(5, 13), Pair(6, 14), Pair(7, 15),
                Pair(8, 9), Pair(8, 10), Pair(9, 11), Pair(10, 11),
                Pair(12, 13), Pair(12, 14), Pair(13, 15), Pair(14, 15),
                Pair(8, 12), Pair(9, 13), Pair(10, 14), Pair(11, 15)
            ))

        val cube: Pair<List<Vector3>, List<Pair<Int, Int>>> = Pair(
            listOf(
                Vector3(-1f, -1f, -1f), Vector3(1f, -1f, -1f),
                Vector3(-1f, 1f, -1f), Vector3(1f, 1f, -1f),
                Vector3(-1f, -1f, 1f), Vector3(1f, -1f, 1f),
                Vector3(-1f, 1f, 1f), Vector3(1f, 1f, 1f)
            ),
            listOf(Pair(0, 1), Pair(0, 2), Pair(1, 3), Pair(2, 3),
                Pair(4, 5), Pair(4, 6), Pair(5, 7), Pair(6, 7),
                Pair(0, 4), Pair(1, 5), Pair(2, 6), Pair(3, 7)
            ))

        val sphere = icosphere(1)

        val hyperSphere = hyperIcoSphere()

        private fun hyperIcoSphere(): Pair<List<Vector4>, List<Pair<Int, Int>>> {
            val (vertices, indices) = sphere
            val newVertices = vertices.map { Vector4(it.x, it.y, it.z, 1f) }.toMutableList()
            newVertices += vertices.map { Vector4(it.x, it.y, it.z, -1f) }
            val newIndices = mutableListOf<Pair<Int, Int>>()

            // Ajouter les indices de l'icosaèdre avec w=1
            newIndices.addAll(indices)

            // Ajouter les indices de la nouvelle icosaèdre avec w=-1
            val offset = vertices.size
            indices.forEach { (i, j) ->
                newIndices.add(Pair(i + offset, j + offset))
            }
            return Pair(newVertices, newIndices)
        }


        private fun subdivide(v1: Vector3, v2: Vector3, v3: Vector3, level: Int, vertices: MutableList<Vector3>, edges: MutableList<Pair<Int, Int>>) {
            if (level == 0) {
                val i1 = vertices.size
                val i2 = i1 + 1
                val i3 = i2 + 1

                val mid1 = (v1 + v2) / 2f
                val mid2 = (v2 + v3) / 2f
                val mid3 = (v3 + v1) / 2f

                vertices.addAll(listOf(mid1.normalize(), mid2.normalize(), mid3.normalize()))

                edges.addAll(listOf(Pair(i1, i2), Pair(i2, i3), Pair(i3, i1)))
            } else {
                val mid1 = (v1 + v2) / 2f
                val mid2 = (v2 + v3) / 2f
                val mid3 = (v3 + v1) / 2f

                subdivide(v1, mid1, mid3, level - 1, vertices, edges)
                subdivide(mid1, v2, mid2, level - 1, vertices, edges)
                subdivide(mid1, mid2, mid3, level - 1, vertices, edges)
                subdivide(mid3, mid2, v3, level - 1, vertices, edges)
            }
        }

        private fun icosphere(n: Int): Pair<List<Vector3>, List<Pair<Int, Int>>> {
            val vertices = mutableListOf<Vector3>()
            val edges = mutableListOf<Pair<Int, Int>>()

            val t = (1.0 + sqrt(5.0)) / 2.0

            val icosahedronVertices = listOf(
                Vector3(-1f, t.toFloat(), 0f),
                Vector3(1f, t.toFloat(), 0f),
                Vector3(-1f, -t.toFloat(), 0f),
                Vector3(1f, -t.toFloat(), 0f),
                Vector3(0f, -1f, t.toFloat()),
                Vector3(0f, 1f, t.toFloat()),
                Vector3(0f, -1f, -t.toFloat()),
                Vector3(0f, 1f, -t.toFloat()),
                Vector3(t.toFloat(), 0f, -1f),
                Vector3(t.toFloat(), 0f, 1f),
                Vector3(-t.toFloat(), 0f, -1f),
                Vector3(-t.toFloat(), 0f, 1f)
            )

            val icosahedronFaces = listOf(
                Triple(0, 11, 5),
                Triple(0, 5, 1),
                Triple(0, 1, 7),
                Triple(0, 7, 10),
                Triple(0, 10, 11),
                Triple(1, 5, 9),
                Triple(5, 11, 4),
                Triple(11, 10, 2),
                Triple(10, 7, 6),
                Triple(7, 1, 8),
                Triple(3, 9, 4),
                Triple(3, 4, 2),
                Triple(3, 2, 6),
                Triple(3, 6, 8),
                Triple(3, 8, 9),
                Triple(4, 9, 5),
                Triple(2, 4, 11),
                Triple(6, 2, 10),
                Triple(8, 6, 7),
                Triple(9, 8, 1)
            )

            for (face in icosahedronFaces) {
                subdivide(
                    icosahedronVertices[face.first],
                    icosahedronVertices[face.second],
                    icosahedronVertices[face.third],
                    n,
                    vertices,
                    edges
                )
            }

            return Pair(vertices, edges)
        }



    }

}