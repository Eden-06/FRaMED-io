package io.framed.linker

import io.framed.framework.*
import io.framed.framework.pictogram.*
import io.framed.framework.util.*
import io.framed.framework.view.*
import io.framed.model.Class
import io.framed.model.Compartment
import io.framed.model.Container

/**
 * @author Sebastian
 * The linker manages a related compartment
 */
class CompartmentLinker(
        override val model: Compartment,
        override val parent: ModelLinker<*, *, *>
) : ModelLinker<Compartment, BoxShape, TextShape> {

    override val nameProperty = property(model::name, RegexValidator("[a-zA-Z]([a-zA-Z0-9])*".toRegex())).trackHistory()
    override var name by nameProperty

    override val container: BoxShape = boxShape(BoxShape.Position.ABSOLUTE) { }

    val attributes = LinkerShapeBox(model::attributes).also { box ->
        box.view = container
    }
    val methods = LinkerShapeBox(model::methods).also { box ->
        box.view = container
    }
    val classes = LinkerShapeBox(model::classes).also { box ->
        box.view = container
    }

    override val connectable: List<Linker<*, *>>
        get() = emptyList()

    override val connections: List<ConnectionLinker<*>>
        get() = emptyList()

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
        val box = boxShape(BoxShape.Position.ABSOLUTE) {
            style {
                border {
                    style = Border.BorderStyle.SOLID
                    width = box(1.0, 0.0, 0.0, 0.0)
                    color = box(color(0, 0, 0, 0.3))
                }
                padding = box(8.0)
            }
        }
        classes.previewBox = box

        style {
            background = linearGradient("to bottom") {
                add(color("#fffbd9"), 0.0)
                add(color("#fff7c4"), 1.0)
            }
            border {
                style = Border.BorderStyle.SOLID
                width = box(1.0)
                color = box(color(0, 0, 0, 0.3))
                radius = box(10.0)
            }
        }
        resizeable = true
    }

    override val listPreview: TextShape = textShape(nameProperty)
    override val flatPreview = boxShape { textShape(nameProperty) }

    override val sidebar = sidebar {
        title("Class")
        group("General") {
            input("Name", nameProperty)
        }
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

    override fun remove(linker: Linker<*, *>) {
        if (linker is AttributeLinker) attributes.remove(linker)
        if (linker is MethodLinker) methods.remove(linker)
        if (linker is ClassLinker) classes.remove(linker)
    }

    override val onConnectionAdd = EventHandler<ConnectionLinker<*>>()
    override val onConnectionRemove = EventHandler<ConnectionLinker<*>>()
    override val setPosition = EventHandler<SetPosition>()

    override fun createConnection(source: Shape, target: Shape) {
        throw UnsupportedOperationException()
    }

    override fun createConnection(source: Shape, target: Shape, type: ConnectionInfo): ConnectionLinker<*> {
        //val sourceId = getIdByShape(source) ?: throw IllegalArgumentException()
        //val targetId = getIdByShape(target) ?: throw IllegalArgumentException()

        throw UnsupportedOperationException()
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