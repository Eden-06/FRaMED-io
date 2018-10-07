package io.framed.framework.view

import io.framed.framework.util.Dimension
import io.framed.framework.util.EventHandler
import io.framed.framework.util.Point
import io.framed.framework.util.point
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.events.Event
import org.w3c.dom.events.EventListener
import org.w3c.dom.events.MouseEvent
import org.w3c.dom.events.WheelEvent
import kotlin.browser.window
import kotlin.math.abs
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
     * Html model to apply transformations.
     */
    private val transformBox = ListView().also {
        html.appendChild(it.html)
    }

    /**
     * Content html model.
     */
    val container = ListView().also {
        transformBox += it
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
            //val old = field
            field = min(zoomSteps.max() ?: 1.0, max(zoomSteps.min() ?: 1.0, value))
        }

    /**
     * Listener for zoom changes.
     */
    val onZoom = EventHandler<Double>()
    val onPan = EventHandler<Point>()

    var viewBox: Dimension
        get() {
            val client = Point(clientWidth, clientHeight)

            val size = client / zoom
            val p = pan - (client / 2.0 - (size / 2.0))

            return Dimension(p, size)
        }
        set(value) {
            val client = Point(clientWidth, clientHeight)

            val size = value.size ?: client
            val p = value.position

            zoom = (client / size).min()
            pan = p + (client / 2.0 - (size / 2.0))

            updateTransform()
        }

    /**
     * Zoom by a relative zoom step.
     *
     * @param delta Relative zoom step.
     * @param center zoom center in percent. Defaults to 50%.
     */
    fun zoomBy(delta: Double, center: Point = Point(0.5, 0.5)) =
            zoomTo(zoom + delta, center)


    /**
     * Zoom to a absolute zoom step.
     *
     * @param zoom Absolute zoom step.
     * @param center zoom center in percent. Defaults to 50%.
     */
    fun zoomTo(zoom: Double, center: Point = Point(0.5, 0.5)) {
        val old = this.zoom
        this.zoom = zoom
        val new = this.zoom

        pan -= (Point(clientWidth, clientHeight) * (-center + 0.5) * (1 / new - 1 / old))

        if (old != new) {
            onZoom.fire(new)
        }
        updateTransform()
    }

    /**
     * Current pan.
     */
    var pan: Point = Point.ZERO
        private set

    /**
     * Pan by a relative coordinate.
     *
     * @param coordinate Relative coordinate.
     */
    fun panBy(coordinate: Point) = panTo(pan + coordinate)

    /**
     * Pan to a absolute coordinate.
     *
     * @param coordinate Absolute coordinate.
     */
    fun panTo(coordinate: Point) {
        pan = coordinate
        onPan.fire(coordinate)
        updateTransform()
    }

    /**
     * Apply transformation to transformBox.
     */
    private fun updateTransform() {
        transformBox.html.style.transform = "scale($zoom) translate(${pan.asPx})"
    }

    fun mouseToCanvas(mouse: Point): Point {
        val client = Point(clientWidth, clientHeight)
        val c = (mouse - Point(offsetLeft, offsetTop)) / client - 0.5

        return client / 2.0 + c * client / zoom - pan
    }

    /**
     * Move reference position for pan.
     */
    private var moveStart: Point = Point.ZERO

    val selectBox = ListView().also {
        it.classes += "select-box"
    }

    /**
     * Mouse down model listener.
     */
    private val moveStartListener = object : EventListener {
        override fun handleEvent(event: Event) {
            (event as? MouseEvent)?.let { e ->

                if (e.ctrlKey) {
                    moveStart = mouseToCanvas(e.point())

                    window.addEventListener("mousemove", selectPerformListener)

                    selectBox.width = 0.0
                    selectBox.height = 0.0
                    selectBox.top = moveStart.y
                    selectBox.left = moveStart.x
                    container += selectBox
                } else {
                    moveStart = e.point()

                    window.addEventListener("mousemove", movePerformListener)
                }
                window.addEventListener("mouseup", moveEndListener)
                window.addEventListener("mouseleave", moveEndListener)
            }
        }
    }

    /**
     * Mouse move model listener.
     */
    private val movePerformListener = object : EventListener {
        override fun handleEvent(event: Event) {
            event.preventDefault()
            (event as? MouseEvent)?.let { e ->
                val mouse = e.point()
                panBy((mouse - moveStart) / zoom)
                moveStart = mouse
            }
        }
    }

    /**
     * Mouse select model listener.
     */
    private val selectPerformListener = object : EventListener {
        override fun handleEvent(event: Event) {
            event.preventDefault()
            (event as? MouseEvent)?.let { e ->
                val mouse = mouseToCanvas(e.point())

                val top = min(mouse.y, moveStart.y)
                val left = min(mouse.x, moveStart.x)

                val width = abs(mouse.x - moveStart.x)
                val height = abs(mouse.y - moveStart.y)

                selectBox.top = top
                selectBox.left = left

                selectBox.width = width
                selectBox.height = height

                onSelect.fire(Dimension(left, top, width, height))
            }
        }
    }

    /**
     * Mouse up an mouse leave listener.
     */
    private val moveEndListener = object : EventListener {
        override fun handleEvent(event: Event) {
            container -= selectBox

            window.removeEventListener("mousemove", movePerformListener)
            window.removeEventListener("mousemove", selectPerformListener)
            window.removeEventListener("mouseup", this)
            window.removeEventListener("mouseleave", this)
        }
    }

    val onSelect = EventHandler<Dimension?>()

    /**
     * Wheel listener.
     */
    private val scrollListener = object : EventListener {
        override fun handleEvent(event: Event) {
            (event as? WheelEvent)?.let { e ->
                e.preventDefault()

                val delta = when (e.deltaMode) {
                    WheelEvent.DOM_DELTA_PIXEL -> {
                        Point(e.deltaX / 6, e.deltaY / 6)
                    }
                    WheelEvent.DOM_DELTA_LINE -> {
                        Point(
                                min(1.0, max(-1.0, e.deltaX)) * 8,
                                min(1.0, max(-1.0, e.deltaY)) * 8
                        )
                    }
                    else -> Point(e.deltaX, e.deltaY)
                }


                if (!touchpadControl || e.ctrlKey) {
                    e.point() - Point(offsetLeft, offsetTop) / Point(clientWidth, clientHeight)
                    zoomBy(delta.y / 150, (e.point() - Point(offsetLeft, offsetTop)) / Point(clientWidth, clientHeight))
                } else {
                    panBy(-delta)
                }
            }
        }
    }

    init {
        html.addEventListener("mousedown", moveStartListener)
        html.addEventListener("wheel", scrollListener)

        onClick {
            onSelect.fire(null)
        }
    }
}