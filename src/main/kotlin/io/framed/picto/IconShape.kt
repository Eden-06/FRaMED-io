package io.framed.picto

import io.framed.util.Property

/**
 * @author lars
 */
class TextShape(
        val property: Property<String>,
        val autocomplete: List<String>
) : Shape() {

}

fun BoxShape.textShape(
        property: Property<String>,
        autocomplete: List<String> = emptyList()
) = TextShape(property, autocomplete).also(this::add)

fun textShape(
        property: Property<String>,
        autocomplete: List<String> = emptyList(),
        init: TextShape.() -> Unit = {}
) = TextShape(property, autocomplete).also(init)