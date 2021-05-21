package io.framed.framework.view

import de.westermann.kobserve.event.EventHandler
import de.westermann.kobserve.property.property
import io.framed.framework.util.Dimension
import io.framed.framework.util.Point
import io.framed.framework.util.async
import io.framed.framework.util.point
import org.w3c.dom.*
import org.w3c.dom.events.Event
import org.w3c.dom.events.EventListener
import org.w3c.dom.events.MouseEvent
import org.w3c.dom.events.WheelEvent
import kotlin.browser.document
import kotlin.browser.window
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * Represents a canvas object.
 *
 * @author lars
 */
class NavigationView : View<HTMLDivElement>("div") {

    private val background = (document.createElement("canvas") as HTMLCanvasElement).also { background ->
        html.appendChild(background)
    }
    private val context: CanvasRenderingContext2D = background.getContext("2d")!! as CanvasRenderingContext2D

    val renderGridProperty = property(window.localStorage["show-grid"]?.toBoolean() ?: true).also { property ->
        property.onChange {
            updateTransform()
            window.localStorage["show-grid"] = property.value.toString()
        }
    }
    var renderGrid by renderGridProperty

    val touchControlProperty = property(window.localStorage["touch-control"]?.toBoolean() ?: false).also { property ->
        property.onChange {
            window.localStorage["touch-control"] = property.value.toString()
        }
    }
    var touchControl by touchControlProperty

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
     * Current zoom depth. Zoom step lies between 0.1 and 4.0.
     */
    var zoom: Double = 1.0
        private set(value) {
            val old = field
            field = min(zoomSteps.maxOrNull() ?: 1.0, max(zoomSteps.minOrNull() ?: 1.0, value))

            if (old != field) {
                onZoom.emit(field)
                updateTransform()
            }
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

            var size = value.size
            if (size.isZero) {
                size = client
            }

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

        pan -= Point(clientWidth, clientHeight) * (-center + 0.5) * (1 / new - 1 / old)

        if (old != new) {
            onZoom.emit(new)
            updateTransform()
        }
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
        onPan.emit(coordinate)
        updateTransform()
    }

    /**
     * Apply transformation to transformBox.
     */
    private fun updateTransform() {
        transformBox.html.style.transform = "scale($zoom) translate(${pan.asPx})"

        drawGrid()
    }

    private var hLine: Set<Double> = emptySet()
    private var vLine: Set<Double> = emptySet()

    fun resize() {
        background.width = clientWidth
        background.height = clientHeight

        updateTransform()
    }

    val gridStep: Int
        get() = when {
            zoom <= 0.2 -> 1
            zoom <= 0.8 -> GRID_STEP / 2
            else -> GRID_STEP
        }

    val gridSize: Int
        get() = GRID_SIZE * (GRID_STEP / gridStep)

    private fun drawGrid() {
        context.clearRect(0.0, 0.0, clientWidth.toDouble(), clientHeight.toDouble())

        if (renderGrid) {
            context.beginPath()
            context.strokeStyle = "rgba(0, 0, 0, 0.3)"

            val topLeft = realToSystem(Point.ZERO)

            val size = gridSize * zoom

            val startX = size - (topLeft.x * zoom) % size
            val xCount = clientWidth / size.toInt() + 1
            val startXr = ((realToSystem(Point(startX / clientWidth, 0.0)).x) / gridSize).roundToInt()
            for (x in -1..xCount) {
                context.beginPath()
                context.lineWidth = if ((startXr + x) % gridStep == 0) 0.3 else 0.15
                context.moveTo(startX + x * size, 0.0)
                context.lineTo(startX + x * size, clientHeight.toDouble())
                context.stroke()
            }

            val startY = size - (topLeft.y * zoom) % size
            val yCount = clientHeight / size.toInt() + 1
            val startYr = ((realToSystem(Point(0.0, startY / clientHeight)).y) / gridSize).roundToInt()
            for (y in -1..yCount) {
                context.beginPath()
                context.lineWidth = if ((startYr + y) % gridStep == 0) 0.3 else 0.15
                context.moveTo(0.0, startY + y * size)
                context.lineTo(clientWidth.toDouble(), startY + y * size)
                context.stroke()
            }
        }

        for (px in vLine) {
            context.beginPath()
            context.lineWidth = 1.0
            context.strokeStyle = "#FFC107"
            val p = systemToReal(Point(px, 0.0)).x
            context.moveTo(p * clientWidth, 0.0)
            context.lineTo(p * clientWidth, clientHeight.toDouble())
            context.stroke()
        }

        for (py in hLine) {
            context.beginPath()
            context.lineWidth = 1.0
            context.strokeStyle = "#FFC107"
            val p = systemToReal(Point(0.0, py)).y
            context.moveTo(0.0, p * clientHeight)
            context.lineTo(clientWidth.toDouble(), p * clientHeight)
            context.stroke()
        }
    }

