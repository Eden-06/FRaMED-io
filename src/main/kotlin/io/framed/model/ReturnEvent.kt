package io.framed.model

import io.framed.framework.ModelElement
import kotlinx.serialization.Serializable

/**
 * The model defines an model of the modeling language
 */
@Serializable
class ReturnEvent() : ModelElement<ReturnEvent>() {

    constructor(init: (ReturnEvent) -> Unit) : this() {
        init(this)
    }

    /**
     * Type of the model
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
}