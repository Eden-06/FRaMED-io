package io.framed.util

import kotlin.math.abs
import kotlin.math.min

/**
 * @author lars
 */
data class Dimension(
        val left: Double,
        val top: Double,
        val width: Double,
        val height: Double
) {

    val edges: Set<Point>
        get() = setOf(
                Point(left, top),
                Point(left + width, top),
                Point(left, top + height),
                Point(left + width, top + height)
        )

    val normalized: Dimension
        get() {
            val l = min(left, left + width)
            val t = min(top, top + height)
            return Dimension(l, t, abs(width), abs(height))
        }

    operator fun contains(other: Dimension): Boolean =
            other.edges.any { contains(it) }


    operator fun contains(other: Point): Boolean {
        val n = normalized
        return (n.left <= other.x && (n.left + width) >= other.x)
                && (n.top <= other.y && (n.top + height) >= other.y)
    }
}