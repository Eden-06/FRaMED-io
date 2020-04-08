package io.framed.model

import io.framed.framework.model.ModelElement
import kotlinx.serialization.Serializable

/**
 * The model defines an model of the modeling language
 */
@Serializable
class Event() : ModelElement() {

    constructor(init: (Event) -> Unit) : this() {
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

    override fun copy() = Event { new ->
        new.type = type
        new.desc = desc
    }
}
