@file:Suppress("UnsafeCastFromDynamic")

package io.framed.framework

import org.w3c.dom.Element
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event
import org.w3c.dom.events.MouseEvent
import org.w3c.dom.svg.SVGSVGElement

/**
 * Typed headers for jsPlumb.
 *
 * @author lars
 */

/**
 * jsPlumb root object.
 */
@JsName("jsPlumb")
external class JsPlumb {
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
     * Bind an model on the instance.
     */
    fun bind(event: String, function: (JsPlumbDropInfoInit) -> dynamic): dynamic

    /**
     * Set the model for drawing.
     *
     * @param element Container for drawing.
     */
    fun setContainer(element: Element)

    /**
     * Create a new connection.
     *
     * @param data Connection properties.
     *
     * @return Connection instance.
     */
    fun connect(data: JsPlumbConnectInit): JsPlumbConnection

    /**
     * Remove a connection.
     *
     * @param connection Connection instance.
     */
    fun deleteConnection(connection: JsPlumbConnection)

    /**
     * Make an element draggable.
     *
     * @param element Element to dragType.
     */
    fun draggable(element: Element)

    /**
     * Set the current zoom for correct dragType functionality.
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

    /**
     *
     */
    fun addEndpoint(target: HTMLElement, options: JsPlumbEndpointOptionsInit): HTMLElement

    /**
     *
     */
    fun deleteEndpoint(target: HTMLElement)

    fun deleteEveryConnection()
    fun deleteEveryEndpoint()

    fun makeTarget(element: HTMLElement, options: JsPlumbTargetOptionsInit)
    fun makeSource(element: HTMLElement, options: JsPlumbSourceOptionsInit)

    fun isTarget(element: HTMLElement):Boolean
    fun isSource(element: HTMLElement):Boolean

    fun setTargetEnabled(element: HTMLElement)
    fun setSourceEnabled(element: HTMLElement)

    fun isTargetEnabled(element: HTMLElement): Boolean
    fun isSourceEnabled(element: HTMLElement): Boolean

    fun unmakeTarget(element: HTMLElement)
    fun unmakeSource(element: HTMLElement)

    fun unmakeEveryTarget()
    fun unmakeEverySource()

}

/**
 * jsPlumb connection init properties.
 */
external interface JsPlumbConnectInit {

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
    var anchors: Array<Array<Any>>

    /**
     * Connector data.
     */
    var connector: Array<Any>

    /**
     * Overlays (Arrows ...).
     */
    var overlays: Array<Any>

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
fun jsPlumbConnect(init: JsPlumbConnectInit.() -> Unit = {}): JsPlumbConnectInit {
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

    var fill: String
}

/**
 * Create a new jsPlumb line style property object.
 *
 * @param init Optional builder callback for this line style.
 *
 * @return The new line style property object.
 */
fun jsPlumbPaintStyle(init: JsPlumbPaintStyle.() -> Unit = {}): JsPlumbPaintStyle {
    val h = js("{}")
    init(h)
    return h
}

/**
 * jsPlumb connection instance
 */
external interface JsPlumbConnection {
    val canvas: SVGSVGElement

    fun bind(event: String, listener: (JsPlumbConnection, Event) -> Unit)
}
/**
 *
 */
external interface JsPlumbDropInfoInit {
    var sourceId: String
    var targetId: String
    var connection: JsPlumbConnection
}

/**
 *
 */
external interface JsPlumbEndpointOptionsInit {
    var isSource: Boolean
    var isTarget: Boolean
    var anchors: Array<String>
    var endpoint: String
    var dropOptions: JsPlumbDropOptionsInit
    var dragOptions: JsPlumbDragOptionsInit
}

/**
 *
 */
fun jsPlumbEndpointOptions(init: JsPlumbEndpointOptionsInit.() -> Unit = {}): JsPlumbEndpointOptionsInit {
    val h = js("{}")
    init(h)
    return h
}
/**
 *
 */
external interface JsPlumbDropOptionsInit {
    var drop: () -> Unit
}
/**
 *
 */
external interface JsPlumbDragOptionsInit {
    var drag: (dynamic, dynamic) -> Unit
}


/**
 *
 */
external interface JsPlumbTargetOptionsInit {
    var allowLoopback: Boolean
}

/**
 *
 */
fun jsPlumbTargetOptionsInit(init: JsPlumbTargetOptionsInit.() -> Unit = {}): JsPlumbTargetOptionsInit {
    val h = js("{}")
    init(h)
    return h
}

/**
 *
 */
external interface JsPlumbSourceOptionsInit {
    var filter: (MouseEvent, HTMLElement) -> Boolean
    var filterExclude: Boolean
}

/**
 *
 */
fun jsPlumbSourceOptionsInit(init: JsPlumbSourceOptionsInit.() -> Unit = {}): JsPlumbSourceOptionsInit {
    val h = js("{}")
    init(h)
    return h
}