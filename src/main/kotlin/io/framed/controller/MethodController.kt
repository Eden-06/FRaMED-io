package io.framed.controller

import io.framed.model.Attribute
import io.framed.model.Method
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
        textView.text = method.toString()
    }
}