package io.framed.view

import io.framed.toDashCase
import io.framed.util.EventHandler
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.KeyboardEvent
import org.w3c.dom.events.MouseEvent
import kotlin.browser.document
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
     * @param type The reflected type of the html element.
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
     * Fires on onClick.
     */
    val onClick = EventHandler<MouseEvent>()

    /**
     * Fires on onContext menu open.
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
    fun focus() {
        html.focus()
    }

    /**
     * Revoke focus from this view.
     */
    fun blur() {
        html.blur()
    }

    val onMouseDown = EventHandler<MouseEvent>()
    val onMouseMove = EventHandler<MouseEvent>()
    val onMouseUp = EventHandler<MouseEvent>()
    val onMouseEnter = EventHandler<MouseEvent>()
    val onMouseLeave = EventHandler<MouseEvent>()

    val onKeyDown = EventHandler<KeyboardEvent>()
    val onKeyPress = EventHandler<KeyboardEvent>()
    val onKeyUp = EventHandler<KeyboardEvent>()

    var isMouseDown by ClassDelegate("mouse-down")

    init {
        html.addEventListener("click", onClick.eventListener)
        html.addEventListener("contextmenu", onContext.eventListener)
        onContext {
            it.preventDefault()
        }

        html.addEventListener("mousedown", onMouseDown.eventListener)
        html.addEventListener("mousemove", onMouseMove.eventListener)
        html.addEventListener("mouseup", onMouseUp.eventListener)
        html.addEventListener("mouseenter", onMouseEnter.eventListener)
        html.addEventListener("mouseleave", onMouseLeave.eventListener)

        html.addEventListener("keydown", onKeyDown.eventListener)
        html.addEventListener("keypress", onKeyPress.eventListener)
        html.addEventListener("keyup", onKeyUp.eventListener)

        onMouseDown {
            isMouseDown = true
        }
        onMouseUp {
            isMouseDown = false
        }
    }

    companion object {

        /**
         * Create html element by generic type.
         */
        @Suppress("UNCHECKED_CAST")
        inline fun <reified V : HTMLElement> createView(): V = createView(V::class)

        /**
         * Create html element by reflected type class.
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
}