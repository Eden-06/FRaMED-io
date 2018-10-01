package io.framed.linker

import io.framed.model.Attribute
import io.framed.model.Method
import io.framed.model.RoleType
import io.framed.picto.*
import io.framed.util.RegexValidator
import io.framed.util.property
import io.framed.view.ContextMenu
import io.framed.view.MaterialIcon
import io.framed.view.Sidebar
import io.framed.view.contextMenu

/**
 * The linker manages a related role type
 */
class RoleTypeLinker (
    val type: RoleType,
    override val parent: ContainerLinker
    ) : Linker<BoxShape>(type, parent){

    val nameProperty = property(type::name, RegexValidator("[a-zA-Z]([a-zA-Z0-9])*".toRegex()))
    var name by nameProperty

    private lateinit var bodyBox: BoxShape

    override val picto = boxShape {
        boxShape {
            textShape(nameProperty)
        }
        bodyBox = boxShape {  }

        style {
            background = linearGradient("to bottom") {
                add(color("#fffbd9"), 0.0)
                add(color("#fff7c4"), 1.0)
            }
            border {
                style = Border.BorderStyle.SOLID
                width = 1.0
                color = color(0, 0, 0, 0.3)
                radius = 10.0
            }
        }

        hasSidebar = true
        hasContext = true

        acceptRelation = true
    }.also(this::initPicto)

    override fun createSidebar(sidebar: Sidebar) = sidebar.setup {
        title("Class")
        group("General") {
            input("Name", nameProperty)
        }
    }

    override fun createContextMenu(event: ContextEvent): ContextMenu? = contextMenu {
        title = "Class: $name"
        addItem(MaterialIcon.DELETE, "Delete") {
            parent.removeRoleType(type)
        }
    }

    init { }
}