package io.framed.linker

import io.framed.framework.*
import io.framed.framework.pictogram.*
import io.framed.framework.util.*
import io.framed.framework.view.*
import io.framed.model.Attribute
import io.framed.model.Class
import io.framed.model.Compartment
import io.framed.model.Method

/**
 * @author Sebastian
 * The linker manages a related compartment
 */
class CompartmentLinker(
        override val model: Compartment,
        override val connectionManager: ConnectionManager,
        override val parent: ModelLinker<*, *, *>
) : ModelLinker<Compartment, BoxShape, TextShape> {

    override val nameProperty = property(model::name, RegexValidator("[a-zA-Z]([a-zA-Z0-9])*".toRegex())).trackHistory()
    override var name by nameProperty

    override val container: BoxShape = boxShape(BoxShape.Position.ABSOLUTE) { }

    val attributes = shapeBox<Attribute, AttributeLinker>(model::attributes) { box ->
        box.view = container
    }
    val methods = shapeBox<Method, MethodLinker>(model::methods) { box ->
        box.view = container
    }
    val classes = shapeBox<Class, ClassLinker>(model::classes, connectionManager) { box ->
        box.view = container
    }

    override val shapeLinkers: Set<ShapeLinker<*, *>>
        get() = emptySet()

    private lateinit var autoLayoutBox: BoxShape

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
        autoLayoutBox = boxShape(BoxShape.Position.ABSOLUTE) {
            style {
                border {
                    style = Border.BorderStyle.SOLID
                    width = box(1.0, 0.0, 0.0, 0.0)
                    color = box(color(0, 0, 0, 0.3))
                }
                padding = box(8.0)
            }
        }
        classes.previewBox = autoLayoutBox

        style {
            background = linearGradient("to bottom") {
                add(color("#fffbd9"), 0.0)
                add(color("#fff7c4"), 1.0)
            }
            border {
                style = Border.BorderStyle.SOLID
                width = box(1.0)
                color = box(color(0, 0, 0, 0.3))
                radius = box(0.0)
            }
        }
        resizeable = true

        onLayerChange {
            updatePreviewType()
        }
    }

    override val listPreview: TextShape = textShape(nameProperty)
    override val flatPreview = boxShape {
        textShape(nameProperty)

        style {
            background = color("#fffbd9")
            border {
                style = Border.BorderStyle.SOLID
                width = box(1.0)
                color = box(color(0, 0, 0, 0.3))
                radius = box(0.0)
            }
            padding = box(10.0)
        }
    }

    private var isFlatPreview by pictogram.data(false)

    private fun updatePreviewType() {
        val shapeIsFlat = autoLayoutBox.position == BoxShape.Position.ABSOLUTE
        if (shapeIsFlat == isFlatPreview) return

        autoLayoutBox.position = if (isFlatPreview) {
            BoxShape.Position.ABSOLUTE
        } else {
            BoxShape.Position.VERTICAL
        }

        autoLayoutBox.clear()
        this@CompartmentLinker.classes.updatePreview()

        parent.redraw(this@CompartmentLinker)
    }

    private lateinit var sidebarPreviewGroup: SidebarGroup
    override val sidebar = sidebar {
        title("Compartment")
        group("General") {
            input("Name", nameProperty)
        }
        sidebarPreviewGroup = group("Preview") {
            button("Toggle preview") {
                isFlatPreview = !isFlatPreview
                updatePreviewType()
            }
            button("Auto layout") {
                autoLayoutBox.autoLayout()
            }
        }
    }

    override fun Sidebar.onOpen(event: SidebarEvent) {
        sidebarPreviewGroup.visible = event.target == pictogram
    }

    private lateinit var contextStepIn: ListView
    private lateinit var contextStepOut: ListView

    override fun ContextMenu.onOpen(event: ContextEvent) {
        contextStepIn.visible = event.target == pictogram
        contextStepOut.visible = event.target != pictogram
    }

    override val contextMenu = contextMenu {
        title = "Compartment: $name"

        contextStepIn = addItem(MaterialIcon.ARROW_FORWARD, "Step in") {
            ControllerManager.display(this@CompartmentLinker)
        }
        contextStepOut = addItem(MaterialIcon.ARROW_BACK, "Step out") {
            ControllerManager.display(parent)
        }

        addItem(MaterialIcon.ADD, "Add class") { event ->
            this@CompartmentLinker.classes += ClassLinker(Class(), this@CompartmentLinker).also {
                setPosition.fire(SetPosition(it, event.position))
                it.focus()
            }
        }

        addItem(MaterialIcon.DELETE, "Delete") { _ ->
            delete()
        }
    }

    override fun remove(linker: ShapeLinker<*, *>) {
        when (linker) {
            is AttributeLinker -> attributes -= linker
            is MethodLinker -> methods -= linker
            is ClassLinker -> classes -= linker

            else -> super.remove(linker)
        }
    }

    override fun redraw(linker: ShapeLinker<*, *>) {
        when (linker) {
            is AttributeLinker -> attributes.redraw(linker)
            is MethodLinker -> methods.redraw(linker)
            is ClassLinker -> classes.redraw(linker)

            else -> super.remove(linker)
        }
    }

    override val setPosition = EventHandler<SetPosition>()

    override fun dropShape(element: Long, target: Long) {
        throw UnsupportedOperationException()
    }

    init {
        model.attributes.forEach { attributes += AttributeLinker(it, this) }
        model.methods.forEach { methods += MethodLinker(it, this) }
        model.classes.forEach { classes += ClassLinker(it, parent) }

        LinkerManager.setup(this)
        connectionManager.addModel(this)
    }

    companion object : LinkerInfoItem {
        override fun canCreate(container: Linker<*, *>): Boolean = container is ContainerLinker
        override fun contains(linker: Linker<*, *>): Boolean = linker is CompartmentLinker

        override val name: String = "Compartment"
    }
}