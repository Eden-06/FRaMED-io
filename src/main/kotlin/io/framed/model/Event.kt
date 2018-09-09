package io.framed.model

import io.framed.view.MaterialIcon

enum class Event(val printableName: String, val symbol: MaterialIcon) {
    RETURN("Return", MaterialIcon.BACKSPACE),
    MESSAGE("Message", MaterialIcon.MESSAGE),
    ERROR("Error", MaterialIcon.ERROR);

    override fun toString() = printableName

    fun getSymbol() = symbol
}