package io.framed.model

import io.framed.exporter.visitor.Visitor
import io.framed.framework.model.ModelElement
import kotlinx.serialization.Serializable

/**
 * The model defines an model of the modeling language
 */
@Serializable
class ReturnEvent() : ModelElement() {

    constructor(init: (ReturnEvent) -> Unit) : this() {
        init(this)
    }

    /**
     * io.framed.exporter.crom.crom.Type of the model
     */
    var type: EventType = EventType.STANDARD
    /**
     * Description of the Event
     */
    var desc: String = ""

    override fun copy() = ReturnEvent { new ->
        new.type = type
        new.desc = desc
    }

    override fun <T> acceptVisitor(visitor: Visitor<T>) {
        visitor.visit(this)
    }
}