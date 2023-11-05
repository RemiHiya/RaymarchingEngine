package utils

data class MarcherOperator(var operator: OperatorType, var smoothness: Float)

data class MarcherObjetct(var index: Int, var operator: MarcherOperator)