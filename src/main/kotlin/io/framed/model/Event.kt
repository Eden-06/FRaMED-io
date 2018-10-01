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
 * The enum defines the type and their specifications
 */
enum class EventType(val printableName: String, val symbol: Icon) {
    RETURN("Return", MaterialIcon.BACKSPACE),
    MESSAGE("Message", MaterialIcon.MESSAGE),
    ERROR("Error", MaterialIcon.ERROR),
    NONE("", MaterialIcon.ACCESSIBILITY);

    override fun toString() = printableName
}