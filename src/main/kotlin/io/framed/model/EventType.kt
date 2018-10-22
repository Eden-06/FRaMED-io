package io.framed.model

import io.framed.framework.view.Icon
import io.framed.framework.view.MaterialIcon

/**
 * The enum defines the model and their specifications
 */
enum class EventType(val printableName: String, val symbol: Icon?) {
    TIMER("Timer", MaterialIcon.AV_TIMER),
    MESSAGE("Message", MaterialIcon.MESSAGE),
    ERROR("Error", MaterialIcon.FLASH_ON),
    STANDARD("Standard", MaterialIcon.RADIO_BUTTON_UNCHECKED),
    CONDITION("Condition", MaterialIcon.REORDER),
    SIGNAL("Signal", MaterialIcon.DETAILS);
}
