package io.framed.controller

import io.framed.model.Event
import io.framed.picto.IconShape
import io.framed.picto.iconShape
import io.framed.util.property

class EventController(
        val event: Event,
        override val parent: ContainerController
) : Controller<IconShape>(event, parent) {

    private val typeProperty = property(event::type)
    private val symbolProperty = property(typeProperty,
            getter = {
                typeProperty.get().symbol
            }
    )

    override val picto: IconShape = iconShape(symbolProperty) {

    }
}