package io.framed.linker

import de.westermann.kobserve.property.*
import io.framed.framework.*
import io.framed.framework.linker.*
import io.framed.framework.model.ModelElement
import io.framed.framework.pictogram.*
import io.framed.framework.util.*
import io.framed.framework.view.*
import io.framed.model.Package

/**
 * @author lars
 */
class PackageLinker(
        override val model: Package,
        override val connectionManager: ConnectionManager,
        override val parent: ModelLinker<*, *, *>? = null
) : ModelLinker<Package, BoxShape, BoxShape> {

    override val nameProperty = property(model::name)
            .validate(RegexValidator.IDENTIFIER::validate)
            .trackHistory()
    override var name by nameProperty

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
        get() = shapeLinkers.flatMap { it.subTypes }.toSet()

    override lateinit var autoLayoutBox: BoxShape
    override lateinit var borderBox: BoxShape
    override val borderShapes = mutableListOf<Shape>()

    override val pictogram = boxShape {
        boxShape {
            textShape(nameProperty)
            style {
                padding = box(8.0)
                notch = true
                border {
                    style = Border.BorderStyle.SOLID
                    width = box(Border.DEFAULT_WIDTH, Border.DEFAULT_WIDTH, 0.0, Border.DEFAULT_WIDTH)
                    color = box(color(0, 0, 0, 0.3))
                }
            }
        }

        autoLayoutBox = boxShape(BoxShape.Position.VERTICAL) {
            style {
                border {
                    style = Border.BorderStyle.SOLID
                    width = box(Border.DEFAULT_WIDTH)
                    color = box(color(0, 0, 0, 0.3))
                }
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
            background = color("#fffbd9")
            border {
                style = Border.BorderStyle.SOLID
                width = box(0.0, 0.0, Border.DEFAULT_WIDTH, 0.0)
                color = box(color(0, 0, 0, 0.3))
            }
            topNotch = true
        }

        resizeable = true

        layerProperty.onChange {
            updatePreviewType()
        }

        onAction {
            ControllerManager.display(this@PackageLinker)
        }
    }

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

        override fun get(): Boolean = isFlatPreviewStringProperty.value?.toBoolean() ?: true
    }, isFlatPreviewStringProperty)
    private var isFlatPreview by isFlatPreviewProperty

    private lateinit var sidebarActionsGroup: SidebarGroup
    private lateinit var sidebarPreviewGroup: SidebarGroup
    private lateinit var sidebarViewGroup: SidebarGroup

    private val creationProperty = property(model.metadata::creationDate)
    private val creationStringProperty = creationProperty.mapBinding { it.toUTCString() }

    private val modifiedProperty = property(model.metadata::modifiedDate)
    private val modifiedStringProperty = modifiedProperty.mapBinding { it.toUTCString() }

    private val authorProperty = property(model.metadata::author)

    private fun updatePreviewType() {
        val shapeIsFlat = autoLayoutBox.position == BoxShape.Position.ABSOLUTE
        if (shapeIsFlat == isFlatPreview) return

        autoLayoutBox.position = if (isFlatPreview) {
            BoxShape.Position.ABSOLUTE
        } else {
            BoxShape.Position.VERTICAL
        }

        autoLayoutBox.clear()
        children.addAllPreviews()

        parent?.redraw(this)
    }

    override val sidebar = sidebar {
        title("Package")
        group("General") {
            input("Name", nameProperty)
        }
        sidebarActionsGroup = group("Actions") {
            button("Auto layout") {
                Layouting.autoLayout(
                        container,
                        connectionManager.connections.asSequence().map { it.pictogram }.toSet(),
                        this@PackageLinker
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
                        this@PackageLinker
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

        group("Metadata") {
            input("Creation date", creationStringProperty)
            input("Modified date", modifiedStringProperty)
            input("Author", authorProperty)

            collapse()
        }
    }

    override fun Sidebar.onOpen(event: SidebarEvent) {
        val isTargetRoot = event.target == pictogram
        sidebarActionsGroup.display = event.target == container
        sidebarViewGroup.display = isTargetRoot
        sidebarPreviewGroup.display = isTargetRoot
    }

    private lateinit var contextStepIn: ListView
    private lateinit var contextStepOut: ListView

    override val contextMenu = defaultContextMenu {
        contextStepIn = addItem(MaterialIcon.ARROW_FORWARD, "Step in") {
            ControllerManager.display(this@PackageLinker)
        }
        contextStepOut = addItem(MaterialIcon.ARROW_BACK, "Step out") {
            parent?.let { ControllerManager.display(it) }
        }
    }

    override fun remove(linker: ShapeLinker<*, *>) {
        if (linker in children) {
            children -= linker
        } else super.remove(linker)
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

    override fun ContextMenu.onOpen(event: ContextEvent) {
        contextStepIn.display = event.target == pictogram
        contextStepOut.display = event.target != pictogram && parent != null
    }

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

        override val info = ElementInfo("Package", FramedIcon.PACKAGE)

        override fun canCreateIn(container: ModelElement): Boolean {
            return container is Package
        }

        override fun isLinkerFor(element: ModelElement): Boolean = element is Package
        override fun isLinkerFor(linker: Linker<*, *>): Boolean = linker is PackageLinker

        override fun createModel(): ModelElement = Package()
        override fun createLinker(model: ModelElement, parent: Linker<*, *>, connectionManager: ConnectionManager?): Linker<*, *> {
            if (model is Package && parent is ModelLinker<*, *, *> && connectionManager != null) {
                return PackageLinker(model, connectionManager, parent)
            } else throw IllegalArgumentException("Cannot create ${info.name} linker for model element ${model::class}")
        }
    }
}
