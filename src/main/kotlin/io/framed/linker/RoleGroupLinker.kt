package io.framed.linker

import de.westermann.kobserve.property.FunctionAccessor
import de.westermann.kobserve.property.constProperty
import de.westermann.kobserve.property.property
import de.westermann.kobserve.property.validate
import io.framed.framework.ConnectionManager
import io.framed.framework.Layouting
import io.framed.framework.linker.*
import io.framed.framework.model.ModelElement
import io.framed.framework.pictogram.*
import io.framed.framework.util.*
import io.framed.framework.view.*
import io.framed.model.*

class RoleGroupLinker(
    override val model: RoleGroup,
    override val connectionManager: ConnectionManager,
    override val parent: ModelLinker<*, *, *>
) : ModelLinker<RoleGroup, BoxShape, BoxShape> {

    override val nameProperty = property(model::name)
        .validate(RegexValidator.IDENTIFIER::validate)
        .trackHistory()
    override var name by nameProperty

    private val cardinalityProperty = property(model::cardinality).trackHistory()

    override val container: BoxShape = boxShape(BoxShape.Position.ABSOLUTE) { }

    private val children = shapeBox<ModelElement, ShapeLinker<out ModelElement, out Shape>>(model::children, connectionManager) { box ->
        box.view = container
        box.onChildrenChange {
            updateSize()
        }
    }
    override val shapeLinkers: Set<ShapeLinker<*, *>>
    get() = children.linkers.toSet()

    override val subTypes: Set<String>
    get() = (shapeLinkers.flatMap { it.subTypes }).toSet() + model.name

    override lateinit var autoLayoutBox: BoxShape
    override lateinit var borderBox: BoxShape
    override val borderShapes = mutableListOf<Shape>()

    override val pictogram = boxShape {
        boxShape {
            textShape(nameProperty,
                alignment = TextShape.TextAlignment.RIGHT
            )
            textShape(cardinalityProperty,
                autocomplete = CardinalityPreset.STRING_VALUES,
                surround = Surround.PARENTHESIS
            )
            style {
                padding = box(8.0)
                flex = true
                overflow = true
            }
        }

        autoLayoutBox = boxShape(BoxShape.Position.ABSOLUTE) {
            style {
                border = null
                padding = box(8.0)
                stretchHeight = true
            }
        }
        children.previewBox = autoLayoutBox

        borderBox = boxShape(BoxShape.Position.BORDER) {}

        children.conditionalContainer(borderBox) {
            it is ReturnEventLinker
        }

        style {
            background = Color.TRANSPARENT

            border {
                style = Border.BorderStyle.DASHED
                width = box(Border.DEFAULT_WIDTH)
                color = box(color(0, 0, 0, 0.3))
                radius = box(20.0)
            }
        }

        resizeable = true

        layerProperty.onChange {
            updatePreviewType()
        }

    }

    /**
     * Preview that is shown for the flatPreview of the parent.
     */
    override val preview = boxShape(BoxShape.Position.HORIZONTAL) {
        iconShape(constProperty(info.icon))
        textShape(nameProperty)

        style {
            padding = box(0.0, 0.0, 0.0, 30.0)
        }

        ignoreLabels = true
    }

    /**
     * Specify if this view should be displayed in the complete view or only the name.
     */
    private val isCompleteViewStringProperty = pictogram.data("complete-view")
    private val isCompleteViewProperty = property(object : FunctionAccessor<Boolean> {
        val default: Boolean
            get() = true

        override fun set(value: Boolean): Boolean {
            if (value == default) {
                isCompleteViewStringProperty.value = null
            } else {
                isCompleteViewStringProperty.value = value.toString()
            }
            return true
        }

        override fun get(): Boolean {
            return isCompleteViewStringProperty.value?.toBoolean() ?: default
        }
    }, isCompleteViewStringProperty)

    /**
     * Specify if the content should be displayed in flat preview or not.
     */
    private val isFlatPreviewStringProperty = pictogram.data("flat-preview")
    private val isFlatPreviewProperty = property(object : FunctionAccessor<Boolean> {
        override fun set(value: Boolean): Boolean {
            isFlatPreviewStringProperty.value = value.toString()
            updatePreviewType()
            return true
        }

        override fun get(): Boolean = isFlatPreviewStringProperty.value?.toBoolean() ?: false
    }, isFlatPreviewStringProperty)
    private var isFlatPreview by isFlatPreviewProperty

    private lateinit var sidebarActionsGroup: SidebarGroup
    private lateinit var sidebarPreviewGroup: SidebarGroup
    private lateinit var sidebarViewGroup: SidebarGroup


    private fun updatePreviewType() {
        val shapeIsFlat = autoLayoutBox.position == BoxShape.Position.VERTICAL
        if (shapeIsFlat == isFlatPreview) return

        autoLayoutBox.position = if (isFlatPreview) {
            BoxShape.Position.VERTICAL
        } else {
            BoxShape.Position.ABSOLUTE
        }

        autoLayoutBox.clear()
        children.addAllPreviews()

        parent.redraw(this)
    }

    override val sidebar = sidebar {
        title("Role Group")
        group("General") {
            input("Name", nameProperty)
            input("Cardinality", cardinalityProperty, CardinalityPreset.STRING_VALUES)
        }
        sidebarActionsGroup = group("Actions") {
            button("Auto layout") {
                Layouting.autoLayout(
                    container,
                    connectionManager.connections.asSequence().map { it.pictogram }.toSet(),
                    this@RoleGroupLinker
                )
            }
            button("Reset zoom") {
                Application.renderer.zoom = 1.0
            }
            button("Reset pan") {
                Application.renderer.panTo(Point.ZERO)
            }
        }
        sidebarPreviewGroup = group("Preview") {
            checkBox("Flat preview", isFlatPreviewProperty, CheckBox.Type.SWITCH)
            button("Auto layout") {
                Layouting.autoLayout(
                    autoLayoutBox,
                    connectionManager.connections.asSequence().map { it.pictogram }.toSet(),
                    this@RoleGroupLinker
                )
            }
        }
        sidebarViewGroup = group("Layout") {
            button("Auto size") {
                updateSize(true)
            }
            checkBox("Complete view", isCompleteViewProperty, CheckBox.Type.SWITCH)
        }

        advanced(pictogram)
    }

    override fun Sidebar.onOpen(event: SidebarEvent) {
        val isTargetRoot = event.target == pictogram
        sidebarActionsGroup.display = event.target == container
        sidebarViewGroup.display = isTargetRoot
        sidebarPreviewGroup.display = isTargetRoot
    }

    override val contextMenu = defaultContextMenu {}

    override fun remove(linker: ShapeLinker<*, *>) {
        if (linker in children) {
            children -= linker
        } else {
            super.remove(linker)
        }
        updatePorts()
    }

    override fun add(model: ModelElement): ShapeLinker<*, *> {
        val linker = LinkerManager.createLinker<ShapeLinker<*, *>>(model, this, connectionManager)
        children += linker
        updatePorts()
        return linker
    }

    override fun redraw(linker: ShapeLinker<*, *>) {
        children.redraw(linker)
        updatePorts()
    }

    override fun ContextMenu.onOpen(event: ContextEvent) {}

    /**
     * The model initializes a new instance of the linker
     */
    init {
        children.view.visibleProperty.bind(isCompleteViewProperty)
        children.previewBox?.visibleProperty?.bind(isCompleteViewProperty)

        pictogram.onAutoSize {
            updateSize()
        }

        for (element in model.children) {
            add(element)
        }

        LinkerManager.setup(this)
        connectionManager.addModel(this)

        connectionManager.onConnectionAdd { updatePorts() }
        connectionManager.onConnectionRemove { updatePorts() }
        updatePorts()
    }

    companion object : LinkerInfoItem {

        override val info = ElementInfo("RoleGroup", FramedIcon.ROLEGROUP)

        override fun canCreateIn(container: ModelElement): Boolean {
            return container is Compartment || container is Scene || container is RoleGroup
        }

        override fun isLinkerFor(element: ModelElement): Boolean = element is RoleGroup
        override fun isLinkerFor(linker: Linker<*, *>): Boolean = linker is RoleGroupLinker

        override fun createModel(): ModelElement = RoleGroup()
        override fun createLinker(model: ModelElement, parent: Linker<*, *>, connectionManager: ConnectionManager?): Linker<*, *> {
            if (model is RoleGroup && parent is ModelLinker<*, *, *> && connectionManager != null) {
                return RoleGroupLinker(model, connectionManager, parent)
            } else throw IllegalArgumentException("Cannot create ${info.name} linker for model element ${model::class}")
        }
    }
}
