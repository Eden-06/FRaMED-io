package io.framed.framework.view

import io.framed.framework.util.Point
import io.framed.framework.util.point
import org.w3c.dom.HTMLElement
import kotlinx.browser.document

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
            if ((event.target as? HTMLElement)?.tagName == "INPUT") {
                return@onKeyDown
            }
            val found = shortcutMap.filterKeys { it.match(event) }
            if (found.isNotEmpty()) {
                event.stopPropagation()
                event.preventDefault()
                found.values.forEach { it() }
            }
        }
    }

    private var shortcutMap: Map<Shortcut, () -> Unit> = emptyMap()

    val shortcuts: Set<Shortcut>
        get() = shortcutMap.keys

    fun shortcut(shortcut: Shortcut, action: () -> Unit) {
        if (shortcut !in shortcutMap) {
            shortcutMap += shortcut to action
        }
    }
}
