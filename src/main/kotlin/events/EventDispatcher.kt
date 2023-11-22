package events

class EventDispatcher<T: Event> {

    private val listeners = mutableListOf<EventListener<out Event>>()

    fun addListener(listener: EventListener<out Event>) {
        listeners.add(listener)
    }

    fun removeListener(listener: EventListener<out Event>) {
        listeners.remove(listener)
    }

    fun dispatchEvent(event: Event) {
        for (listener in listeners) {
            listener.onEventReceived(event)
        }
    }
}
