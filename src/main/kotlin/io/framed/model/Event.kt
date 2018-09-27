package io.framed.model

import io.framed.view.MaterialIcon

class Event : Model() {
    var type: EventType = EventType.NONE
}

enum class EventType(val printableName: String, val symbol: MaterialIcon) {
    RETURN("Return", MaterialIcon.BACKSPACE),
    MESSAGE("Message", MaterialIcon.MESSAGE),
    ERROR("Error", MaterialIcon.ERROR),
    NONE("", MaterialIcon.ACCESSIBILITY);

    override fun toString() = printableName
}