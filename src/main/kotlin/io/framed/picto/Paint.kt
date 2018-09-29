package io.framed.picto

import kotlin.math.roundToInt

/**
 * @author lars
 */
interface Paint {
    fun toCss(): String
}

data class Color(
        val red: Int,
        val green: Int,
        val blue: Int,
        val alpha: Double = 1.0
) : Paint {
    companion object {
        val TRANSPARENT = Color(0, 0, 0, 0.0)

        fun parse(color: String): Color {
            if (color.startsWith("#")) {
                val r = color.substring(1, 3)
                val g = color.substring(3, 5)
                val b = color.substring(5, 7)
                return Color(r.toInt(16), g.toInt(16), b.toInt(16), 1.0)
            } else {
                throw IllegalArgumentException()
            }
        }
    }

    override fun toCss(): String = "rgba($red, $green, $blue, $alpha)"
}

fun color(red: Int, green: Int, blue: Int, alpha: Double) = Color(red, green, blue, alpha)
fun color(color: String): Color = Color.parse(color)

class LinearGradient(
        var direction: String,
        var colorStops: List<Pair<Color, Double>> = emptyList()
) : Paint {

    fun add(color: Color, stop: Double) {
        colorStops += color to stop
    }

    override fun toCss(): String =
            "linear-gradient(" + direction + colorStops.map { ", ${it.first.toCss()} ${(it.second*100).roundToInt()}%" }.joinToString(" ") + ")"
}

fun linearGradient(direction: String, init: LinearGradient.() -> Unit): LinearGradient =
        LinearGradient(direction).also(init)