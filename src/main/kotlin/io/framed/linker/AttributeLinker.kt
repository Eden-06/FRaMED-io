package io.framed.linker

import de.westermann.kobserve.property.FunctionAccessor
import de.westermann.kobserve.property.FunctionProperty
import de.westermann.kobserve.property.property
import de.westermann.kobserve.property.validate
import io.framed.framework.*
import io.framed.framework.pictogram.TextShape
import io.framed.framework.pictogram.textShape
import io.framed.framework.util.History
import io.framed.framework.util.RegexValidator
import io.framed.framework.util.trackHistory
import io.framed.framework.view.sidebar
import io.framed.model.*

/**
 * @author lars
 */
class AttributeLinker(
        override val model: Attribute,
        override val parent: ShapeLinker<*, *>
) : ShapeLinker<Attribute, TextShape> {

    override val nameProperty = property(model::name)
            .validate(RegexValidator.IDENTIFIER::validate)
            .trackHistory()
    private val typeProperty = property(model::type)
            .validate(RegexValidator.IDENTIFIER::validate)
            .trackHistory()

    override val name by nameProperty

    override val subTypes: Set<String>
        get() = setOf(model.type)

    private val lineProperty = FunctionProperty(object : FunctionAccessor<String> {
        override fun set(value: String): Boolean {
            var state = State.NAME

            var name = ""
            var type = ""

            value.forEach { char ->
                state = when (state) {
                    State.NAME -> {
                        when (char) {
                            ':', ' ' -> {
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
            input("Type", typeProperty, this@AttributeLinker::getTypeSubset)
        }
    }

    override val contextMenu = defaultContextMenu()

    init {
        LinkerManager.setup(this)
    }

    companion object : LinkerInfoItem {
        override fun canCreateIn(container: ModelElement<*>): Boolean {
            return container is Class || container is Compartment || container is Scene || container is RoleType
        }

        override fun isLinkerFor(element: ModelElement<*>): Boolean = element is Attribute
        override fun isLinkerFor(linker: Linker<*, *>): Boolean = linker is AttributeLinker

        override fun createModel(): ModelElement<*> = Attribute()
        override fun createLinker(model: ModelElement<*>, parent: Linker<*, *>, connectionManager: ConnectionManager?): Linker<*, *> {
            if (model is Attribute && parent is ShapeLinker<*, *>) {
                return AttributeLinker(model, parent)
            } else throw UnsupportedOperationException()
        }

        override val name: String = "Attribute"
        override val isConnectable = false
    }
}