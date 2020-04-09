package io.framed.framework.view

import de.westermann.kobserve.ReadOnlyProperty
import de.westermann.kobserve.event.EventHandler
import de.westermann.kobserve.property.FunctionAccessor
import de.westermann.kobserve.property.property
import io.framed.framework.pictogram.Shape
import io.framed.framework.util.*
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.KeyboardEvent
import org.w3c.dom.events.MouseEvent
import org.w3c.dom.get
import org.w3c.dom.set
import kotlin.browser.document
import kotlin.dom.addClass
import kotlin.dom.removeClass

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
     * The representing html element.
     */
    val html: V = view.also { view ->
        this::class.simpleName?.let { name ->
            view.classList.add(name.toDashCase())
        }
    }

    var assignedShape: Shape? = null

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

    var id by AttributeDelegate(String::class, "")

    /**
     * Show or hide this view. Does only work for view who are display by default.
     */
    val displayProperty = property(object : FunctionAccessor<Boolean> {
        override fun set(value: Boolean): Boolean {
            if (value) {
                html.style.removeProperty("display")
            } else {
                html.style.display = "none"
            }
            return true
        }

        override fun get(): Boolean = html.style.display != "none"
    })
    var display: Boolean by displayProperty

    val hiddenProperty = property(object : FunctionAccessor<Boolean> {
        override fun set(value: Boolean): Boolean {
            if (value) {
                html.style.visibility = "hidden"
            } else {
                html.style.removeProperty("visibility")
            }
            return true
        }

        override fun get(): Boolean = html.style.visibility == "hidden"
    })
    var hidden: Boolean by hiddenProperty

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

    fun bindCssClass(cssClass: String, property: ReadOnlyProperty<Boolean>) {
        classes[cssClass] = property.value
        property.onChange {
            classes[cssClass] = property.value
        }
    }

    val dimension: Dimension
        get() = Dimension(left, top, width, height)


    /**
     * Fires on onClick.
     */
    val onClick = EventHandler<MouseEvent>()

    /**
     * Fires on onContextMenu menu open.
     */
    val onContext = EventHandler<MouseEvent>()

    val onMouseDown = EventHandler<MouseEvent>()
    val onMouseMove = EventHandler<MouseEvent>()
    val onMouseUp = EventHandler<MouseEvent>()
    val onMouseEnter = EventHandler<MouseEvent>()
    val onMouseLeave = EventHandler<MouseEvent>()

    val onKeyDown = EventHandler<KeyboardEvent>()
    val onKeyPress = EventHandler<KeyboardEvent>()
    val onKeyUp = EventHandler<KeyboardEvent>()
    val onDblClick = EventHandler<MouseEvent>()

    val isMouseDownProperty by ClassDelegate("mouse-down")
    var isMouseDown by isMouseDownProperty

    val selectedViewProperty by ClassDelegate()
    var selectedView by selectedViewProperty

    val highlightedViewProperty by ClassDelegate()
    var highlightedView by highlightedViewProperty

    var allowDrag = false
    var disableDrag = false

    val onDrag = EventHandler<DragEvent>()

    var minTop: Double? = null
    var minLeft: Double? = null

    fun performDrag(dragEvent: DragEvent) {
        if (!allowDrag) {
            throw IllegalStateException()
        }
        onDrag.emit(dragEvent)
    }

    var dragZoom = 1.0
    var draggable by AttributeDelegate(Boolean::class, false)

    var tooltip: String? = null
        set(value) {
            field = value

            if (value == null) {
                html.removeAttribute("title")
            } else {
                html.setAttribute("title", value)
            }
        }

    fun click() {
        html.click()
    }

    fun preventDrag() {
        html.dataset["prevent"] = "true"
    }

    init {
        onClick.bind(html, "click")
        onContext.bind(html, "contextmenu")
        onMouseDown.bind(html, "mousedown")
        onMouseMove.bind(html, "mousemove")
        onMouseUp.bind(html, "mouseup")
        onMouseEnter.bind(html, "mouseenter")
        onMouseLeave.bind(html, "mouseleave")
        onDblClick.bind(html, "dblclick")
        onKeyDown.bind(html, "keydown")
        onKeyPress.bind(html, "keypress")
        onKeyUp.bind(html, "keyup")

        onContext {
            it.preventDefault()
        }

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
        dragEnd = {
            removeDrag()
        }

        onMouseDown {
            isMouseDown = true

            var prevented = false

            var h = it.target as? HTMLElement
            while (h != null) {
                if (h?.dataset?.get("prevent") == "true") {
                    prevented = true
                    break
                }
                h = h?.parentElement as? HTMLElement
            }
            if (allowDrag && !prevented) {
                it.stopPropagation()

                Root.onMouseUp += dragEnd
                Root.onMouseLeave += dragEnd
            }
        }

        onMouseUp {
            isMouseDown = false
        }

        onMouseMove {
            val added = dragEnd in Root.onMouseUp
            if (isMouseDown && !isCurrentlyDragging && allowDrag && added) {
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

        var disableDrag: Boolean = false
            set(value) {
                field = value
                Root.classes["no-select"] = value
            }

        /**
         * Create html element by tag name.
         */
        @Suppress("UNCHECKED_CAST")
        fun <V : HTMLElement> createView(tagName: String): V = document.createElement(tagName) as V
    }

    data class DragEvent(
            val delta: Point,
            val direct: Boolean
    ) {
        val indirect: DragEvent
            get() = copy(direct = false)
    }
}