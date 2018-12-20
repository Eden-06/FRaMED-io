package io.framed.framework.pictogram

import de.westermann.kobserve.ReadOnlyProperty
import io.framed.framework.Linker
import io.framed.framework.view.Icon

/**
 * @author lars
 */
class IconShape(
        val property: ReadOnlyProperty<out Icon?>,
        id: Long?
) : Shape(id) {

}

fun BoxShape.iconShape(
        property: ReadOnlyProperty<out Icon?>
) = IconShape(property, null).also(this::add)

fun Linker<*, *>.iconShape(
        property: ReadOnlyProperty<out Icon?>,
        id: Long = this.id,
        init: IconShape.() -> Unit = {}
) = IconShape(property, id).also(init)