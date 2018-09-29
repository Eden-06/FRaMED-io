package io.framed.model

import io.framed.view.Icon
import io.framed.view.MaterialIcon
import kotlinx.serialization.Serializable

@Serializable
class Event : Model {

    override val id: Long = Model.lastId++

    var type: EventType = EventType.NONE

    override val metadata = Metadata()
}

enum class EventType(val printableName: String, val symbol: Icon) {
    RETURN("Return", MaterialIcon.BACKSPACE),
    MESSAGE("Message", MaterialIcon.MESSAGE),
    ERROR("Error", MaterialIcon.ERROR),
    NONE("", MaterialIcon.ACCESSIBILITY);

    override fun toString() = printableName
}