package io.framed.framework.view

import io.framed.framework.util.Point
import io.framed.framework.util.async
import org.w3c.dom.HTMLDivElement
import kotlin.math.PI
import kotlin.math.sqrt
import kotlin.math.tan

class CyclicChooser<T>(
        val data: Collection<T>,
        val init: ListView.(T) -> Unit
) : ViewCollection<View<*>, HTMLDivElement>("div") {

    private fun calc(width: Double, height: Double, angle: Int): Point {
        val shortAngle = (angle % 360)
        val pi = (angle % 360) / 180.0 * PI
        val a = width / 2
        val b = height / 2

        val signX = if (shortAngle in 90 until 270) -1 else 1
        val signY = if (shortAngle in 0 until 180) -1 else 1

        val h = tan(pi)
        val x = signX * (a * b) / sqrt(b * b + a * a * h * h)
        val i = (x / a)
        val y = signY * sqrt(1 - i * i) * b

        return Point(x, y)
    }

    init {
        val container = listView { }

        val views = data.map {
            container.listView {
                init(it)
            }
        }

        Root += this

        onClick {
            Root -= this
        }

        async {
            val height = (views.asSequence().map { it.clientHeight }.max() ?: 0) * 2.2
            val width = (views.asSequence().map { it.clientWidth }.max() ?: 0) * 1.8

            container.left = Root.mousePosition.x
            container.top = Root.mousePosition.y

            val part = 360 / views.size
            views.forEachIndexed { index, view ->
                val pos = calc(width, height, part * index)
                view.top = pos.y
                view.left = pos.x

                view.marginTop = -(view.clientHeight / 2.0)
                view.marginLeft = -(view.clientWidth / 2.0)
            }
        }
    }
}