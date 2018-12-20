package io.framed.linker

import de.westermann.kobserve.basic.property
import de.westermann.kobserve.basic.validate
import io.framed.framework.Linker
import io.framed.framework.LinkerInfoItem
import io.framed.framework.LinkerManager
import io.framed.framework.PreviewLinker
import io.framed.framework.pictogram.*
import io.framed.framework.util.RegexValidator
import io.framed.framework.util.trackHistory
import io.framed.framework.view.MaterialIcon
import io.framed.framework.view.contextMenu
import io.framed.framework.view.sidebar
import io.framed.model.RoleType

/**
 * The linker manages a related role model
 */
class RoleTypeLinker(
        override val model: RoleType,
        override val parent: ContainerLinker
) : PreviewLinker<RoleType, BoxShape, TextShape> {

    private val nameProperty = property(model::name)
            .validate(RegexValidator("[a-zA-Z]([a-zA-Z0-9])*".toRegex())::validate)
            .trackHistory()
    var name by nameProperty

    private lateinit var bodyBox: BoxShape

    override val pictogram = boxShape {
        boxShape {
            textShape(nameProperty)
            style {
                padding = box(8.0)
            }
        }
        bodyBox = boxShape {
            style {
                border {
                    style = Border.BorderStyle.SOLID
                    width = box(1.0, 0.0, 0.0, 0.0)
                    color = box(color(0, 0, 0, 0.3))
                }
                padding = box(8.0)
            }
        }

        style {
            background = color("#d6d6d6")
            border {
                style = Border.BorderStyle.SOLID
                width = box(1.0)
                color = box(color(0, 0, 0, 0.3))
                radius = box(20.0)
            }
        }
    }

    override val listPreview: TextShape = textShape(nameProperty)
    override val flatPreview = boxShape {
        textShape(nameProperty)

        style {
            background = color("#d6d6d6")
            border {
                style = Border.BorderStyle.SOLID
                width = box(1.0)
                color = box(color(0, 0, 0, 0.3))
                radius = box(20.0)
            }
            padding = box(10.0)
        }
    }

    override val sidebar = sidebar {
        title("Class")
        group("General") {
            input("Name", nameProperty)
        }
    }

    override val contextMenu = contextMenu {
        title = "Class: $name"
        addItem(MaterialIcon.DELETE, "Delete") {
            delete()
        }
    }

    init {
        LinkerManager.setup(this)
    }

    companion object : LinkerInfoItem {
        override fun canCreate(container: Linker<*, *>): Boolean = container is ContainerLinker
        override fun contains(linker: Linker<*, *>): Boolean = linker is RoleTypeLinker

        override val name: String = "Role type"
    }
}