package io.framed.framework.pictogram

import de.westermann.kobserve.ReadOnlyProperty
import io.framed.framework.linker.Linker

/**
 * @author lars
 */
class TextShape(
        val property: ReadOnlyProperty<String>,
        val autocomplete: List<String>,
        id: Long?,
        val alignment: TextAlignment
) : Shape(id) {
        enum class TextAlignment {
                LEFT,
                RIGHT,
                CENTER
        }
}

fun BoxShape.textShape(
        property: ReadOnlyProperty<String>,
        autocomplete: List<String> = emptyList(),
        init: TextShape.() -> Unit = {},
        alignment: TextShape.TextAlignment = TextShape.TextAlignment.LEFT
) = TextShape(property, autocomplete, null, alignment).also(init).also(this::add)

fun Linker<*, *>.textShape(
        property: ReadOnlyProperty<String>,
        autocomplete: List<String> = emptyList(),
        init: TextShape.() -> Unit = {},
        alignment: TextShape.TextAlignment = TextShape.TextAlignment.LEFT
) = TextShape(property, autocomplete, id, alignment).also(init)