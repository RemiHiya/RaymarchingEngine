package events

interface EventListener<T: Event> {

    fun <T: Event> onEventReceived(event: T)
}