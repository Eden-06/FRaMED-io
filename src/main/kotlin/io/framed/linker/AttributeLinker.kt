package io.framed.linker

import io.framed.framework.Linker
import io.framed.framework.LinkerInfoItem
import io.framed.framework.LinkerManager
import io.framed.framework.ShapeLinker
import io.framed.framework.pictogram.Shape
import io.framed.framework.pictogram.TextShape
import io.framed.framework.pictogram.textShape
import io.framed.framework.util.*
import io.framed.framework.view.contextMenu
import io.framed.framework.view.MaterialIcon
import io.framed.framework.view.sidebar
import io.framed.model.Attribute

/**
 * @author lars
 */
class AttributeLinker(
        override val model: Attribute,
        override val parent: ShapeLinker<*, *>
) : ShapeLinker<Attribute, TextShape> {

    private val nameProperty = property(model::name, RegexValidator("[a-zA-Z]([a-zA-Z0-9])*".toRegex())).trackHistory()
    private val typeProperty = property(model::type, RegexValidator("[a-zA-Z]([a-zA-Z0-9])*".toRegex())).trackHistory()

    private val lineProperty = property(nameProperty, typeProperty,
            getter = {
                model.name + model.type.let {
                    if (it.isBlank()) "" else ": $it"
                }.trim()
            },
            setter = { input ->
                var state = State.NAME

                var name = ""
                var type = ""

                input.forEach { char ->
                    state = when (state) {
                        State.NAME -> {
                            when (char) {
                                ':' -> {
                                    State.TYPE
                                }
                                '(', ')' -> return@property Validator.Result.ERROR
                                else -> {
                                    name += char
                                    State.NAME
                                }
                            }
                        }
                        State.TYPE -> {
                            when (char) {
                                ':', '(', ')' -> return@property Validator.Result.ERROR
                                else -> {
                                    type += char
                                    State.TYPE
                                }
                            }
                        }
                    }
                }

                model.name = name.trim()
                model.type = type.trim()

                Validator.Result.VALID
            }
    )

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
        override fun canCreate(container: Linker<*, *>): Boolean = container is ClassLinker
        override fun contains(linker: Linker<*, *>): Boolean = linker is AttributeLinker

        override val name: String = "Attribute"
    }
}