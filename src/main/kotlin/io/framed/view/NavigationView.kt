package io.framed.view

import io.framed.util.EventHandler
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.events.Event
import org.w3c.dom.events.EventListener
import org.w3c.dom.events.MouseEvent
import org.w3c.dom.events.WheelEvent
import kotlin.browser.window
import kotlin.math.max
import kotlin.math.min

/**
 * @author lars
 */
class NavigationView : View<HTMLDivElement>("div") {

    var touchpadControl: Boolean = false

    private val transformBox = createView<HTMLDivElement>().also {
        html.appendChild(it)
    }
    val container = createView<HTMLDivElement>().also {
        transformBox.appendChild(it)
    }

    val zoomSteps = listOf(0.1, 0.3, 0.5, 0.67, 0.8, 0.9, 1.0, 1.1, 1.2, 1.33, 1.5, 1.7, 2.0, 2.4, 3.0, 4.0)
    var zoom: Double = 1.0
        set(value) {
            val old = field
            field = min(zoomSteps.max() ?: 1.0, max(zoomSteps.min() ?: 1.0, value))
            if (field != old) {
                zoomListener.fire(field)
            }
        }
    val zoomListener = EventHandler<Double>()

    fun zoomBy(delta: Double, x: Double = 0.5, y: Double = 0.5) =
            zoomTo(zoom + delta, x, y)

    fun zoomTo(zoom: Double, x: Double = 0.5, y: Double = 0.5) {
        val old = this.zoom
        this.zoom = zoom
        val new = this.zoom

        //val dx = ((0.5 - x) * clientWidth * (new - old))
        //val dy = ((0.5 - y) * clientHeight * (new - old))

        val dx = (clientWidth * (0.5 - x) * (1 / new - 1 / old))
        val dy = (clientHeight * (0.5 - y) * (1 / new - 1 / old))

        pan = Coordinate(pan.x - dx, pan.y - dy)

        updateTransform()
    }

    var pan: Coordinate = Coordinate(0.0, 0.0)

    fun panBy(x: Double, y: Double) = panTo(pan.x + x, pan.y + y)

    fun panTo(x: Double, y: Double) {
        pan = Coordinate(x, y)
        updateTransform()
    }

    private fun updateTransform() {
        transformBox.style.transform = "scale($zoom) translate($pan)"
    }

    private var moveStartX: Int = 0
    private var moveStartY: Int = 0

    private val moveStartListener = object : EventListener {
        override fun handleEvent(event: Event) {
            (event as? MouseEvent)?.let { e ->
                moveStartX = e.clientX
                moveStartY = e.clientY

                window.addEventListener("mousemove", movePerformListener)
                window.addEventListener("mouseup", moveEndListener)
                window.addEventListener("mouseleave", moveEndListener)
            }
        }
    }
    private val movePerformListener = object : EventListener {
        override fun handleEvent(event: Event) {
            (event as? MouseEvent)?.let { e ->
                panBy((e.clientX - moveStartX).toDouble() / zoom, (e.clientY - moveStartY).toDouble() / zoom)
                moveStartX = e.clientX
                moveStartY = e.clientY
            }
        }
    }
    private val moveEndListener = object : EventListener {
        override fun handleEvent(event: Event) {
            window.removeEventListener("mousemove", movePerformListener)
            window.removeEventListener("mouseup", this)
            window.removeEventListener("mouseleave", this)
        }
    }
    private val scrollListener = object : EventListener {
        override fun handleEvent(event: Event) {
            (event as? WheelEvent)?.let { e ->
                e.preventDefault()

                val (deltaX, deltaY) = when (e.deltaMode) {
                    WheelEvent.DOM_DELTA_PIXEL -> {
                        Pair(e.deltaX / 6, e.deltaY / 6)
                    }
                    WheelEvent.DOM_DELTA_LINE -> {
                        Pair(
                                min(1.0, max(-1.0, e.deltaX)) * 8,
                                min(1.0, max(-1.0, e.deltaY)) * 8
                        )
                    }
                    else -> Pair(e.deltaX, e.deltaY)
                }


                if (!touchpadControl || e.ctrlKey) {
                    zoomBy(deltaY / 150, e.clientX.toDouble() / clientWidth, (e.clientY.toDouble() - offsetTop) / clientHeight)
                } else {
                    panBy(-1 * deltaX, -1 * deltaY)
                }
            }
        }
    }

    init {
        html.addEventListener("mousedown", moveStartListener)
        html.addEventListener("wheel", scrollListener)
    }

    data class Coordinate(
            val x: Double,
            val y: Double
    ) {
        override fun toString(): String {
            return "${x}px, ${y}px"
        }
    }
}