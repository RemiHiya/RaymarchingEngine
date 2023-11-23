package events.events

import elements.Actor
import events.Event

data class ButtonActorClicked(val target: Actor): Event {
}