package io.framed.linker

import io.framed.framework.Linker
import io.framed.framework.LinkerInfoItem
import io.framed.framework.LinkerManager
import io.framed.framework.pictogram.ContextEvent
import io.framed.framework.pictogram.TextShape
import io.framed.framework.pictogram.textShape
import io.framed.framework.util.RegexValidator
import io.framed.framework.util.Validator
import io.framed.framework.util.property
import io.framed.framework.view.*
import io.framed.model.Method
import io.framed.model.Parameter
import io.framed.model.param

/**
 * @author lars
 */
class MethodLinker(
        override val model: Method,
        override val parent: ClassLinker
) : Linker<Method, TextShape> {

    private val nameProperty = property(model::name, RegexValidator("[a-zA-Z]([a-zA-Z0-9])*".toRegex()))

    private val typeProperty = property(model::type, RegexValidator("[a-zA-Z]([a-zA-Z0-9])*".toRegex()))

    private val parameterProperty = property(model::parameters)

    private val lineProperty = property(nameProperty, typeProperty, parameterProperty,
            getter = {
                "${model.name}(" + model.parameters.joinToString(", ") { it.toString() } + ")" + model.type.let {
                    if (it.isBlank()) "" else ": $it"
                }.trim()
            },
            setter = { input ->

                var state = State.NAME

                var name = ""
                var type = ""
                var param = listOf<Pair<String, String>>()

                input.forEach { char ->
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
                                ')' -> return@property Validator.Result.ERROR
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
                                ':', ')' -> return@property Validator.Result.ERROR
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
                                '(' -> return@property Validator.Result.ERROR
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
                                ':', '(' -> return@property Validator.Result.ERROR
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
                                else -> return@property Validator.Result.ERROR
                            }
                        }
                    }
                }

                model.name = name.trim()
                model.type = type.trim()
                model.parameters = param.asSequence().filter { it.first.isNotBlank() || it.first.isNotBlank() }.map {
                    Parameter().apply {
                        this.name = it.first.trim()
                        this.type = it.second.trim()
                    }
                }.toList()

                Validator.Result.VALID
            }
    )

    override val pictogram = textShape(lineProperty)

    override val sidebar = sidebar {
        title("Method")
        group("General") {
            input("Name", nameProperty)
            input("Type", typeProperty)
        }
        sidebarParameters = group("Parameters") {}

        updateSidebar()
    }

    override val contextMenu = contextMenu {
        title = "Method: " + model.name
        addItem(MaterialIcon.DELETE, "Delete") {
            parent.methods -= this@MethodLinker
        }
    }

    private lateinit var sidebarParameters: SidebarGroup

    private enum class State {
        NAME, TYPE, PARAM_NAME, PARAM_TYPE, AFTER_PARAM
    }

    private fun updateSidebar() = sidebarParameters.apply {
        clearContent()

        model.parameters.forEach { param ->
            custom {
                tableView {
                    row {
                        cellBox {
                            inputView {
                                value = param.name
                                onChange {
                                    param.name = it
                                    redraw = false
                                    parameterProperty.fire()
                                }
                                onFocusLeave {
                                    param.name = param.name.trim()
                                    redraw = true
                                    parameterProperty.fire()
                                }
                            }
                        }
                        cellBox { textView("") }
                        cellBox {
                            inputView {
                                value = param.type
                                onChange {
                                    param.type = it
                                    redraw = false
                                    parameterProperty.fire()
                                }
                                onFocusLeave {
                                    param.type = param.type.trim()
                                    redraw = true
                                    parameterProperty.fire()
                                }
                            }
                        }
                        cellBox {
                            iconView(MaterialIcon.CLEAR) {
                                onClick {
                                    model.parameters -= param
                                    redraw = true
                                    parameterProperty.fire()
                                }
                            }
                        }
                    }
                }
            }
        }

        custom {
            iconView(MaterialIcon.ADD)
            textView("Add parameter")
            onClick {
                model.param("")
                redraw = true
                parameterProperty.fire()
            }
        }
    }

    var redraw = true

    init {
        parameterProperty.onChange { _ ->
            if (redraw) {
                updateSidebar()
            }
        }

        LinkerManager.setup(this)
    }

    companion object: LinkerInfoItem {
        override fun canCreate(container: Linker<*, *>): Boolean = container is ClassLinker
        override val name: String = "Method"
    }
}
