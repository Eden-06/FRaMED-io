package io.framed.controller

import io.framed.model.Method
import io.framed.model.Parameter
import io.framed.model.param
import io.framed.util.EventHandler
import io.framed.util.point
import io.framed.view.*

/**
 * @author lars
 */
class MethodController(
        val method: Method,
        val parent: ClassController
) : Controller {

    override val view: View<*>
        get() = inputView

    override val sidebar: Sidebar = parent.createSidebar()
    private val parameterTable = TableView()

    private fun updateParameters() {
        parameterTable.clear()

        method.parameters.forEach { param ->
            parameterTable.row {
                cellBox {
                    inputView {
                        value = param.name
                        onChange {
                            param.name = it
                            update.fire(2)
                        }
                    }
                }
                cellBox { textView("") }
                cellBox {
                    inputView {
                        value = param.type
                        onChange {
                            param.type = it
                            update.fire(2)
                        }
                    }
                }
                cellBox {
                    iconView(MaterialIcon.CLEAR) {
                        onClick {
                            method.parameters -= param
                            update.fire(-1)
                        }
                    }
                }
            }
        }
    }

    private val inputView = InputView()

    private val update = EventHandler<Int>()

    init {
        inputView.classes += "method-view"

        inputView.value = method.toString()
        inputView.onChange {
            inputView.invalid = parse(it) >= 0
            update.fire(1)
        }
        inputView.onFocusLeave {
            if (!inputView.invalid) {
                inputView.value = method.toString()
            }
        }
        update {
            if (it != 1) {
                inputView.value = method.toString()
                inputView.invalid = false
            }
        }

        view.onContext {
            it.stopPropagation()
            contextMenu {
                title = "Method: " + method.name
                addItem(MaterialIcon.DELETE, "Delete") {
                    parent.removeMethod(method)
                }
            }.open(it.point())
        }

        sidebar.setup(view, inputView) {
            title("Method")
            input("Name", method.name) {
                method.name = it
                update.fire(0)
            }.also { i ->
                update {
                    if (it != 0)
                        i.value = method.name
                }
                i.onFocusLeave {
                    i.value = method.name
                }
            }
            input("Type", method.type) {
                method.type = it.trim()
                update.fire(0)
            }.also { i ->
                update {
                    if (it != 0)
                        i.value = method.type
                }
                i.onFocusLeave {
                    i.value = method.type
                }
            }

            custom {
                textView("Parameters")
                append(parameterTable)
                listView {
                    iconView(MaterialIcon.ADD)
                    textView("Add parameter")
                    onClick {
                        method.param("")
                        update.fire(-1)
                    }
                }
            }
        }

        updateParameters()
        update {
            if (it != 2)
                updateParameters()
        }
    }


    private fun parse(input: String): Int {
        var state = State.NAME

        var name = ""
        var type = ""
        var param = listOf<Pair<String, String>>()

        input.forEachIndexed { index, char ->
            state = when (state) {
                State.NAME -> {
                    when (char) {
                        ':' -> {
                            State.TYPE
                        }
                        '(' -> {
                            param += "" to ""
                            State.PARAM_NAME
                        }
                        ')' -> return index
                        else -> {
                            name += char
                            State.NAME
                        }
                    }
                }
                State.TYPE -> {
                    when (char) {
                        '(' -> {
                            param += "" to ""
                            State.PARAM_NAME
                        }
                        ':', ')' -> return index
                        else -> {
                            type += char
                            State.TYPE
                        }
                    }
                }
                State.PARAM_NAME -> {
                    when (char) {
                        ':' -> {
                            State.PARAM_TYPE
                        }
                        ',' -> {
                            param += "" to ""
                            State.PARAM_NAME
                        }
                        ')' -> {
                            State.AFTER_PARAM
                        }
                        '(' -> return index
                        else -> {
                            param = param.dropLast(1) + param.last().let {
                                it.first + char to it.second
                            }
                            State.PARAM_NAME
                        }
                    }
                }
                State.PARAM_TYPE -> {
                    when (char) {
                        ',' -> {
                            param += "" to ""
                            State.PARAM_NAME
                        }
                        ')' -> {
                            State.AFTER_PARAM
                        }
                        ':', '(' -> return index
                        else -> {
                            param = param.dropLast(1) + param.last().let {
                                it.first to it.second + char
                            }
                            State.PARAM_TYPE
                        }
                    }
                }
                State.AFTER_PARAM -> {
                    when (char) {
                        ' ' -> State.AFTER_PARAM
                        ':' -> State.TYPE
                        else -> return index
                    }
                }
            }
        }

        method.name = name.trim()
        method.type = type.trim()
        method.parameters = param.filter { it.first.isNotBlank() || it.first.isNotBlank() }.map {
            Parameter().apply {
                this.name = it.first.trim()
                this.type = it.second.trim()
            }
        }

        return -1
    }

    private enum class State {
        NAME, TYPE, PARAM_NAME, PARAM_TYPE, AFTER_PARAM
    }
}
