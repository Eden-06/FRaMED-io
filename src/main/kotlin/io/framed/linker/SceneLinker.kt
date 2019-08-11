package io.framed.linker

import de.westermann.kobserve.property.FunctionAccessor
import de.westermann.kobserve.property.constProperty
import de.westermann.kobserve.property.property
import de.westermann.kobserve.property.validate
import io.framed.framework.*
import io.framed.framework.linker.*
import io.framed.framework.model.ModelElement
import io.framed.framework.pictogram.*
import io.framed.framework.util.*
import io.framed.framework.view.*
import io.framed.model.Attribute
import io.framed.model.Compartment
import io.framed.model.Package
import io.framed.model.Scene

/**
 * @author lars
 */
class SceneLinker(
        override val model: Scene,
        override val connectionManager: ConnectionManager,
        override val parent: ModelLinker<*, *, *>
) : ModelLinker<Scene, BoxShape, BoxShape> {

    override val nameProperty = property(model::name)
            .validate(RegexValidator.IDENTIFIER::validate)
            .trackHistory()
    override var name by nameProperty

    override val container: BoxShape = boxShape(BoxShape.Position.ABSOLUTE) { }

    private val attributes = shapeBox<Attribute, AttributeLinker>(model::attributes, connectionManager)

    private val children = shapeBox(model::children, connectionManager) { box ->
        box.view = container
        box.onChildrenChange {
            updateSize()
        }
    }
    override val shapeLinkers: Set<ShapeLinker<*, *>>
        get() = children.linkers.toSet()

    override val subTypes: Set<String>
        get() = (attributes.linkers.flatMap { it.subTypes } + shapeLinkers.flatMap { it.subTypes }).toSet() + model.name

    override lateinit var autoLayoutBox: BoxShape
    override lateinit var borderBox: BoxShape
    override val borderShapes = mutableListOf<Shape>()

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
                    width = box(Border.DEFAULT_WIDTH, 0.0, 0.0, 0.0)
                    color = box(color(0, 0, 0, 0.3))
                }
                padding = box(8.0)
            }
        }

        autoLayoutBox = boxShape(BoxShape.Position.VERTICAL) {
            style {
                border {
                    style = Border.BorderStyle.SOLID
                    width = box(Border.DEFAULT_WIDTH, 0.0, 0.0, 0.0)
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
            background = linearGradient("to bottom") {
                add(color("#e5ffd9"), 0.0)
                add(color("#edf6e2"), 1.0)
            }
            border {
                style = Border.BorderStyle.SOLID
                width = box(Border.DEFAULT_WIDTH)
                color = box(color(0, 0, 0, 0.3))
                leftDoubleBar = true
            }
        }

        resizeable = true

        layerProperty.onChange {
            updatePreviewType()
        }

        onAction {
            ControllerManager.display(this@SceneLinker)
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

    private lateinit var sidebarAttributes: SidebarGroup
    private lateinit var sidebarAttributesAdd: ListView
    private val sidebarAttributesList: MutableList<SidebarEntry<Attribute>> = mutableListOf()

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

        parent.redraw(this)
    }

    override val sidebar = sidebar {
        title("Scene")
        group("General") {
            input("Name", nameProperty)
        }
        sidebarAttributes = group("Attributes") {
            collapse()
            sidebarAttributesAdd = custom {
                iconView(MaterialIcon.ADD)
                textView("Add attribute")
                onClick {
                    attributes += AttributeLinker(Attribute(), this@SceneLinker)
                }
            }
        }
        sidebarActionsGroup = group("Actions") {
            button("Auto layout") {
                Layouting.autoLayout(
                        container,
                        connectionManager.connections.asSequence().map { it.pictogram }.toSet(),
                        this@SceneLinker
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
                        this@SceneLinker
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

    private lateinit var contextStepIn: ListView
    private lateinit var contextStepOut: ListView

    override val contextMenu = defaultContextMenu {
        contextStepIn = addItem(MaterialIcon.ARROW_FORWARD, "Step in") {
            ControllerManager.display(this@SceneLinker)
        }
        contextStepOut = addItem(MaterialIcon.ARROW_BACK, "Step out") {
            ControllerManager.display(parent)
        }
    }

    override fun remove(linker: ShapeLinker<*, *>) {
        when (linker) {
            is AttributeLinker -> attributes.remove(linker)
            in children -> children -= linker
            else -> super.remove(linker)
        }
        updatePorts()
    }

    override fun add(model: ModelElement<*>): ShapeLinker<*, *> {
        val linker = LinkerManager.createLinker<ShapeLinker<*, *>>(model, this, connectionManager)
        when (linker) {
            is AttributeLinker -> attributes.add(linker)
            else -> children += linker
        }
        updatePorts()
        return linker
    }

    override fun redraw(linker: ShapeLinker<*, *>) {
        children.redraw(linker)
        updatePorts()
    }

    override fun ContextMenu.onOpen(event: ContextEvent) {
        contextStepIn.display = event.target == pictogram
        contextStepOut.display = event.target != pictogram
    }

    /**
     * Update the sidebar attribute overview. Add/remove missing/old items.
     */
    private fun updateSidebarAttributes() {
        while (sidebarAttributesList.size > attributes.linkers.size) {
            val last = sidebarAttributesList.last()
            last.remove()
            sidebarAttributesList -= last
        }

        for (i in 0 until sidebarAttributesList.size) {
            sidebarAttributesList[i].bind(attributes.linkers[i])
        }

        for (i in sidebarAttributesList.size until attributes.linkers.size) {
            sidebarAttributesList += SidebarEntry(sidebarAttributes, attributes.linkers[i])
        }

        sidebarAttributes.toForeground(sidebarAttributesAdd)
    }

    /**
     * The model initializes a new instance of the linker
     */
    init {
        attributes.view.visibleProperty.bind(isCompleteViewProperty)
        children.view.visibleProperty.bind(isCompleteViewProperty)
        children.previewBox?.visibleProperty?.bind(isCompleteViewProperty)

        pictogram.onAutoSize {
            updateSize()
        }

        model.attributes.forEach { attributes += AttributeLinker(it, this) }
        for (element in model.children) {
            add(element)
        }

        LinkerManager.setup(this)
        connectionManager.addModel(this)

        connectionManager.onConnectionAdd { updatePorts() }
        connectionManager.onConnectionRemove { updatePorts() }
        updatePorts()

        updateSidebarAttributes()

        attributes.view.onAdd {
            updateSidebarAttributes()
        }
        attributes.view.onRemove {
            updateSidebarAttributes()
        }
    }

    companion object : LinkerInfoItem {

        override val info = ElementInfo("Scene", FramedIcon.SCENE)

        override fun canCreateIn(container: ModelElement<*>): Boolean {
            return container is Package || container is Compartment || container is Scene
        }

        override fun isLinkerFor(element: ModelElement<*>): Boolean = element is Scene
        override fun isLinkerFor(linker: Linker<*, *>): Boolean = linker is SceneLinker

        override fun createModel(): ModelElement<*> = Scene()
        override fun createLinker(model: ModelElement<*>, parent: Linker<*, *>, connectionManager: ConnectionManager?): Linker<*, *> {
            if (model is Scene && parent is ModelLinker<*, *, *> && connectionManager != null) {
                return SceneLinker(model, connectionManager, parent)
            } else throw IllegalArgumentException("Cannot create ${info.name} linker for model element ${model::class}")
        }
    }
}
