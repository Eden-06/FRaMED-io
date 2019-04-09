package io.framed.linker

import de.westermann.kobserve.property.FunctionAccessor
import de.westermann.kobserve.property.property
import de.westermann.kobserve.property.validate
import io.framed.framework.*
import io.framed.framework.pictogram.TextShape
import io.framed.framework.pictogram.textShape
import io.framed.framework.util.History
import io.framed.framework.util.RegexValidator
import io.framed.framework.util.trackHistory
import io.framed.framework.view.*
import io.framed.model.*

/**
 * @author lars
 */
class MethodLinker(
        override val model: Method,
        override val parent: ShapeLinker<*, *>
) : ShapeLinker<Method, TextShape> {

    override val nameProperty = property(model::name)
            .validate(RegexValidator.IDENTIFIER::validate)
            .trackHistory()
    private val typeProperty = property(model::type)
            .validate(RegexValidator.IDENTIFIER::validate)
            .trackHistory()

    override val name by nameProperty

    override val subTypes: Set<String>
        get() = setOf(model.type) + model.parameters.map { it.type }

    private val parameterProperty = property(model::parameters).trackHistory()

    private enum class State {
        NAME, TYPE, PARAM_NAME, PARAM_TYPE, AFTER_PARAM
    }

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

    private lateinit var sidebarParameters: SidebarGroup
    private lateinit var sidebarParametersAdd: ListView
    private val sidebarList: MutableList<SidebarEntry> = mutableListOf()

    private fun updateSidebar() {
        while (sidebarList.size > model.parameters.size) {
            val last = sidebarList.last()
            last.remove()
            sidebarList -= last
        }

        for (i in 0 until sidebarList.size) {
            sidebarList[i].bind(model.parameters[i])
        }

        for (i in sidebarList.size until model.parameters.size) {
            sidebarList += SidebarEntry(sidebarParameters, model.parameters[i])
        }

        sidebarParameters.toForeground(sidebarParametersAdd)
    }

    override val sidebar = sidebar {
        title("Method")
        group("General") {
            input("Name", nameProperty)
            input("Type", typeProperty, this@MethodLinker::getTypeSubset)
        }
        sidebarParameters = group("Parameters") {
            sidebarParametersAdd = custom {
                iconView(MaterialIcon.ADD)
                textView("Add parameter")
                onClick {
                    parameterProperty.value += Parameter()
                }
            }
        }

        updateSidebar()
    }

    override val contextMenu = defaultContextMenu()

    init {
        parameterProperty.onChange { _ ->
            updateSidebar()
        }

        LinkerManager.setup(this)
    }

    companion object : LinkerInfoItem {

        override fun canCreateIn(container: ModelElement<*>): Boolean {
            return container is Class || container is Compartment || container is RoleType
        }

        override fun isLinkerFor(element: ModelElement<*>): Boolean = element is Method
        override fun isLinkerFor(linker: Linker<*, *>): Boolean = linker is MethodLinker

        override fun createModel(): ModelElement<*> = Method()
        override fun createLinker(model: ModelElement<*>, parent: Linker<*, *>, connectionManager: ConnectionManager?): Linker<*, *> {
            if (model is Method && parent is ShapeLinker<*, *>) {
                return MethodLinker(model, parent)
            } else throw UnsupportedOperationException()
        }

        override val name: String = "Method"
        override val isConnectable = false
    }

    inner class SidebarEntry(private val sidebarGroup: SidebarGroup, private var param: Parameter) {

        private lateinit var nameInput: InputView
        private lateinit var typeInput: InputView
        private val listView: ListView

        fun bind(parameter: Parameter) {
            if (param !== parameter) {
                param = parameter
                nameInput.value = param.name
                typeInput.value = param.type
            }
        }

        private fun deleteParameter() {
            parameterProperty.value -= param
            remove()
        }

        fun remove() {
            sidebarGroup.remove(listView)
        }

        init {
            listView = sidebarGroup.custom {
                tableView {
                    row {
                        cellBox {
                            nameInput = inputView {
                                value = param.name
                                onChange {
                                    param.name = it
                                    parameterProperty.onChange.emit(Unit)
                                }
                                onFocusLeave {
                                    param.name = param.name.trim()
                                    parameterProperty.onChange.emit(Unit)
                                }
                            }
                        }
                        cellBox { textView("") }
                        cellBox {
                            typeInput = inputView {
                                autocomplete(this@MethodLinker::getTypeSubset)
                                value = param.type
                                onChange {
                                    param.type = it
                                    parameterProperty.onChange.emit(Unit)
                                }
                                onFocusLeave {
                                    param.type = param.type.trim()
                                    parameterProperty.onChange.emit(Unit)
                                }
                            }
                        }
                        cellBox {
                            iconView(MaterialIcon.CLEAR) {
                                onClick {
                                    deleteParameter()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}