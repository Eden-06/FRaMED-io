package io.framed.linker

import io.framed.model.Event
import io.framed.picto.IconShape
import io.framed.picto.iconShape
import io.framed.util.property

class EventLinker(
        val event: Event,
        override val parent: ContainerLinker
) : Linker<IconShape>(event, parent) {

    private val typeProperty = property(event::type)
    private val symbolProperty = property(typeProperty,
            getter = {
                typeProperty.get().symbol
            }
    )

    override val picto: IconShape = iconShape(symbolProperty) {

    }
}