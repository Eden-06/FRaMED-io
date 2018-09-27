package io.framed.controller

import io.framed.model.Event
import io.framed.picto.IconShape
import io.framed.picto.iconShape
import io.framed.util.ComplexProperty
import io.framed.util.property
import io.framed.view.Icon

class EventController(
        val event: Event,
        override val parent: ContainerController
) : Controller<IconShape>(event, parent) {

    val typeProperty = property(event::type)
    val symbolProperty = property(typeProperty,
            getter = {
                typeProperty.get().symbol
            },
            setter = { _ ->
                throw NotImplementedError()
            }
    )

    override val picto: IconShape = iconShape(symbolProperty) {

    }
}