package io.framed.linker

import de.westermann.kobserve.basic.join
import de.westermann.kobserve.basic.mapBinding
import de.westermann.kobserve.basic.property
import de.westermann.kobserve.basic.validate
import io.framed.framework.*
import io.framed.framework.pictogram.*
import io.framed.framework.util.RegexValidator
import io.framed.framework.util.shapeBox
import io.framed.framework.util.trackHistory
import io.framed.framework.view.*
import io.framed.model.Attribute
import io.framed.model.Compartment
import io.framed.model.Method
import io.framed.model.RoleType
import kotlin.math.roundToInt

/**
 * @author lars
 */
class RoleTypeLinker(
        override val model: RoleType,
        override val parent: ShapeLinker<*, *>
) : PreviewLinker<RoleType, BoxShape, TextShape> {

    override val nameProperty = property(model::name)
            .validate(RegexValidator("[a-zA-Z]([a-zA-Z0-9 ])*".toRegex())::validate)
            .trackHistory()
    override var name by nameProperty

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
                add(color("#d6d6d6"), 0.0)
                add(color("#d6d6d6"), 1.0)
            }
            border {
                style = Border.BorderStyle.SOLID
                width = box(1.0)
                color = box(color(0, 0, 0, 0.3))
                radius = box(20.0)
            }
        }

        resizeable = true
    }

    override val listPreview: TextShape = textShape(nameProperty)

    override val flatPreview = boxShape {
        textShape(nameProperty)

        style {
            background = linearGradient("to bottom") {
                add(color("#d6d6d6"), 0.0)
                add(color("#d6d6d6"), 1.0)
            }
            border {
                style = Border.BorderStyle.SOLID
                width = box(1.0)
                color = box(color(0, 0, 0, 0.3))
                radius = box(20.0)
            }
            padding = box(10.0)
        }

        resizeable = true
    }

    private lateinit var sidebarViewGroup: SidebarGroup
    private lateinit var sidebarFlatViewGroup: SidebarGroup

    override val sidebar = sidebar {
        title("RoleType")
        group("General") {
            input("Name", nameProperty)
        }
        sidebarViewGroup = group("Layout") {
            input("Position", pictogram.leftProperty.join(pictogram.topProperty) { left, top ->
                "x=${left.roundToInt()}, y=${top.roundToInt()}"
            })
            input("Size", pictogram.widthProperty.join(pictogram.heightProperty) { width, height ->
                "width=${width.roundToInt()}, height=${height.roundToInt()}"
            })
            checkBox("Autosize", pictogram.autosizeProperty, CheckBox.Type.SWITCH)
        }
        sidebarFlatViewGroup = group("Preview layout") {
            input("Position", flatPreview.leftProperty.join(flatPreview.topProperty) { left, top ->
                "x=${left.roundToInt()}, y=${top.roundToInt()}"
            })
            input("Size", flatPreview.widthProperty.join(flatPreview.heightProperty) { width, height ->
                "width=${width.roundToInt()}, height=${height.roundToInt()}"
            })
            checkBox("Autosize", flatPreview.autosizeProperty, CheckBox.Type.SWITCH)
        }

    }

    override fun Sidebar.onOpen(event: SidebarEvent) {
        sidebarViewGroup.display = event.target == pictogram
        sidebarFlatViewGroup.display = event.target == flatPreview
    }

    override val contextMenu = contextMenu {
        titleProperty.bind(nameProperty.mapBinding { "RoleType: $it" })
        addItem(MaterialIcon.ADD, "Add attribute") { event ->
            attributes += AttributeLinker(Attribute(), this@RoleTypeLinker).also { linker ->
                linker.focus(event.target)
            }
        }
        addItem(MaterialIcon.ADD, "Add method") { event ->
            methods += MethodLinker(Method(), this@RoleTypeLinker).also { linker ->
                linker.focus(event.target)
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
        override fun canCreateIn(container: ModelElement<*>): Boolean {
            return container is Compartment
        }

        override fun isLinkerOfType(element: ModelElement<*>): Boolean = element is RoleType

        override val name: String = "RoleType"
    }
}