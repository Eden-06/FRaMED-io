package io.framed.picto

import io.framed.util.Point

/**
 * @author lars
 */

data class ContextEvent(
        val position: Point,
        val target: Picto
)

data class SidebarEvent(
        val target: Picto
) {

    val isNone = this == NONE

    companion object {
        val NONE = SidebarEvent(object : Picto() {})
    }
}