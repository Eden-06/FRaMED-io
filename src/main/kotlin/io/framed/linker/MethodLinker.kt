package io.framed.linker

import de.westermann.kobserve.basic.FunctionAccessor
import de.westermann.kobserve.basic.property
import de.westermann.kobserve.basic.validate
import io.framed.framework.Linker
import io.framed.framework.LinkerInfoItem
import io.framed.framework.LinkerManager
import io.framed.framework.ShapeLinker
import io.framed.framework.pictogram.TextShape
import io.framed.framework.pictogram.textShape
import io.framed.framework.util.History
import io.framed.framework.util.RegexValidator
import io.framed.framework.util.trackHistory
import io.framed.framework.view.*
import io.framed.model.Method
import io.framed.model.Parameter

/**
 * @author lars
 */
class MethodLinker(
        override val model: Method,
        override val parent: ShapeLinker<*, *>
) : ShapeLinker<Method, TextShape> {

    private val nameProperty = property(model::name)
            .validate(RegexValidator("[a-zA-Z]([a-zA-Z0-9])*".toRegex())::validate)
            .trackHistory()
    private val typeProperty = property(model::type)
            .validate(RegexValidator("[a-zA-Z]([a-zA-Z0-9])*".toRegex())::validate)
            .trackHistory()

    private val parameterProperty = property(model::parameters).trackHistory()

    private val lineProperty = property(object : FunctionAccessor<String> {
        override fun set(value: String): Boolean {
            var state = State.NAME

            var name = ""
            var type = ""
            var param = listOf<Pair<String, String>>()

            value.forEach { char ->
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
                            ')' -> return false
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
                            ':', ')' -> return false
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
                            '(' -> return false
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
                            ':', '(' -> return false
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
                            else -> return false
                        }
                    }
                }
            }

            History.group("Change property") {
                nameProperty.value = name.trim()
                typeProperty.value = type.trim()
                parameterProperty.value = param.asSequence().filter { it.first.isNotBlank() || it.first.isNotBlank() }.map {
                    Parameter().apply {
                        this.name = it.first.trim()
                        this.type = it.second.trim()
                    }
                }.toList()
            }

            return true
        }

        override fun get(): String {
            return "${model.name}(" + model.parameters.joinToString(", ") { it.toString() } + ")" + model.type.let {
                if (it.isBlank()) "" else ": $it"
            }.trim()
        }

    }, nameProperty, typeProperty, parameterProperty)

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
            delete()
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
                                    parameterProperty.onChange.emit(Unit)
                                }
                                onFocusLeave {
                                    param.name = param.name.trim()
                                    redraw = true
                                    parameterProperty.onChange.emit(Unit)
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
                                    parameterProperty.onChange.emit(Unit)
                                }
                                onFocusLeave {
                                    param.type = param.type.trim()
                                    redraw = true
                                    parameterProperty.onChange.emit(Unit)
                                }
                            }
                        }
                        cellBox {
                            iconView(MaterialIcon.CLEAR) {
                                onClick {
                                    model.parameters -= param
                                    redraw = true
                                    parameterProperty.onChange.emit(Unit)
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
                model.parameters += Parameter()
                redraw = true
                parameterProperty.onChange.emit(Unit)
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

    companion object : LinkerInfoItem {
        override fun canCreate(container: Linker<*, *>): Boolean = container is ClassLinker
        override fun contains(linker: Linker<*, *>): Boolean = linker is MethodLinker

        override val name: String = "Method"
    }
}
