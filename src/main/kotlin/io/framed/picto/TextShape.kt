package io.framed.picto

import io.framed.util.Property

/**
 * @author lars
 */
class TextShape(
        val property: Property<String>
) : Shape() {

}

fun BoxShape.textShape(property: Property<String>) = TextShape(property).also(this::add)

fun textShape(property: Property<String>, init: TextShape.() -> Unit = {}) = TextShape(property).also(init)