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
import io.framed.model.Compartment

/**
 * @author Sebastian
 * The linker manages a related compartment
 */
class CompartmentLinker(
        override val model: Compartment,
        override val parent: Linker<*, *>
) : PreviewLinker<Compartment, BoxShape, TextShape> {

    private val nameProperty = property(model::name, RegexValidator("[a-zA-Z]([a-zA-Z0-9])*".toRegex())).trackHistory()
    var name by nameProperty

    val attributes = LinkerShapeBox(model::attributes)
    val methods = LinkerShapeBox(model::methods)
    val classes = LinkerShapeBox(model::classes)

    override val pictogram = boxShape {
        boxShape {
            textShape(nameProperty)
        }

        attributes.view = boxShape { }
        methods.view = boxShape { }
        classes.view = boxShape { }

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
        addItem(MaterialIcon.DELETE, "Delete") {
            delete()
        }
    }

    override fun remove(linker: Linker<*, *>) {
        if (linker is AttributeLinker) attributes.remove(linker)
        if (linker is MethodLinker) methods.remove(linker)
        if (linker is ClassLinker) classes.remove(linker)
    }

    init {
        model.attributes.forEach { attributes += AttributeLinker(it, this) }
        model.methods.forEach { methods += MethodLinker(it, this) }
        model.classes.forEach { classes += ClassLinker(it, parent) }

        LinkerManager.setup(this)
    }

    companion object : LinkerInfoItem {
        override fun canCreate(container: Linker<*, *>): Boolean = container is ContainerLinker
        override val name: String = "Compartment"
    }
}