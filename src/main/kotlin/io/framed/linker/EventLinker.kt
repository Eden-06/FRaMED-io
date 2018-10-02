package io.framed.linker

import io.framed.model.Event
import io.framed.picto.*
import io.framed.util.property

class EventLinker(
        val event: Event,
        override val parent: ContainerLinker
) : Linker<IconShape>(event, parent) {

    private val typeProperty = property(event::type)
    val symbolProperty = property(typeProperty,
            getter = {
                typeProperty.get().symbol
            }
    )

    override val picto: IconShape = iconShape(symbolProperty) {
        style {
            background = linearGradient("to bottom") {
                add(color("#fffbd9"), 0.0)
                add(color("#fff7c4"), 1.0)
            }
            border {
                style = Border.BorderStyle.SOLID
                width = 1.0
                color = color(0, 0, 0, 0.3)
                radius = 10.0
            }
            padding {
                paddingTop = 10.0
                paddingRight = 10.0
                paddingBottom = 10.0
                paddingLeft = 10.0
            }
        }
    }
}