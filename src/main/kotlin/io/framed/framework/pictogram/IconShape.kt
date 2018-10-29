package io.framed.framework.pictogram

import io.framed.framework.Linker
import io.framed.framework.util.Property
import io.framed.framework.view.Icon

/**
 * @author lars
 */
class IconShape(
        val property: Property<out Icon?>,
        override val id: Long?
) : Shape() {

}

fun BoxShape.iconShape(
        property: Property<out Icon?>
) = IconShape(property, null).also(this::add)

fun Linker<*, *>.iconShape(
        property: Property<out Icon?>,
        init: IconShape.() -> Unit = {}
) = IconShape(property, id).also(init)