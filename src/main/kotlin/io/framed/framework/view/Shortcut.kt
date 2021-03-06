package io.framed.framework.view

import org.w3c.dom.events.KeyboardEvent

data class Shortcut(
        val letter: String,
        val modifiers: Set<Modifier>,
        val description: String? = null
) {

    constructor(letter: String, vararg modifiers: Modifier) : this(letter, modifiers.toSet())

    fun match(event: KeyboardEvent): Boolean {
        //println("'${event.key.uppercase()}' == '${letter.uppercase()}' -> ${event.key.uppercase() == letter.uppercase()}")
        //println("Ctrl: ${event.ctrlKey} == ${Modifier.CTRL in modifiers} -> ${event.ctrlKey == Modifier.CTRL in modifiers}")
        //println("Alt: ${event.altKey} == ${Modifier.ALT in modifiers} -> ${event.altKey == Modifier.ALT in modifiers}")
        //println("Shift: ${event.shiftKey} == ${Modifier.SHIFT in modifiers} -> ${event.shiftKey == Modifier.SHIFT in modifiers}")
        return event.key.uppercase() == letter.uppercase() &&
                event.ctrlKey == Modifier.CTRL in modifiers &&
                event.altKey == Modifier.ALT in modifiers &&
                event.shiftKey == Modifier.SHIFT in modifiers
    }

    private fun formatLetter(): String {
        val lower = letter.lowercase()

        return when (lower) {
            "+" -> "Plus"
            "-" -> "Minus"
            "arrowleft" -> "Left"
            "arrowright" -> "Right"
            "arrowup" -> "Up"
            "arrowdown" -> "Down"
            else -> lower.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
        }
    }

    fun description(description: String) = this.copy(description = description)

    override fun toString(): String =
            (modifiers.sortedBy { it.priority }.map { it.name.lowercase()
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() } } +
                    listOf(formatLetter()))
                    .joinToString("+")

    enum class Modifier(val priority: Int) {
        CTRL(1), ALT(2), SHIFT(3)
    }
}