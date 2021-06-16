package io.framed.framework.view

import de.westermann.kobserve.event.EventListener
import io.framed.framework.util.Point
import io.framed.framework.util.async
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.events.KeyboardEvent
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

    private fun keyListener(event: KeyboardEvent) {

        val keyCode = event.keyCode

        if (keyCode == 27) {
            close()
        } else if (keyCode in 48..57) {
            selectDirection(keyCode - 48)
        }
    }

    private var positionMap: Map<IntRange, View<*>> = emptyMap()

    private fun selectDirection(index: Int) {
        val angle = ((360 / data.size) * index)

        positionMap.filterKeys {
            angle in it
        }.values.firstOrNull()?.let {
            it.onClick.emit(js("{}"))
            close()
        }
    }

    var reference: EventListener<*>? = null

    fun open() {
        Root += this
        reference = Root.onKeyDown.reference(this::keyListener)
    }

    private fun close() {
        Root -= this
        reference?.detach()
    }

    init {
        val container = listView { }

        val views = data.map {
            container.listView {
                init(it)
            }
        }

        open()

        onClick {
            close()
        }


        async {
            val height = (views.asSequence().map { it.clientHeight }.maxOrNull() ?: 0).toDouble()
            val width = (views.asSequence().map { it.clientWidth }.maxOrNull() ?: 0).toDouble()

            container.left = Root.mousePosition.x
            container.top = Root.mousePosition.y

            val part = 360 / views.size
            val scaling: Double = (views.size) / 1.9 // 3.0

            positionMap = views.mapIndexed { index, view ->
                val angle = (part * index)
                val pos = calc(scaling * width, scaling * height, angle)
                view.top = pos.y
                view.left = pos.x

                view.marginTop = -(height / 1.8)
                view.marginLeft = -(width / 1.8)

                view.width = width
                view.height = height

                angle.rangeBy(part) to view
            }.toMap()
        }
    }

    private fun Int.rangeBy(distance: Int): IntRange = (this - distance / 2)..(this + distance / 2)
}
