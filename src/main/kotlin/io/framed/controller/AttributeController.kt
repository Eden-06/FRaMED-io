package io.framed.controller

import io.framed.model.Attribute
import io.framed.view.InputView
import io.framed.view.MaterialIcon
import io.framed.view.View
import io.framed.view.contextMenu

/**
 * @author lars
 */
class AttributeController(
        val attribute: Attribute,
        val parent: ClassController
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

        view.context.on {
            it.stopPropagation()
            contextMenu {
                title = "Attribute: " + attribute.name
                addItem(MaterialIcon.DELETE, "Delete") {
                    parent.removeAttribute(attribute)
                }
            }.open(it.clientX.toDouble(), it.clientY.toDouble())
        }
    }
}