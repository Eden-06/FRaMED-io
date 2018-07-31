package io.framed.controller

import io.framed.model.Attribute
import io.framed.util.EventHandler
import io.framed.view.*

/**
 * @author lars
 */
class AttributeController(
        val attribute: Attribute,
        val parent: ClassController
) : Controller {

    override val view: View<*>
        get() = inputView

    override val sidebar: Sidebar = parent.createSidebar()

    private val inputView = InputView()

    private val update = EventHandler<Int>()

    init {
        inputView.classes += "attribute-view"

        inputView.value = attribute.toString()
        inputView.change {
            inputView.invalid = parse(it) >= 0
            update.fire(1)
        }
        inputView.focusLeave {
            if (!inputView.invalid) {
                inputView.value = attribute.toString()
            }
        }
        update {
            if (it != 1) {
                inputView.value = attribute.toString()
                inputView.invalid = false
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

        sidebar.setup(view, inputView) {
            title("Attribute")
            input("Name", attribute.name) {
                attribute.name = it
                update.fire(0)
            }.also { i ->
                update {
                    if (it != 0)
                        i.value = attribute.name
                }
                i.focusLeave {
                    i.value = attribute.name
                }
            }
            input("Type", attribute.type) {
                attribute.type = it.trim()
                update.fire(0)
            }.also { i ->
                update {
                    if (it != 0)
                        i.value = attribute.type
                }
                i.focusLeave {
                    i.value = attribute.type
                }
            }
        }
    }

    private fun parse(input: String): Int {
        var state = State.NAME

        var name = ""
        var type = ""

        input.forEachIndexed { index, char ->
            state = when (state) {
                State.NAME -> {
                    when (char) {
                        ':' -> {
                            State.TYPE
                        }
                        '(', ')' -> return index
                        else -> {
                            name += char
                            State.NAME
                        }
                    }
                }
                State.TYPE -> {
                    when (char) {
                        ':', '(', ')' -> return index
                        else -> {
                            type += char
                            State.TYPE
                        }
                    }
                }
            }
        }

        attribute.name = name.trim()
        attribute.type = type.trim()

        return -1
    }

    private enum class State {
        NAME, TYPE
    }
}