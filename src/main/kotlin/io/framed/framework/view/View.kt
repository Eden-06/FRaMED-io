package io.framed.framework.view

import de.westermann.kobserve.EventHandler
import io.framed.framework.util.*
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.KeyboardEvent
import org.w3c.dom.events.MouseEvent
import kotlin.browser.document
import kotlin.math.max
import kotlin.reflect.KClass

/**
 * Base class for html views. Each view is represented by a html element.
 *
 * @param view The html element.
 *
 * @author lars
 */
abstract class View<V : HTMLElement>(view: V) {

    /**
     * @param tagName The html tag name for this view.
     */
    constructor(tagName: String) : this(createView<V>(tagName))

    /**
     * @param type The reflected model of the html element.
     */
    constructor(type: KClass<V>) : this(createView(type))

    /**
     * The representing html element.
     */
    val html: V = view.also { view ->
        this::class.simpleName?.let { name ->
            view.classList.add(name.toDashCase())
        }
    }

    /**

    data class DropEvent(
    val target: HTMLElement,
    val element: HTMLElement,
    val direct: Boolean
    ) {
    val indirect: DropEvent
    get() = copy(direct = false)
    }

    data class DragOverEvent(
    val target: HTMLElement,
    val element: HTMLElement,
    val direct: Boolean
    ) {
    val indirect: DragOverEvent
    get() = copy(direct = false)
    }
     * Fires on onClick.
     */
    val onClick = EventHandler<MouseEvent>()

    /**
     * Fires on onContextMenu menu open.
     */
    val onContext = EventHandler<MouseEvent>()

    /**
     * Access css classes of this view.
     */
    val classes = ClassList(html.classList)

    /**
     * Current calculated width in px.
     */
    val clientWidth: Int
        get() = html.clientWidth

    /**
     * Current calculated height in px.
     */
    val clientHeight: Int
        get() = html.clientHeight

    /**
     * Css top position in px.
     */
    var top: Double
        get() = html.style.top.replace("px", "").toDoubleOrNull() ?: 0.0
        set(value) {
            html.style.top = "${value}px"
        }

    /**
     * Css left position in px.
     */
    var left: Double
        get() = html.style.left.replace("px", "").toDoubleOrNull() ?: 0.0
        set(value) {
            html.style.left = "${value}px"
        }

    /**
     * Css right position in px.
     */
    var right: Double
        get() = html.style.right.replace("px", "").toDoubleOrNull() ?: 0.0
        set(value) {
            html.style.right = "${value}px"
        }

    /**
     * Css margin top in px.
     */
    var marginTop: Double
        get() = html.style.marginTop.replace("px", "").toDoubleOrNull() ?: 0.0
        set(value) {
            html.style.marginTop = "${value}px"
        }

    /**
     * Css margin left in px.
     */
    var marginLeft: Double
        get() = html.style.marginLeft.replace("px", "").toDoubleOrNull() ?: 0.0
        set(value) {
            html.style.marginLeft = "${value}px"
        }

    /**
     * Css width in px.
     */
    var width: Double
        get() = html.style.width.replace("px", "").toDoubleOrNull() ?: clientWidth.toDouble()
        set(value) {
            html.style.width = "${value}px"
        }

    /**
     * Css height in px.
     */
    var height: Double
        get() = html.style.height.replace("px", "").toDoubleOrNull() ?: clientHeight.toDouble()
        set(value) {
            html.style.height = "${value}px"
        }
    var zIndex: Int?
        get() = html.style.zIndex.toIntOrNull()
        set(value) {
            if (value == null) {
                html.style.removeProperty("z-index")
            } else {
                html.style.zIndex = value.toString()
            }
        }

    fun autoWidth() {
        html.style.removeProperty("width")
    }

    fun autoHeight() {
        html.style.removeProperty("height")
    }

    /**
     * Accumulated offset top in px.
     */
    val offsetTop: Int
        get() {
            var offset = html.offsetTop
            var h = html.offsetParent as? HTMLElement
            while (h != null) {
                offset += h.offsetTop
                h = h.offsetParent as? HTMLElement
            }

            return offset
        }

    /**
     * Accumulated offset left in px.
     */
    val offsetLeft: Int
        get() {
            var offset = html.offsetLeft
            var h = html.offsetParent as? HTMLElement
            while (h != null) {
                offset += h.offsetLeft
                h = h.offsetParent as? HTMLElement
            }

            return offset
        }

    val id by AttributeDelegate(String::class, "")

    /**
     * Show or hide this view. Does only work for view who are visible by default.
     */
    var visible: Boolean
        get() = html.style.display != "none"
        set(value) {
            if (value) {
                html.style.removeProperty("display")
            } else {
                html.style.display = "none"
            }
        }

    var hiddenVisibility: Boolean
        get() = html.style.visibility == "hidden"
        set(value) {
            if (value) {
                html.style.visibility = "hidden"
            } else {
                html.style.removeProperty("visibility")
            }
        }

    /**
     * Request focus to this view.
     */
    open fun focus() {
        html.focus()
    }

