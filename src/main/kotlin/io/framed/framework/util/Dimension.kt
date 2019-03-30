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
        val width: Double = 0.0,
        val height: Double = 0.0
) {

    constructor(position: Point, size: Point = Point.ZERO) : this(position.x, position.y, size.x, size.y)

    @Transient
    val position: Point
        get() = Point(left, top)

    @Transient
    val size: Point
        get() = Point(width, height)

    @Transient
    val right: Double
        get() = left + width

    @Transient
    val bottom: Double
        get() = top + height

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
            return Dimension(l, t, abs(width), abs(width))
        }

    operator fun contains(other: Dimension): Boolean = !(other.left > right ||
            other.right < left ||
            other.top > bottom ||
            other.bottom < top)


    operator fun contains(other: Point): Boolean {
        val n = normalized
        return (n.left <= other.x && (n.left + width) >= other.x)
                && (n.top <= other.y && (n.top + height) >= other.y)
    }

    operator fun plus(point: Point) = copy(left + point.x, top + point.y)

    companion object {
        val ZERO = Dimension(0.0, 0.0)
    }
}

val Dimension.center: Point
    get() = Point(left + width / 2, top + height / 2)