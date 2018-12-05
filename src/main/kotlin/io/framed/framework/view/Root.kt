package io.framed.framework.view

import io.framed.framework.util.Point
import io.framed.framework.util.point
import org.w3c.dom.HTMLElement
import kotlin.browser.document

/**
 * @author lars
 */
object Root : ViewCollection<View<*>, HTMLElement>(document.body!!) {

    var mousePosition: Point = Point.ZERO

    init {
        onMouseMove {
            mousePosition = it.point()
        }

        onKeyDown { event ->
            val found = shortcuts.filterKeys { it.match(event) }
            if (found.isNotEmpty()) {
                event.stopPropagation()
                event.preventDefault()
                found.values.forEach { println(5);it() }
            }
        }
    }

    private var shortcuts: Map<Shortcut, () -> Unit> = emptyMap()

    fun shortcut(shortcut: Shortcut, action: () -> Unit) {
        shortcuts += shortcut to action
    }
}