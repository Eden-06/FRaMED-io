package io.framed.picto

import io.framed.util.Property
import io.framed.view.Icon

/**
 * @author lars
 */
class IconShape(
        val property: Property<out Icon>
) : Shape() {

}

fun BoxShape.iconShape(
        property: Property<out Icon>
) = IconShape(property).also(this::add)

fun iconShape(
        property: Property<out Icon>,
        init: IconShape.() -> Unit = {}
) = IconShape(property).also(init)