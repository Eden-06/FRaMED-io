package io.framed.controller

import io.framed.model.Attribute
import io.framed.view.InputView
import io.framed.view.View

/**
 * @author lars
 */
class AttributeController(
        val attribute: Attribute
) : Controller {

    override val view: View<*>
        get() = inputView

    private val inputView = InputView()

    init {
        inputView.classes += "attribute-view"

        inputView.value = attribute.toString()
        inputView.change.on {
            val split = it.trim().split(";", limit = 2)
                    .map { it.trim() }

            attribute.name = split[0]
            if (split.size == 2) {
                attribute.type = split[1]
            }
        }
    }
}