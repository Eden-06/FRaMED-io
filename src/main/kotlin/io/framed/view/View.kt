package io.framed.view

import io.framed.toDashCase
import io.framed.util.EventHandler
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event
import org.w3c.dom.events.EventListener
import kotlin.browser.document
import kotlin.reflect.KClass

/**
 * @author lars
 */
abstract class View<V : HTMLElement>(view: V) {

    constructor(tagName: String) : this(createView<V>(tagName))
    constructor(type: KClass<V>) : this(createView(type))

    val html: V = view.also { view ->
        this::class.simpleName?.let { name ->
            view.classList.add(name.toDashCase())
        }
    }

    /**
     * Fires on every click
     */
    val click = EventHandler<Unit>()

    /**
     * Access css classes of this view
     */
    val classes = ClassList(html.classList)

    val clientWidth: Int
        get() = html.clientWidth
    val clientHeight: Int
        get() = html.clientHeight

    var top: Double
        get() = html.style.top.replace("px","").toDoubleOrNull() ?: 0.0
        set(value) {
            html.style.top = "${value}px"
        }
    var left: Double
        get() = html.style.left.replace("px","").toDoubleOrNull() ?: 0.0
        set(value) {
            html.style.left = "${value}px"
        }
    var width: Double
        get() = html.style.width.replace("px","").toDoubleOrNull() ?: clientWidth.toDouble()
        set(value) {
            html.style.width = "${value}px"
        }
    var height: Double
        get() = html.style.height.replace("px","").toDoubleOrNull() ?: clientHeight.toDouble()
        set(value) {
            html.style.height = "${value}px"
        }

    /**
     * Request focus to this view
     */
    fun focus() {
        html.focus()
    }

    /**
     * Revoke focus off this view
     */
    fun blur() {
        html.blur()
    }

    init {
        html.addEventListener("click", object : EventListener {
            override fun handleEvent(event: Event) {
                click.fire(Unit)
            }
        })
    }

    companion object {

        @Suppress("UNCHECKED_CAST")
        inline fun <reified V : HTMLElement> createView(): V = createView(V::class)

        @Suppress("UNCHECKED_CAST")
        fun <V : HTMLElement> createView(type: KClass<V>): V =
                document.createElement(
                        type.simpleName?.replace("HTML|Element".toRegex(), "")?.toLowerCase()
                                ?: "div"
                ) as V

        @Suppress("UNCHECKED_CAST")
        fun <V : HTMLElement> createView(tagName: String): V = document.createElement(tagName) as V
    }

    data class DragEvent(
            val deltaX: Int,
            val deltaY: Int
    )
}