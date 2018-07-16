package io.framed.controller

import io.framed.model.Method
import io.framed.model.Visibility
import io.framed.view.TextView
import io.framed.view.View

/**
 * @author lars
 */
class MethodController(
        val method: Method
) : Controller {

    override val view: View<*>
        get() = textView

    private val textView = TextView()

    init {
        textView.classes += "method-view"
        textView.contentEditable = true
        textView.singleLine = true

        textView.text = method.toString()
        textView.change.on {
            val line = it.trim()
            method.visibility = Visibility.parse(line[0])
            val split = (if (method.visibility == Visibility.NONE) line else line.substring(1).trim())
                    .split(";", limit = 2)
                    .map { it.trim() }

            method.name = split[0]
            if (split.size == 2) {
                method.type = split[1]
            }
        }
    }
}