package io.framed.linker

import io.framed.framework.Linker
import io.framed.framework.LinkerInfoItem
import io.framed.framework.LinkerManager
import io.framed.framework.PreviewLinker
import io.framed.framework.pictogram.*
import io.framed.framework.util.LinkerShapeBox
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
        override val parent: Linker<*,*>
) : PreviewLinker<Class, BoxShape, TextShape> {

    private val nameProperty = property(model::name, RegexValidator("[a-zA-Z]([a-zA-Z0-9 ])*".toRegex())).trackHistory()
    var name by nameProperty

    val attributes = LinkerShapeBox(model::attributes)
    val methods = LinkerShapeBox(model::methods)

    override val pictogram = boxShape {
        boxShape {
            textShape(nameProperty)
            style {
                padding = box(8.0)
            }
        }
        attributes.view = boxShape {
            style {
                border {
                    style = Border.BorderStyle.SOLID
                    width = box(1.0, 0.0, 0.0, 0.0)
                    color = box(color(0, 0, 0, 0.3))
                }
                padding = box(8.0)
            }
        }
        methods.view = boxShape {
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
            background = linearGradient("to bottom") {
                add(color("#e3ffd9"), 0.0)
                add(color("#e3ffd9"), 1.0)
            }
            border {
                style = Border.BorderStyle.SOLID
                width = box(1.0)
                color = box(color(0, 0, 0, 0.3))
            }
        }

        resizeable = true
    }

    override val listPreview: TextShape = textShape(nameProperty)

    override val flatPreview = boxShape {
        textShape(nameProperty)

        style {
            background = linearGradient("to bottom") {
                add(color("#e3ffd9"), 0.0)
                add(color("#e3ffd9"), 1.0)
            }
            border {
                style = Border.BorderStyle.SOLID
                width = box(1.0)
                color = box(color(0, 0, 0, 0.3))
            }
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
            delete()
        }
    }

    override fun remove(linker: Linker<*, *>) {
        if (linker is AttributeLinker) attributes.remove(linker)
        if (linker is MethodLinker) methods.remove(linker)
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