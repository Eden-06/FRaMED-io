package io.framed.model

import io.framed.framework.view.FramedIcon
import io.framed.framework.view.Icon

/**
 * The enum defines the model and their specifications
 */
enum class EventType(val printableName: String, val symbol: Icon?) {
    STANDARD("Standard", FramedIcon.EVENT_STANDARD),
    TIMER("Timer", FramedIcon.EVENT_TIMER),
    MESSAGE("Message", FramedIcon.EVENT_MESSAGE),
    ERROR("Error", FramedIcon.EVENT_ERROR),
    CONDITION("Condition", FramedIcon.EVENT_CONDITION),
    SIGNAL("Signal", FramedIcon.EVENT_SIGNAL);
}
