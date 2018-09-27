package io.framed.picto

import io.framed.util.Property
import io.framed.view.Icon
import io.framed.view.MaterialIcon

/**
 * @author lars
 */
class IconShape(
        val property: Property<MaterialIcon>
) : Shape() {

}

fun BoxShape.iconShape(
        property: Property<MaterialIcon>
) = IconShape(property).also(this::add)

fun iconShape(
        property: Property<MaterialIcon>,
        init: IconShape.() -> Unit = {}
) = IconShape(property).also(init)