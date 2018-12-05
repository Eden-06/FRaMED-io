package io.framed.framework.view

import org.w3c.dom.events.KeyboardEvent

data class Shortcut(
        val letter: Char,
        val modifiers: Set<Modifier>
) {

    constructor(letter: Char, vararg modifiers: Modifier) : this(letter.toUpperCase(), modifiers.toSet())

    fun match(event: KeyboardEvent): Boolean {
        //println("${event.key.first().toUpperCase()} | ${event.ctrlKey} | ${event.altKey} | ${event.shiftKey}")
        //println("${letter} | ${Modifier.CTRL in modifiers} | ${Modifier.ALT in modifiers} | ${Modifier.SHIFT in modifiers}")
        return event.key.first().toUpperCase() == letter &&
                event.ctrlKey == Modifier.CTRL in modifiers &&
                event.altKey == Modifier.ALT in modifiers &&
                event.shiftKey == Modifier.SHIFT in modifiers
    }


    override fun toString(): String =
            (modifiers.sortedBy { it.priority }.map { it.name.toLowerCase().capitalize() } +
                    listOf(letter.toString()))
                    .joinToString("+")

    enum class Modifier(val priority: Int) {
        CTRL(1), ALT(2), SHIFT(3)
    }
}