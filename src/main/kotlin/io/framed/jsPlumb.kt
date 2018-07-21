package io.framed

import org.w3c.dom.Element
import org.w3c.dom.HTMLElement

/**
 * Typed headers for jsPlumb.
 *
 * @author lars
 */

/**
 * jsPlumb root object.
 */
external class jsPlumb {
    companion object {
        /**
         * Create a new jsPlumb instance.
         *
         * @return New jsPlumb instance.
         */
        fun getInstance(): JsPlumbInstance
    }
}

/**
 * jsPlumb instance
 */
external interface JsPlumbInstance {
    /**
     * Set the container for drawing.
     *
     * @param element Container for drawing.
     */
    fun setContainer(element: Element)

    /**
     * Create a new connection.
     *
     * @param data Connection properties.
     */
    fun connect(data: JsPlumbConnect)

    /**
     * Make an element draggable.
     *
     * @param element Element to drag.
     */
    fun draggable(element: Element)

    /**
     * Set the current zoom for correct drag functionality.
     *
     * @param zoom The current zoom.
     */
    fun setZoom(zoom: Double)

    /**
     * Redraw assigned connections.
     *
     * @param element Reference element.
     */
    fun revalidate(element: Element)

    /**
     * Redraw all connections.
     */
    fun repaintEverything()
}

/**
 * jsPlumb connection properties.
 */
external interface JsPlumbConnect {

    /**
     * Source dom element.
     */
    var source: HTMLElement

    /**
     * Target dom element.
     */
    var target: HTMLElement

    /**
     * List of anchors.
     */
    var anchor: Array<Any>

    /**
     * Connector data.
     */
    var connector: Array<Any>

    /**
     * Endpoint style.
     */
    var endpoint: String

    /**
     * Line style.
     */
    var paintStyle: JsPlumbPaintStyle
}

/**
 * Create a new jsPlumb connection property object.
 *
 * @param init Optional builder callback for this connection.
 *
 * @return The new connection property object.
 */
fun JsPlumbConnect(init: JsPlumbConnect.() -> Unit = {}): JsPlumbConnect {
    val h = js("{}")
    init(h)
    return h
}

/**
 * jsPlumb line style properties.
 */
external interface JsPlumbPaintStyle {

    /**
     * Line color.
     */
    var stroke: String

    /**
     * Line width.
     */
    var strokeWidth: Number

    /**
     * Dash style.
     */
    var dashstyle: String
}

/**
 * Create a new jsPlumb line style property object.
 *
 * @param init Optional builder callback for this line style.
 *
 * @return The new line style property object.
 */
fun JsPlumbPaintStyle(init: JsPlumbPaintStyle.() -> Unit = {}): JsPlumbPaintStyle {
    val h = js("{}")
    init(h)
    return h
}