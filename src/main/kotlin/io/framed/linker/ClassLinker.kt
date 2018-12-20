package io.framed.linker

import de.westermann.kobserve.basic.join
import de.westermann.kobserve.basic.property
import de.westermann.kobserve.basic.validate
import io.framed.framework.*
import io.framed.framework.pictogram.*
import io.framed.framework.util.RegexValidator
import io.framed.framework.util.shapeBox
import io.framed.framework.util.trackHistory
import io.framed.framework.view.*
import io.framed.model.Attribute
import io.framed.model.Class
import io.framed.model.Method
import kotlin.math.roundToInt

/**
 * @author lars
 */
class ClassLinker(
        override val model: Class,
        override val parent: ShapeLinker<*, *>
) : PreviewLinker<Class, BoxShape, TextShape> {

    private val nameProperty = property(model::name)
            .validate(RegexValidator("[a-zA-Z]([a-zA-Z0-9 ])*".toRegex())::validate)
            .trackHistory()
    var name by nameProperty

    val attributes = shapeBox(model::attributes)
    val methods = shapeBox(model::methods)

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
                add(color("#f9f9f9"), 0.0)
                add(color("#eaeaea"), 1.0)
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
                add(color("#f9f9f9"), 0.0)
                add(color("#eaeaea"), 1.0)
            }
            border {
                style = Border.BorderStyle.SOLID
                width = box(1.0)
                color = box(color(0, 0, 0, 0.3))
            }
            padding = box(10.0)
        }
    }

    private val positionProperty = pictogram.leftProperty.join(pictogram.topProperty) { left, top ->
        "x=${left.roundToInt()}, y=${top.roundToInt()}"
    }
    private val sizeProperty = pictogram.widthProperty.join(pictogram.heightProperty) { width, height ->
        "width=${width.roundToInt()}, height=${height.roundToInt()}"
    }

    private lateinit var sidebarViewGroup: SidebarGroup

    override val sidebar = sidebar {
        title("Class")
        group("General") {
            input("Name", nameProperty)
        }
        sidebarViewGroup = group("View") {
            input("Position", positionProperty)
            input("Size", sizeProperty)
            checkBox("Autosize", pictogram.autosizeProperty, CheckBox.Type.SWITCH)
        }

    }

    override fun Sidebar.onOpen(event: SidebarEvent) {
        val h = event.target == pictogram
        sidebarViewGroup.display = h
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

    override fun remove(linker: ShapeLinker<*, *>) {
        when (linker) {
            is AttributeLinker -> attributes.remove(linker)
            is MethodLinker -> methods.remove(linker)
            else -> super.remove(linker)
        }
    }

    init {
        model.attributes.forEach { attributes += AttributeLinker(it, this) }
        model.methods.forEach { methods += MethodLinker(it, this) }

        LinkerManager.setup(this)
    }

    companion object : LinkerInfoItem {
        override fun canCreate(container: Linker<*, *>): Boolean = container is ContainerLinker
        override fun contains(linker: Linker<*, *>): Boolean = linker is ClassLinker

        override val name: String = "Class"
    }
}