    /**
     * Revoke focus from this view.
     */
    open fun blur() {
        html.blur()
    }

    val dimension: Dimension
        get() = Dimension(left, top, width, height)

    val onMouseDown = EventHandler<MouseEvent>()
    val onMouseMove = EventHandler<MouseEvent>()
    val onMouseUp = EventHandler<MouseEvent>()
    val onMouseEnter = EventHandler<MouseEvent>()
    val onMouseLeave = EventHandler<MouseEvent>()

    val onKeyDown = EventHandler<KeyboardEvent>()
    val onKeyPress = EventHandler<KeyboardEvent>()
    val onKeyUp = EventHandler<KeyboardEvent>()
    val onDblClick = EventHandler<MouseEvent>()

    var isMouseDown by ClassDelegate("mouse-down")
    var selectedView by ClassDelegate("selected-view")

    var dragType = DragType.NONE
    val onDrag = EventHandler<DragEvent>()

    var minTop: Double? = null
    var minLeft: Double? = null

    fun performDrag(dragEvent: DragEvent) {
        val newPosition = when (dragType) {
            View.DragType.NONE -> throw IllegalStateException()
            View.DragType.CUSTOM -> Point(dragEvent.delta.x, dragEvent.delta.y)
            View.DragType.ABSOLUTE -> {
                val newLeft = left + dragEvent.delta.x
                val newTop = top + dragEvent.delta.y

                left = minLeft?.let { max(it, newLeft) } ?: newLeft
                top = minTop?.let { max(it, newTop) } ?: newTop
                Point(left, top)
            }
            View.DragType.MARGIN -> {
                marginLeft += dragEvent.delta.x
                marginTop += dragEvent.delta.y
                Point(marginLeft, marginTop)
            }
        }
        onDrag.emit(dragEvent.copy(newPosition = newPosition))
    }

    var dragZoom = 1.0
    var draggable by AttributeDelegate(Boolean::class, false)

    init {
        html.addEventListener("click", onClick.eventListener())
        html.addEventListener("contextmenu", onContext.eventListener())
        onContext {
            it.preventDefault()
        }
        html.addEventListener("mousedown", onMouseDown.eventListener())
        html.addEventListener("mousemove", onMouseMove.eventListener())
        html.addEventListener("mouseup", onMouseUp.eventListener())
        html.addEventListener("mouseenter", onMouseEnter.eventListener())
        html.addEventListener("mouseleave", onMouseLeave.eventListener())
        html.addEventListener("dblclick", onDblClick.eventListener())
        html.addEventListener("keydown", onKeyDown.eventListener())
        html.addEventListener("keypress", onKeyPress.eventListener())
        html.addEventListener("keyup", onKeyUp.eventListener())

        var isCurrentlyDragging = false
        var lastDragPosition = Point.ZERO
        val dragMove = { event: MouseEvent ->
            val delta = (event.point() - lastDragPosition) / dragZoom
            performDrag(DragEvent(delta, true))
            lastDragPosition = event.point()
        }

        var dragEnd: (MouseEvent) -> Unit = {}
        fun removeDrag() {
            isCurrentlyDragging = false
            isMouseDown = false

            Root.onMouseMove -= dragMove
            Root.onMouseUp -= dragEnd
            Root.onMouseLeave -= dragEnd
        }
        dragEnd = { _: MouseEvent ->
            removeDrag()
        }

        onMouseDown {
            isMouseDown = true

            if (dragType != DragType.NONE) {
                it.stopPropagation()

                Root.onMouseUp += dragEnd
                Root.onMouseLeave += dragEnd
            }
        }
        onMouseUp {
            isMouseDown = false
        }

        onMouseMove {
            if (isMouseDown && !isCurrentlyDragging && dragType != DragType.NONE) {
                it.preventDefault()
                it.stopPropagation()

                lastDragPosition = it.point()
                Root.onMouseMove += dragMove
                Root.onMouseUp += dragEnd
                Root.onMouseLeave += dragEnd

                isCurrentlyDragging = true
            }
        }
    }

    companion object {

        /**
         * Create html element by generic model.
         */
        @Suppress("UNCHECKED_CAST")
        inline fun <reified V : HTMLElement> createView(): V = createView(V::class)

        /**
         * Create html element by reflected model class.
         */
        @Suppress("UNCHECKED_CAST")
        fun <V : HTMLElement> createView(type: KClass<V>): V =
                document.createElement(
                        type.simpleName?.replace("HTML|Element".toRegex(), "")?.toLowerCase()
                                ?: "div"
                ) as V

        /**
         * Create html element by tag name.
         */
        @Suppress("UNCHECKED_CAST")
        fun <V : HTMLElement> createView(tagName: String): V = document.createElement(tagName) as V
    }

    enum class DragType {
        NONE,
        ABSOLUTE,
        MARGIN,
        CUSTOM
    }

    data class DragEvent(
            val delta: Point,
            val direct: Boolean,
            val newPosition: Point = Point.ZERO
    ) {
        val indirect: DragEvent
            get() = copy(direct = false)
    }
}