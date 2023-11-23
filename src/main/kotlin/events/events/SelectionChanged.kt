package events.events

import elements.Actor
import events.Event

class SelectionChanged(val new: Actor): Event {
}