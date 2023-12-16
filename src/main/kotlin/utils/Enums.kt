package utils

enum class OperatorType(val value: Int) {
    UNION(0),
    SUBTRACTION(1),
    INTERSECTION(2)
}

enum class ColliderType {
    STATIC,
    DYNAMIC,
    KINEMATIC
}