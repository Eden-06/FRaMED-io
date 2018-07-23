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
 * Represents a canvas object.
 *
 * @author lars
 */
class NavigationView : View<HTMLDivElement>("div") {

    /**
     * Set the zoom and pan mode.
     *
     * mouse control: zoom with scroll, pan with mouse.
     * touchpad control: zoom with scroll + ctrl, pan with scroll or mouse.
     */
    var touchpadControl: Boolean = false

    /**
     * Html container to apply transformations.
     */
    private val transformBox = createView<HTMLDivElement>().also {
        html.appendChild(it)
    }

    /**
     * Content html container.
     */
    val container = createView<HTMLDivElement>().also {
        transformBox.appendChild(it)
    }

    /**
     * List of zoom steps for stepped zooming.
     */
    val zoomSteps = listOf(0.1, 0.3, 0.5, 0.67, 0.8, 0.9, 1.0, 1.1, 1.2, 1.33, 1.5, 1.7, 2.0, 2.4, 3.0, 4.0)

    /**
     * Current zoom depth. Zoom step lies between 0.1 and 4.0.
     */
    var zoom: Double = 1.0
        set(value) {
            val old = field
            field = min(zoomSteps.max() ?: 1.0, max(zoomSteps.min() ?: 1.0, value))
            if (field != old) {
                zoomListener.fire(field)
            }
        }

    /**
     * Listener for zoom changes.
     */
    val zoomListener = EventHandler<Double>()

    /**
     * Zoom by a relative zoom step.
     *
     * @param delta Relative zoom step.
     * @param x Left zoom center in percent. Defaults to 50%.
     * @param y Top zoom center in percent. Defaults to 50%.
     */
    fun zoomBy(delta: Double, x: Double = 0.5, y: Double = 0.5) =
            zoomTo(zoom + delta, x, y)


    /**
     * Zoom to a absolute zoom step.
     *
     * @param zoom Absolute zoom step.
     * @param x Left zoom center in percent. Defaults to 50%.
     * @param y Top zoom center in percent. Defaults to 50%.
     */
    fun zoomTo(zoom: Double, x: Double = 0.5, y: Double = 0.5) {
        val old = this.zoom
        this.zoom = zoom
        val new = this.zoom

        val dx = (clientWidth * (0.5 - x) * (1 / new - 1 / old))
        val dy = (clientHeight * (0.5 - y) * (1 / new - 1 / old))

        pan = Coordinate(pan.x - dx, pan.y - dy)

        updateTransform()
    }

    /**
     * Current pan.
     */
    var pan: Coordinate = Coordinate(0.0, 0.0)
        private set

    /**
     * Pan by a relative width.
     *
     * @param x x delta.
     * @param y y delta.
     */
    fun panBy(x: Double, y: Double) = panTo(pan.x + x, pan.y + y)

    /**
     * Pan to a absolute coordinate.
     *
     * @param x x position.
     * @param y y position.
     */
    fun panTo(x: Double, y: Double) {
        pan = Coordinate(x, y)
        updateTransform()
    }

    /**
     * Apply transformation to transformBox.
     */
    private fun updateTransform() {
        transformBox.style.transform = "scale($zoom) translate($pan)"
    }

    fun mouseToCanvas(x: Double, y: Double): Pair<Double, Double> {
        val cx = (x - offsetLeft) / clientWidth - 0.5
        val cy = (y - offsetTop) / clientHeight - 0.5

        val hx = clientWidth / 2.0 + cx * clientWidth / zoom - pan.x
        val hy = clientHeight / 2.0 + cy * clientHeight / zoom - pan.y

        return hx to hy
    }

    /**
     * Move reference x  position for pan.
     */
    private var moveStartX: Int = 0

    /**
     * Move reference y position for pan.
     */
    private var moveStartY: Int = 0

    /**
     * Mouse down event listener.
     */
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

    /**
     * Mouse move event listener.
     */
    private val movePerformListener = object : EventListener {
        override fun handleEvent(event: Event) {
            (event as? MouseEvent)?.let { e ->
                panBy((e.clientX - moveStartX).toDouble() / zoom, (e.clientY - moveStartY).toDouble() / zoom)
                moveStartX = e.clientX
                moveStartY = e.clientY
            }
        }
    }

    /**
     * Mouse up an mouse leave listener.
     */
    private val moveEndListener = object : EventListener {
        override fun handleEvent(event: Event) {
            window.removeEventListener("mousemove", movePerformListener)
            window.removeEventListener("mouseup", this)
            window.removeEventListener("mouseleave", this)
        }
    }

    /**
     * Wheel listener.
     */
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

    /**
     * Helper class for coordinates.
     */
    data class Coordinate(
            val x: Double,
            val y: Double
    ) {
        override fun toString(): String {
            return "${x}px, ${y}px"
        }
    }
}