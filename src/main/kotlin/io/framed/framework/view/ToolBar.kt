package io.framed.framework.view

import org.w3c.dom.HTMLDivElement

class ToolBar : ViewCollection<View<*>, HTMLDivElement>("div") {

    private val leftBar = listView {
        classes += "left-bar"
    }
    private val rightBar = listView {
        classes += "right-bar"
    }

    fun action(side: Side, icon: Icon, tooltip: String? = null, onAction: (IconView) -> Unit): IconView = when (side) {
        Side.LEFT -> leftBar
        Side.RIGHT -> rightBar
    }.iconView(icon) {
        this.tooltip = tooltip
        onClick { onAction(this) }
    }

    fun custom(side: Side, init: ListView.() -> Unit) = when (side) {
        Side.LEFT -> leftBar
        Side.RIGHT -> rightBar
    }.also(init)


    fun separator(side: Side) = when (side) {
        Side.LEFT -> leftBar
        Side.RIGHT -> rightBar
    }.let { bar ->
        bar.last().classes += "separator"
    }

    enum class Side {
        LEFT, RIGHT
    }
}

fun ViewCollection<in ToolBar, *>.toolBar(init: ToolBar.() -> Unit) =
        ToolBar().also(this::append).also(init)