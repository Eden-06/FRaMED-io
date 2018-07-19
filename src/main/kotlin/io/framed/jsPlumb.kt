package io.framed

import org.w3c.dom.Element
import org.w3c.dom.HTMLElement

/**
 * @author lars
 */

external class jsPlumb {
    companion object {
        fun getInstance(): JsPlumbInstance
    }
}

external interface JsPlumbInstance {
    fun setContainer(element: Element)
    fun connect(data: JsPlumbConnect)
    fun draggable(element: Element)
    fun setZoom(zoom: Double)

    fun revalidate(element: Element)
    fun repaintEverything()
}

external interface JsPlumbConnect {
    var source: HTMLElement
    var target: HTMLElement
    var anchor: Array<Any>
    var connector: Array<Any>
    var endpoint: String
    var paintStyle: JsPlumbPaintStyle
}

fun JsPlumbConnect(init: JsPlumbConnect.() -> Unit = {}): JsPlumbConnect {
    val h = js("{}")
    init(h)
    return h
}

external interface JsPlumbPaintStyle {
    var stroke: String
    var strokeWidth: Number
    var dashstyle: String
}

fun JsPlumbPaintStyle(init: JsPlumbPaintStyle.() -> Unit = {}): JsPlumbPaintStyle {
    val h = js("{}")
    init(h)
    return h
}