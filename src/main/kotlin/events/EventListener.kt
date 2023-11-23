package events

interface EventListener<out T : Event> {

    fun onEventReceived(event: @UnsafeVariance T)
}