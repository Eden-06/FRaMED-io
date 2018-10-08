package io.framed.linker

import io.framed.framework.Linker
import io.framed.framework.LinkerInfoItem
import io.framed.framework.LinkerManager
import io.framed.framework.PreviewLinker
import io.framed.framework.pictogram.*
import io.framed.framework.util.LinkerBox
import io.framed.framework.util.RegexValidator
import io.framed.framework.util.property
import io.framed.framework.util.trackHistory
import io.framed.framework.view.*
import io.framed.model.Attribute
import io.framed.model.Class
import io.framed.model.Method

/**
 * @author lars
 */
class ClassLinker(
        override val model: Class,
        override val parent: ContainerLinker
) : PreviewLinker<Class, BoxShape, TextShape> {

    private val nameProperty = property(model::name, RegexValidator("[a-zA-Z]([a-zA-Z0-9 ])*".toRegex())).trackHistory()
    var name by nameProperty

    val attributes = LinkerBox(model::attributes)
    val methods = LinkerBox(model::methods)

    override val pictogram = boxShape {
        boxShape {
            textShape(nameProperty)
        }
        attributes.view = boxShape { }
        methods.view = boxShape { }

        style {
            background = linearGradient("to bottom") {
                add(color("#fffbd9"), 0.0)
                add(color("#fff7c4"), 1.0)
            }
            border {
                style = Border.BorderStyle.SOLID
                width = 1.0
                color = color(0, 0, 0, 0.3)
            }
        }
    }

    override val preview: TextShape = textShape(nameProperty)

    override val sidebar = sidebar {
        title("Class")
        group("General") {
            input("Name", nameProperty)
        }
    }

    override val contextMenu = contextMenu {
        title = "Class: $name"
        addItem(MaterialIcon.ADD, "Add attribute") {
            attributes += AttributeLinker(Attribute(), this@ClassLinker).also { linker ->
                linker.focus()
            }
        }
        addItem(MaterialIcon.ADD, "Add method") {
            methods += MethodLinker(Method(), this@ClassLinker).also { linker ->
                linker.focus()
            }
        }
        addItem(MaterialIcon.DELETE, "Delete") {
            parent.classes -= this@ClassLinker
        }
    }

    init {
        model.attributes.forEach { attributes += AttributeLinker(it, this) }
        model.methods.forEach { methods += MethodLinker(it, this) }

        LinkerManager.setup(this)
    }

    companion object : LinkerInfoItem {
        override fun canCreate(container: Linker<*, *>): Boolean = container is ContainerLinker
        override val name: String = "Class"
    }
}