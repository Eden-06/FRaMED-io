package io.framed.framework.pictogram

import io.framed.framework.util.Point

/**
 * @author lars
 */

data class ContextEvent(
        val position: Point,
        val diagram: Point,
        val target: Pictogram
)

data class SidebarEvent(
        val target: Pictogram
)