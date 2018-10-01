package io.framed.linker

import io.framed.model.Method
import io.framed.model.Parameter
import io.framed.model.param
import io.framed.picto.ContextEvent
import io.framed.picto.TextShape
import io.framed.picto.textShape
import io.framed.util.RegexValidator
import io.framed.util.Validator
import io.framed.util.property
import io.framed.view.*

/**
 * @author lars
 */
class MethodLinker(
        val method: Method,
        override val parent: ClassLinker
) : Linker<TextShape>(method, parent) {

    private val nameProperty = property(method::name, RegexValidator("[a-zA-Z]([a-zA-Z0-9])*".toRegex()))

    private val typeProperty = property(method::type, RegexValidator("[a-zA-Z]([a-zA-Z0-9])*".toRegex()))

    private val parameterProperty = property(method::parameters)

    private val lineProperty = property(nameProperty, typeProperty, parameterProperty,
            getter = {
                "${method.name}(" + method.parameters.joinToString(", ") { it.toString() } + ")" + method.type.let {
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

                method.name = name.trim()
                method.type = type.trim()
                method.parameters = param.filter { it.first.isNotBlank() || it.first.isNotBlank() }.map {
                    Parameter().apply {
                        this.name = it.first.trim()
                        this.type = it.second.trim()
                    }
                }

                Validator.Result.VALID
            }
    )

    override val picto = textShape(lineProperty) {
        hasSidebar = true
        hasContext = true
    }.also(this::initPicto)

    override fun createContextMenu(event: ContextEvent): ContextMenu? = contextMenu {
        title = "Method: " + method.name
        addItem(MaterialIcon.DELETE, "Delete") {
            parent.removeMethod(method)
        }
    }

    private lateinit var sidebarParameters: SidebarGroup

    override fun createSidebar(sidebar: Sidebar) = sidebar.setup() {
        title("Method")
        group("General") {
            input("Name", nameProperty)
            input("Type", typeProperty)
        }
        sidebarParameters = group("Parameters") {}

        updateSidebar()
    }

    private enum class State {
        NAME, TYPE, PARAM_NAME, PARAM_TYPE, AFTER_PARAM
    }

    private fun updateSidebar() = sidebarParameters.apply {
        clearContent()

        method.parameters.forEach { param ->
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
                                    method.parameters -= param
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
                method.param("")
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
    }
}
