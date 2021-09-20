package io.framed.framework.pictogram

import de.westermann.kobserve.ReadOnlyProperty
import io.framed.framework.linker.Linker
import io.framed.framework.view.Surround

/**
 * ViewModel class that represents a text field.
 *
 * @author Lars Westermann, David Oberacker
 */
class TextShape(
        /**
         * Property containing the text that is displayed inside the text shape.
         */
        val property: ReadOnlyProperty<String>,
        /**
         * Strings to use for autocompletion when writing inside the text field.
         */
        val autocomplete: List<String>,
        /**
         * Optional id of the text shape.
         */
        id: Long?,
        /**
         * Text alignment direction.
         */
        val alignment: TextAlignment,
        /**
         * Limiters that should surround the text at all times.
         */
        val surround: Surround
) : Shape(id) {

        /**
         * Text alignment direction.
         */
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
        alignment: TextShape.TextAlignment = TextShape.TextAlignment.LEFT,
        surround: Surround = Surround.NONE
) = TextShape(property, autocomplete, null, alignment, surround).also(init).also(this::add)

fun Linker<*, *>.textShape(
        property: ReadOnlyProperty<String>,
        autocomplete: List<String> = emptyList(),
        init: TextShape.() -> Unit = {},
        alignment: TextShape.TextAlignment = TextShape.TextAlignment.LEFT,
        surround: Surround = Surround.NONE
) = TextShape(property, autocomplete, id, alignment, surround).also(init)
