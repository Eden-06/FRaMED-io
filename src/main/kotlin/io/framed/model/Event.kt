package io.framed.model

import io.framed.view.Icon
import io.framed.view.MaterialIcon
import kotlinx.serialization.Serializable

/**
 * The model defines an event of the modeling language
 */
@Serializable
class Event : Model {
    /**
     * Identifier of the Instance
     */
    override val id: Long = Model.lastId++
    /**
     * Type of the event
     */
    var type: EventType = EventType.NONE
    /**
     * Metadata of the event
     */
    override val metadata = Metadata()
}

/**
 * The method initalizes a new event
 * @param type type of the event
 */
fun Container.event(type: EventType, init: Event.() -> Unit): Event {
    val evt = Event()
    evt.type = type
    evt.init()
    events += evt
    return evt
}

/**
 * The enum defines the type and their specifications
 */
enum class EventType(val printableName: String, val symbol: Icon) {
    RETURN("RETURN", MaterialIcon.BACKSPACE),
    MESSAGE("MESSAGE", MaterialIcon.MESSAGE),
    ERROR("ERROR", MaterialIcon.ERROR),
    NONE("NONE", MaterialIcon.STOP);

    override fun toString() = printableName
}