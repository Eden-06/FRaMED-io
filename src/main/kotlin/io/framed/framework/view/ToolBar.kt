package io.framed.framework.view

import org.w3c.dom.HTMLDivElement

class ToolBar : ViewCollection<View<*>, HTMLDivElement>("div") {

    private val leftBar = listView {
        classes += "left-bar"
    }
    private val rightBar = listView {
        classes += "right-bar"
    }

    fun action(side: Side, icon: Icon, onAction: (IconView) -> Unit): IconView = when (side) {
        Side.LEFT -> leftBar
        Side.RIGHT -> rightBar
    }.iconView(icon) {
        onClick { onAction(this) }
    }

    fun separator() {
        //TODO
    }

    enum class Side {
        LEFT, RIGHT
    }
}

fun ViewCollection<in ToolBar, *>.toolBar(init: ToolBar.() -> Unit) =
        ToolBar().also(this::append).also(init)