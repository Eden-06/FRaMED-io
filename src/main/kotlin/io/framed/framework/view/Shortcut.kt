package io.framed.framework.view

import org.w3c.dom.events.KeyboardEvent

data class Shortcut(
        val letter: String,
        val modifiers: Set<Modifier>
) {

    constructor(letter: String, vararg modifiers: Modifier) : this(letter, modifiers.toSet())

    fun match(event: KeyboardEvent): Boolean {
        //println("'${event.key.toUpperCase()}' == '${letter.toUpperCase()}' -> ${event.key.toUpperCase() == letter.toUpperCase()}")
        //println("Ctrl: ${event.ctrlKey} == ${Modifier.CTRL in modifiers} -> ${event.ctrlKey == Modifier.CTRL in modifiers}")
        //println("Alt: ${event.altKey} == ${Modifier.ALT in modifiers} -> ${event.altKey == Modifier.ALT in modifiers}")
        //println("Shift: ${event.shiftKey} == ${Modifier.SHIFT in modifiers} -> ${event.shiftKey == Modifier.SHIFT in modifiers}")
        return  event.key.toUpperCase() == letter.toUpperCase() &&
                event.ctrlKey == Modifier.CTRL in modifiers &&
                event.altKey == Modifier.ALT in modifiers &&
                event.shiftKey == Modifier.SHIFT in modifiers
    }

    override fun toString(): String =
            (modifiers.sortedBy { it.priority }.map { it.name.toLowerCase().capitalize() } +
                    listOf(letter.toLowerCase().capitalize()))
                    .joinToString("+")

    enum class Modifier(val priority: Int) {
        CTRL(1), ALT(2), SHIFT(3)
    }
}