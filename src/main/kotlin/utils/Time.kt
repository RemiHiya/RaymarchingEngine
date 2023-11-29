package utils

class Time {

    companion object {
        val timeStarted: Float = System.nanoTime().toFloat()

        fun getTime() = (System.nanoTime().toFloat() - timeStarted) * 1E-9
    }

}