package io.framed.controller

import io.framed.model.Method
import io.framed.view.InputView
import io.framed.view.MaterialIcon
import io.framed.view.View
import io.framed.view.contextMenu

/**
 * @author lars
 */
class MethodController(
        val method: Method
) : Controller {

    override val view: View<*>
        get() = inputView

    private val inputView = InputView()

    init {
        inputView.classes += "method-view"

        inputView.value = method.toString()
        inputView.change.on {
            val split = it.trim().substring(1).trim()
                    .split(";", limit = 2)
                    .map { it.trim() }

            method.name = split[0]
            if (split.size == 2) {
                method.type = split[1]
            }
        }


        view.context.on {
            it.stopPropagation()
            contextMenu {
                title = "Method: " + method.name
                addItem(MaterialIcon.DELETE, "Delete") {

                }
            }.open(it.clientX.toDouble(), it.clientY.toDouble())
        }
    }
}