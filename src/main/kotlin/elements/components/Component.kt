package elements.components

import editor.EditorElement
import elements.Actor

abstract class Component: EditorElement {

    lateinit var parent: Actor
    abstract var displayName: String

}