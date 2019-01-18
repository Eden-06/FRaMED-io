package io.framed.linker

import Layouting
import de.westermann.kobserve.basic.*
import io.framed.framework.*
import io.framed.framework.pictogram.*
import io.framed.framework.util.*
import io.framed.framework.view.*
import io.framed.model.*
import kotlin.math.roundToInt

/**
 * @author lars
 */
class ContainerLinker(
        override val model: Container,
        override val connectionManager: ConnectionManager,
        override val parent: ModelLinker<*, *, *>? = null
) : ModelLinker<Container, BoxShape, TextShape> {

    override val nameProperty = property(model::name)
            .validate(RegexValidator("[a-zA-Z]([a-zA-Z0-9 ])*".toRegex())::validate)
            .trackHistory()
    override var name by nameProperty

    override val container: BoxShape = boxShape(BoxShape.Position.ABSOLUTE) { }

    private val classes = shapeBox<Class, ClassLinker>(model::classes, connectionManager) { box ->
        box.view = container
    }
    private val compartments = shapeBox<Compartment, CompartmentLinker>(model::compartments, connectionManager) { box ->
        box.view = container
    }
    private val containers = shapeBox<Container, ContainerLinker>(model::containers, connectionManager) { box ->
        box.view = container
    }
    private val roleTypes = shapeBox<RoleType, RoleTypeLinker>(model::roleTypes, connectionManager) { box ->
        box.view = container
    }
    private val events = shapeBox<Event, EventLinker>(model::events, connectionManager) { box ->
        box.view = container
    }

    override val shapeLinkers: Set<ShapeLinker<*, *>>
        get() = classes.linkers + containers.linkers + roleTypes.linkers + events.linkers + compartments.linkers

    private lateinit var autoLayoutBox: BoxShape
    private lateinit var borderBox: BoxShape
    override val pictogram = boxShape {
        boxShape {
            textShape(nameProperty)
            style {
                padding = box(8.0)
            }
        }

        autoLayoutBox = boxShape(BoxShape.Position.VERTICAL) {
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
        containers.previewBox = autoLayoutBox
        compartments.previewBox = autoLayoutBox
        roleTypes.previewBox = autoLayoutBox
        events.previewBox = autoLayoutBox

        borderBox = boxShape(BoxShape.Position.BORDER) {}

        events.conditionalContainer(borderBox) {
            it.returnEvent
        }

        style {
            background = linearGradient("to bottom") {
                add(color("#fffbd9"), 0.0)
                add(color("#fff7c4"), 1.0)
            }
            border {
                style = Border.BorderStyle.SOLID
                width = box(1.0)
                color = box(color(0, 0, 0, 0.3))
            }
        }

        resizeable = true

        layerProperty.onChange {
            updatePreviewType()
        }
    }

    override val listPreview = textShape(nameProperty)

    override val flatPreview = boxShape {
        textShape(nameProperty)

        style {
            background = linearGradient("to bottom") {
                add(color("#fffbd9"), 0.0)
                add(color("#fff7c4"), 1.0)
            }
            border {
                style = Border.BorderStyle.SOLID
                width = box(1.0)
                color = box(color(0, 0, 0, 0.3))
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

    private lateinit var sidebarActionsGroup: SidebarGroup
    private lateinit var sidebarPreviewGroup: SidebarGroup
    private lateinit var sidebarViewGroup: SidebarGroup
    private lateinit var sidebarFlatViewGroup: SidebarGroup

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
        this@ContainerLinker.classes.addAllPreviews()
        containers.addAllPreviews()
        compartments.addAllPreviews()
        roleTypes.addAllPreviews()
        events.addAllPreviews()

        parent?.redraw(this@ContainerLinker)
    }

    override val sidebar = sidebar {
        title("Container")
        group("General") {
            input("Name", nameProperty)
        }
        sidebarActionsGroup = group("Actions") {
            button("Auto layout") {
                Layouting.autoLayout(
                        container,
                        connectionManager.connections.asSequence().map { it.pictogram }.toSet()
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
                        connectionManager.connections.asSequence().map { it.pictogram }.toSet()
                )
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

        sidebarFlatViewGroup.display = event.target == flatPreview
    }

    private lateinit var contextStepIn: ListView
    private lateinit var contextStepOut: ListView
    private lateinit var contextDelete: ListView

    override val contextMenu = contextMenu {
        title = "Package: $name"

        contextStepIn = addItem(MaterialIcon.ARROW_FORWARD, "Step in") {
            ControllerManager.display(this@ContainerLinker)
        }
        contextStepOut = addItem(MaterialIcon.ARROW_BACK, "Step out") {
            parent?.let { ControllerManager.display(it) }
        }

        addItem(MaterialIcon.ADD, "Add class") { event ->
            val linker = ClassLinker(Class(), this@ContainerLinker)
            this@ContainerLinker.classes += linker
            linker.also {
                it.pictogram.left = event.diagram.x
                it.pictogram.top = event.diagram.y
                it.focus(event.target)
            }
        }
        addItem(MaterialIcon.ADD, "Add package") { event ->
            val linker = ContainerLinker(Container(), connectionManager, this@ContainerLinker)
            containers += linker
            linker.also {
                it.pictogram.left = event.diagram.x
                it.pictogram.top = event.diagram.y
                it.focus(event.target)
            }
        }
        addItem(MaterialIcon.ADD, "Add event") { event ->
            val linker = EventLinker(Event(), this@ContainerLinker)
            events += linker
            linker.also {
                it.pictogram.left = event.diagram.x
                it.pictogram.top = event.diagram.y
                it.focus(event.target)
            }
        }
        addItem(MaterialIcon.ADD, "Add role type") { event ->
            val linker = RoleTypeLinker(RoleType(), this@ContainerLinker)
            roleTypes += linker
            linker.also {
                it.pictogram.left = event.diagram.x
                it.pictogram.top = event.diagram.y
                it.focus(event.target)
            }
        }
        addItem(MaterialIcon.ADD, "Add compartment") { event ->
            val linker = CompartmentLinker(Compartment(), connectionManager, this@ContainerLinker)
            compartments += linker
            linker.also {
                it.pictogram.left = event.diagram.x
                it.pictogram.top = event.diagram.y
                it.focus(event.target)
            }
        }

        contextDelete = addItem(MaterialIcon.DELETE, "Delete") {
            delete()
        }
    }

    override fun remove(linker: ShapeLinker<*, *>) {
        when (linker) {
            is ClassLinker -> classes -= linker
            is CompartmentLinker -> compartments -= linker
            is ContainerLinker -> containers -= linker
            is RoleTypeLinker -> roleTypes -= linker
            is EventLinker -> events -= linker

            else -> super.remove(linker)
        }
        checkBorder()
    }


    override fun add(model: ModelElement<*>) {
        when (model) {
            is Class -> classes += ClassLinker(model, this)
            is Compartment -> compartments += CompartmentLinker(model, connectionManager, this)
            is Container -> containers += ContainerLinker(model, connectionManager, this)
            is RoleType -> roleTypes += RoleTypeLinker(model, this)
            is Event -> events += EventLinker(model, this)
            else -> super.add(model)
        }
        checkBorder()
    }


    override fun redraw(linker: ShapeLinker<*, *>) {
        when (linker) {
            is ClassLinker -> classes.redraw(linker)
            is CompartmentLinker -> compartments.redraw(linker)
            is ContainerLinker -> containers.redraw(linker)
            is RoleTypeLinker -> roleTypes.redraw(linker)
            is EventLinker -> events.redraw(linker)

            else -> super.remove(linker)
        }
        checkBorder()
    }

    override fun ContextMenu.onOpen(event: ContextEvent) {
        contextStepIn.display = event.target == pictogram
        contextStepOut.display = event.target != pictogram && parent != null
        contextDelete.display = parent != null
    }

    override fun dropShape(element: Long, target: Long) {
        val elementLinker = getLinkerById(element) ?: throw IllegalArgumentException()
        val targetLinker = getLinkerById(target) ?: throw IllegalArgumentException()

        val connectionCount = connectionManager.listConnections(elementLinker.id).size

        val elementName = elementLinker.model::class.simpleName?.toLowerCase() ?: "element"
        val targetName = targetLinker.model::class.simpleName?.toLowerCase() ?: "container"

        if (connectionCount > 0) {
            dialog {
                title = "Move $elementName to $targetName"
                contentView.textView("How should $connectionCount connection(s) be handled.")
                closable = true
                addButton("Move and delete", true) {
                    History.group("Move $elementName to $targetName") {
                        remove(elementLinker)
                        targetLinker.add(elementLinker.model.copy())
                    }
                }
                addButton("Move and keep") {
                    History.group("Move $elementName to $targetName") {
                        val connectionList = connectionManager.listConnections(elementLinker.id).map { it.model }

                        val oldId = elementLinker.id
                        remove(elementLinker)
                        val model = elementLinker.model.copy()
                        targetLinker.add(model)
                        val newId = model.id

                        connectionList.forEach {
                            if (it.sourceId == oldId) {
                                it.sourceId = newId
                            }
                            if (it.targetId == oldId) {
                                it.targetId = newId
                            }
                            connectionManager.add(it)
                        }
                    }
                }
                addButton("Abort")
            }.open()
        } else {
            History.group("Move $elementName to $targetName") {
                remove(elementLinker)
                targetLinker.add(elementLinker.model.copy())
            }
        }
    }

    private var borderShapes: List<Shape> = emptyList()

    private fun checkBorder() {
        val directIdList = shapeLinkers.map { it.id }
        var neededBorderViews = connectionManager.connections
                .mapNotNull {
                    val s = it.sourceIdProperty.value
                    val t = it.targetIdProperty.value

                    if (s in directIdList && t !in directIdList) {
                        s
                    } else if (s !in directIdList && t in directIdList) {
                        t
                    } else {
                        null
                    }
                }
                .distinct()

        borderShapes.forEach {
            val id = it.id?.unaryMinus() ?: return@forEach
            if ((id) !in neededBorderViews) {
                borderBox -= it
                borderShapes -= it
            } else {
                neededBorderViews -= id
            }
        }

        neededBorderViews.forEach { id ->
            val shape = iconShape(property<Icon?>(null), id = -id) {
                style {
                    background = color(255, 255, 255)
                    border {
                        style = Border.BorderStyle.SOLID
                        width = box(1.0)
                        color = box(color(0, 0, 0, 0.3))
                    }
                    padding = box(10.0)
                }
            }
            borderBox += shape
            borderShapes += shape
        }

        updateLabelBindings()
    }

    override fun updateLabelBindings() {
        for (shape in borderShapes) {
            var label = shape.labels.find { it.id == "name" }
            if (label == null) {
                label = Label(id = "name")
                shape.labels += label
            }

            val id = -(shape.id ?: continue)
            val linker = shapeLinkers.find { it.id == id } as? PreviewLinker<*, *, *> ?: continue

            val typeName = linker.typeName
            val name = linker.nameProperty.mapBinding { "$typeName: $it" }

            if (label.textProperty.isBound) {
                label.textProperty.unbind()
            }
            label.textProperty.bind(name)

            shape.labelsProperty.onChange.emit(Unit)
        }
    }

    /**
     * The model initializes a new instance of the linker
     */
    init {
        model.classes.forEach { classes += ClassLinker(it, this) }
        model.containers.forEach { containers += ContainerLinker(it, connectionManager, this) }
        model.roleTypes.forEach { roleTypes += RoleTypeLinker(it, this) }
        model.events.forEach { events += EventLinker(it, this) }
        model.compartments.forEach { compartments += CompartmentLinker(it, connectionManager, this) }

        LinkerManager.setup(this)
        connectionManager.addModel(this)

        connectionManager.onConnectionAdd { checkBorder() }
        connectionManager.onConnectionRemove { checkBorder() }
        checkBorder()

        isFlatPreviewProperty.onChange {
            updatePreviewType()
        }
    }

    companion object : LinkerInfoItem {
        override fun canCreate(container: Linker<*, *>): Boolean = container is ContainerLinker
        override fun contains(linker: Linker<*, *>): Boolean = linker is ContainerLinker

        override val name: String = "Container"
    }
}
