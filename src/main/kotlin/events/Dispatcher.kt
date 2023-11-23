package events

interface Dispatcher {
    fun getDispatcher(): EventDispatcher
}