    fun clearLines() {
        hLine = emptySet()
        vLine = emptySet()
        updateTransform()
    }

    fun hLines(lines: Set<Double>) {
        hLine = lines
        updateTransform()
    }

    fun vLines(lines: Set<Double>) {
        vLine = lines
        updateTransform()
    }

    /**
     * Calculate the coordinate system point of the given screen point.
     *
     * @param point Screen point in percent.
     * @return The corresponding point on the coordinate system.
     */
    fun realToSystem(point: Point): Point {
        val client = Point(clientWidth, clientHeight)
        return client / 2.0 + (point - 0.5) * client / zoom - pan
    }

    /**
     * Calculate the on screen point of the given coordinate system point.
     *
     * @param point Coordinate system point.
     * @return The corresponding point on the screen in percent.
     */
    fun systemToReal(point: Point): Point {
        val client = Point(clientWidth, clientHeight)
        return ((point + pan - client / 2.0) * zoom / client) + 0.5
    }

    fun mouseToCanvas(mouse: Point): Point = realToSystem((mouse - Point(offsetLeft, offsetTop)) / Point(clientWidth, clientHeight))

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
            if (event is MouseEvent) {
                if (event.ctrlKey) {
                    moveStart = mouseToCanvas(event.point())

                    window.addEventListener("mousemove", selectPerformListener)

                    selectBox.width = 0.0
                    selectBox.height = 0.0
                    selectBox.top = moveStart.y
                    selectBox.left = moveStart.x
                    container += selectBox
                } else {
                    moveStart = event.point()

                    window.addEventListener("mousemove", movePerformListener)
                }
                window.addEventListener("mouseup", moveEndListener)
                window.addEventListener("mouseleave", moveEndListener)
            } else if (event is TouchEvent) {
                val touch = event.touches[0] ?: return
                moveStart = touch.point()
                window.addEventListener("touchmove", movePerformListener)
                window.addEventListener("touchend", moveEndListener)
                window.addEventListener("touchcancel", moveEndListener)
            }
        }
    }

    /**
     * Mouse move model listener.
     */
    private val movePerformListener = object : EventListener {
        override fun handleEvent(event: Event) {
            event.preventDefault()
            if (event is MouseEvent) {
                val mouse = event.point()
                panBy((mouse - moveStart) / zoom)
                moveStart = mouse
            } else if (event is TouchEvent) {
                val touch = event.touches[0] ?: return
                val mouse = touch.point()
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

                onSelect.emit(Dimension(left, top, width, height))
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

            window.removeEventListener("touchmove", movePerformListener)
            window.removeEventListener("touchend", this)
            window.removeEventListener("touchcancel", this)
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

                if (!touchControl || e.ctrlKey) {
                    e.point() - Point(offsetLeft, offsetTop) / Point(clientWidth, clientHeight)
                    zoomBy(-delta.y / 150 * zoom, (e.point() - Point(offsetLeft, offsetTop)) / Point(clientWidth, clientHeight))
                } else {
                    panBy(-delta * 1.5)
                }
            }
        }
    }

    init {
        html.addEventListener("mousedown", moveStartListener)
        html.addEventListener("touchstart", moveStartListener)
        html.addEventListener("wheel", scrollListener)

        onClick {
            onSelect.emit(null)
        }

        async {
            resize()
        }

        window.addEventListener("resize", object : EventListener {
            override fun handleEvent(event: Event) {
                resize()
            }
        })
    }

    companion object {

        /**
         * List of zoom steps for stepped zooming.
         */
        val zoomSteps = listOf(0.1, 0.3, 0.5, 0.67, 0.8, 0.9, 1.0, 1.1, 1.2, 1.33, 1.5, 1.7, 2.0, 2.4, 3.0, 4.0)
        const val GRID_SIZE = 16
        const val GRID_STEP = 4
    }
}
