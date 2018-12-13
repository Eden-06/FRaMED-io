package io.framed.framework.util

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlin.math.abs
import kotlin.math.min

/**
 * @author lars
 */
@Serializable
data class Dimension(
        val left: Double,
        val top: Double,
        val width: Double? = null,
        val height: Double? = null
) {

    constructor(position: Point, size: Point? = null) : this(position.x, position.y, size?.x, size?.y)

    @Transient
    val position: Point
        get() = Point(left, top)

    @Transient
    val size: Point?
        get() = if (width != null && height != null) Point(width, height) else null

    @Transient
    val widthNotNull: Double
        get() = width ?: 0.0
    @Transient
    val heightNotNull: Double
        get() = height ?: 0.0

    @Transient
    val right: Double
        get() = left + widthNotNull

    @Transient
    val bottom: Double
        get() = top + heightNotNull

    @Transient
    val edges: Set<Point>
        get() = setOf(
                Point(left, top),
                Point(right, top),
                Point(left, bottom),
                Point(right, bottom)
        )

    @Transient
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

    operator fun plus(point: Point) = copy(left + point.x, top + point.y)
}