package io.framed.util

import kotlin.math.abs
import kotlin.math.min

/**
 * @author lars
 */
data class Dimension(
        val left: Double,
        val top: Double,
        val width: Double? = null,
        val height: Double? = null
) {

    val widthNotNull: Double
        get() = width ?: 0.0
    val heightNotNull: Double
        get() = height ?: 0.0

    val right: Double
        get() = left + widthNotNull

    val bottom: Double
        get() = top + heightNotNull

    val edges: Set<Point>
        get() = setOf(
                Point(left, top),
                Point(right, top),
                Point(left, bottom),
                Point(right, bottom)
        )

    val normalized: Dimension
        get() {
            val l = min(left, right)
            val t = min(top, bottom)
            return Dimension(l, t, abs(widthNotNull), abs(heightNotNull))
        }

    /*
    operator fun contains(other: Dimension): Boolean =
            other.edges.any { contains(it) }
            */

    operator fun contains(other: Dimension): Boolean = !(other.left > right ||
            other.right < left ||
            other.top > bottom ||
            other.bottom < top)


    operator fun contains(other: Point): Boolean {
        val n = normalized
        return (n.left <= other.x && (n.left + widthNotNull) >= other.x)
                && (n.top <= other.y && (n.top + heightNotNull) >= other.y)
    }
}