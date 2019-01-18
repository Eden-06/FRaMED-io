package io.framed.framework.pictogram

import de.westermann.kobserve.ReadOnlyProperty
import io.framed.framework.Linker

/**
 * @author lars
 */
class TextShape(
        val property: ReadOnlyProperty<String>,
        val autocomplete: List<String>,
        id: Long?
) : Shape(id) {

}

fun BoxShape.textShape(
        property: ReadOnlyProperty<String>,
        autocomplete: List<String> = emptyList(),
        init: TextShape.() -> Unit = {}
) = TextShape(property, autocomplete, null).also(init).also(this::add)

fun Linker<*, *>.textShape(
        property: ReadOnlyProperty<String>,
        autocomplete: List<String> = emptyList(),
        init: TextShape.() -> Unit = {}
) = TextShape(property, autocomplete, id).also(init)