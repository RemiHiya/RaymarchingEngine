package utils

class Time {

    companion object {
        var time = 0f
        val timeStarted: Float = System.nanoTime().toFloat()
        fun getTime() = (System.nanoTime().toFloat() - timeStarted) * 1E-9

    }

}