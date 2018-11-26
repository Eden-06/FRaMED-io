package io.framed.framework.pictogram

import de.westermann.kobserve.Property
import io.framed.framework.Linker

/**
 * @author lars
 */
class TextShape(
        val property: Property<String>,
        val autocomplete: List<String>,
        override val id: Long?
) : Shape() {

}

fun BoxShape.textShape(
        property: Property<String>,
        autocomplete: List<String> = emptyList(),
        init: TextShape.() -> Unit = {}
) = TextShape(property, autocomplete, null).also(init).also(this::add)

fun Linker<*, *>.textShape(
        property: Property<String>,
        autocomplete: List<String> = emptyList(),
        init: TextShape.() -> Unit = {}
) = TextShape(property, autocomplete, id).also(init)