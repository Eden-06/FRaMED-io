package io.framed.linker

import de.westermann.kobserve.basic.FunctionAccessor
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
import io.framed.model.Compartment
import io.framed.model.Method
import kotlin.math.roundToInt

/**
 * @author Sebastian
 * The linker manages a related compartment
 */
class CompartmentLinker(
        override val model: Compartment,
        override val connectionManager: ConnectionManager,
        override val parent: ModelLinker<*, *, *>
) : ModelLinker<Compartment, BoxShape, TextShape> {

    override val nameProperty = property(model::name)
            .validate(RegexValidator("[a-zA-Z]([a-zA-Z0-9])*".toRegex())::validate)
            .trackHistory()
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

        layerProperty.onChange {
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

    private val isFlatPreviewStringProperty = pictogram.data("flat-preview")
    private val isFlatPreviewProperty = property(object : FunctionAccessor<Boolean> {
        override fun set(value: Boolean): Boolean {
            isFlatPreviewStringProperty.value = value.toString()
            return true
        }

        override fun get(): Boolean = isFlatPreviewStringProperty.value?.toBoolean() ?: false
    }, isFlatPreviewStringProperty)
    private var isFlatPreview by isFlatPreviewProperty

    private fun updatePreviewType() {
        val shapeIsFlat = autoLayoutBox.position == BoxShape.Position.ABSOLUTE
        if (shapeIsFlat == isFlatPreview) return

        autoLayoutBox.position = if (isFlatPreview) {
            BoxShape.Position.ABSOLUTE
        } else {
            BoxShape.Position.VERTICAL
        }

        autoLayoutBox.clear()
        this@CompartmentLinker.classes.addAllPreviews()

        parent.redraw(this@CompartmentLinker)
    }

    private lateinit var sidebarViewGroup: SidebarGroup
    private lateinit var sidebarFlatViewGroup: SidebarGroup
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
        }
    }

    override fun Sidebar.onOpen(event: SidebarEvent) {
        val isTargetRoot = event.target == pictogram
        sidebarViewGroup.display = isTargetRoot
        sidebarPreviewGroup.display = isTargetRoot

        sidebarFlatViewGroup.display = event.target == flatPreview
    }

    private lateinit var contextStepIn: ListView
    private lateinit var contextStepOut: ListView

    override fun ContextMenu.onOpen(event: ContextEvent) {
        contextStepIn.display = event.target == pictogram
        contextStepOut.display = event.target != pictogram
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
            val linker = ClassLinker(Class(), this@CompartmentLinker)
            this@CompartmentLinker.classes += linker
            linker.also {
                it.pictogram.left = event.diagram.x
                it.pictogram.top = event.diagram.y
                it.focus(event.target)
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