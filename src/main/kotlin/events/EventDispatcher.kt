package events


class EventDispatcher {

    private val listeners = mutableListOf<EventListener<Event>>()

    fun addListener(listener: EventListener<Event>) {
        listeners.add(listener)
    }

    fun removeListener(listener: EventListener<Event>) {
        listeners.remove(listener)
    }

    fun dispatchEvent(event: Event) {
        for (listener in listeners) {
            listener.onEventReceived(event)
        }
    }
}
