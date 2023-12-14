package editor

import utils.Transform4

interface Debuggable {
    fun debug() = Unit
    fun debug(transform: Transform4) = Unit
}