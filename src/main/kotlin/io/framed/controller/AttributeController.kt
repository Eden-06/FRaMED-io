package io.framed.controller

import io.framed.model.Attribute
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
        textView.text = attribute.toString()
    }
}