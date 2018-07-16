package io.framed

import org.w3c.dom.Element

/**
 * @author lars
 */

external class jsPlumb {
    companion object {
        fun getInstance(): JsPlumbInstance
    }
}

external class JsPlumbInstance {
    fun setContainer(element: Element)
    fun connect(data: dynamic)
}

fun obj(init: dynamic.() -> Unit): dynamic {
    val d = js("{}")
    init(d)
    return d
}

fun arr(vararg item: dynamic): dynamic {
    val d = js("[]")
    item.forEach {
        d.push(it)
    }
    return d
}