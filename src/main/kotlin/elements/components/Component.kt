package elements.components

import editor.EditorElement
import elements.Actor

sealed class Component: EditorElement {

    lateinit var parent: Actor
    abstract var displayName: String

}