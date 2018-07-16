package io.framed.controller

import io.framed.model.Attribute
import io.framed.model.Visibility
import io.framed.view.TextView
import io.framed.view.View

/**
 * @author lars
 */
class AttributeController(
        val attribute: Attribute
) : Controller {

    override val view: View<*>
        get() = textView

    private val textView = TextView()

    init {
        textView.classes += "attribute-view"
        textView.contentEditable = true
        textView.singleLine = true

        textView.text = attribute.toString()
        textView.change.on {
            val line = it.trim()
            attribute.visibility = Visibility.parse(line[0])
            val split = (if (attribute.visibility == Visibility.NONE) line else line.substring(1).trim())
                    .split(";", limit = 2)
                    .map { it.trim() }

            attribute.name = split[0]
            if (split.size == 2) {
                attribute.type = split[1]
            }
        }
    }
}