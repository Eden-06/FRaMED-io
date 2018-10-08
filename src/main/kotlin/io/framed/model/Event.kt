package io.framed.model

import io.framed.framework.ModelElement
import io.framed.framework.view.Icon
import io.framed.framework.view.MaterialIcon
import kotlinx.serialization.Serializable

/**
 * The model defines an model of the modeling language
 */
@Serializable
class Event : ModelElement {
    /**
     * Identifier of the Instance
     */
    override val id: Long = ModelElement.lastId++
    /**
     * Type of the model
     */
    var type: EventType = EventType.NONE
    /**
     * Description of the Event
     */
    var desc: String = ""
}

/**
 * The model initalizes a new model
 * @param type model of the model
 */
fun Container.event(type: EventType, desc: String, init: Event.() -> Unit): Event {
    val evt = Event()
    evt.type = type
    evt.desc = desc
    evt.init()
    events += evt
    return evt
}

/**
 * The enum defines the model and their specifications
 */
enum class EventType(val printableName: String, val symbol: Icon?) {
    RETURN("Return", MaterialIcon.BACKSPACE),
    MESSAGE("Message", MaterialIcon.MESSAGE),
    ERROR("Error", MaterialIcon.ERROR),
    NONE("None", null);
}