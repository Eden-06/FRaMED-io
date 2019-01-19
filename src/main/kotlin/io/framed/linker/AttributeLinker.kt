package io.framed.linker

import de.westermann.kobserve.basic.FunctionAccessor
import de.westermann.kobserve.basic.FunctionProperty
import de.westermann.kobserve.basic.property
import de.westermann.kobserve.basic.validate
import io.framed.framework.*
import io.framed.framework.pictogram.TextShape
import io.framed.framework.pictogram.textShape
import io.framed.framework.util.History
import io.framed.framework.util.RegexValidator
import io.framed.framework.util.trackHistory
import io.framed.framework.view.MaterialIcon
import io.framed.framework.view.contextMenu
import io.framed.framework.view.sidebar
import io.framed.model.Attribute
import io.framed.model.Class
import io.framed.model.Compartment
import io.framed.model.RoleType

/**
 * @author lars
 */
class AttributeLinker(
        override val model: Attribute,
        override val parent: ShapeLinker<*, *>
) : ShapeLinker<Attribute, TextShape> {

    private val nameProperty = property(model::name)
            .validate(RegexValidator("[a-zA-Z]([a-zA-Z0-9])*".toRegex())::validate)
            .trackHistory()
    private val typeProperty = property(model::type)
            .validate(RegexValidator("([a-zA-Z]([a-zA-Z0-9])*)?".toRegex())::validate)
            .trackHistory()

    private val lineProperty = FunctionProperty(object : FunctionAccessor<String> {
        override fun set(value: String): Boolean {
            var state = State.NAME

            var name = ""
            var type = ""

            value.forEach { char ->
                state = when (state) {
                    State.NAME -> {
                        when (char) {
                            ':' -> {
                                State.TYPE
                            }
                            '(', ')' -> return false
                            else -> {
                                name += char
                                State.NAME
                            }
                        }
                    }
                    State.TYPE -> {
                        when (char) {
                            ':', '(', ')' -> return false
                            else -> {
                                type += char
                                State.TYPE
                            }
                        }
                    }
                }
            }

            History.group("Change property") {
                nameProperty.value = name.trim()
                typeProperty.value = type.trim()
            }

            return nameProperty.valid && typeProperty.valid
        }

        override fun get(): String {
            return model.name + model.type.let {
                if (it.isBlank()) "" else ": $it"
            }.trim()
        }
    }, nameProperty, typeProperty)

    private enum class State {
        NAME, TYPE
    }

    override val pictogram = textShape(lineProperty)

    override val sidebar = sidebar {
        title("Attribute")

        group("General") {
            input("Name", nameProperty)
            input("Type", typeProperty)
        }
    }

    override val contextMenu = contextMenu {
        title = "Attribute: " + model.name
        addItem(MaterialIcon.DELETE, "Delete") {
            delete()
        }
    }

    init {
        LinkerManager.setup(this)
    }

    companion object : LinkerInfoItem {
        override fun canCreateIn(container: ModelElement<*>): Boolean {
            return container is Class || container is Compartment || container is RoleType
        }

        override fun isLinkerOfType(element: ModelElement<*>): Boolean = element is Attribute

        override val name: String = "Attribute"
    }
}