package io.framed.linker

import Layouting
import de.westermann.kobserve.basic.*
import io.framed.framework.*
import io.framed.framework.pictogram.*
import io.framed.framework.util.Point
import io.framed.framework.util.RegexValidator
import io.framed.framework.util.shapeBox
import io.framed.framework.util.trackHistory
import io.framed.framework.view.*
import io.framed.model.Class
import io.framed.model.Compartment
import io.framed.model.Event
import io.framed.model.Package
import kotlin.math.roundToInt

/**
 * @author lars
 */
class PackageLinker(
        override val model: Package,
        override val connectionManager: ConnectionManager,
        override val parent: ModelLinker<*, *, *>? = null
) : ModelLinker<Package, BoxShape, TextShape> {

    override val nameProperty = property(model::name)
            .validate(RegexValidator("[a-zA-Z]([a-zA-Z0-9 ])*".toRegex())::validate)
            .trackHistory()
    override var name by nameProperty

    override val container: BoxShape = boxShape(BoxShape.Position.ABSOLUTE) { }

    private val children = shapeBox(model::children, connectionManager) { box ->
        box.view = container
    }

    override val shapeLinkers: Set<ShapeLinker<*, *>>
        get() = children.linkers.toSet()

    override val subTypes: Set<String>
        get() = shapeLinkers.flatMap { it.subTypes }.toSet()

    private lateinit var autoLayoutBox: BoxShape
    private lateinit var borderBox: BoxShape
    override val pictogram = boxShape {
        boxShape {
            textShape(nameProperty)
            style {
                padding = box(8.0)
                notch = true
                border {
                    style = Border.BorderStyle.SOLID
                    width = box(1.0, 1.0, 0.0, 1.0)
                    color = box(color(0, 0, 0, 0.3))
                }
            }
        }

        autoLayoutBox = boxShape(BoxShape.Position.VERTICAL) {
            style {
                border {
                    style = Border.BorderStyle.SOLID
                    width = box(1.0)
                    color = box(color(0, 0, 0, 0.3))
                }
                padding = box(8.0)
                stretchHeight = true
            }
        }
        children.previewBox = autoLayoutBox

        borderBox = boxShape(BoxShape.Position.BORDER) {}

        children.conditionalContainer(borderBox) {
            it is EventLinker && it.returnEvent
        }

        style {
            background = color("#fffbd9")
            border {
                style = Border.BorderStyle.SOLID
                width = box(0.0, 0.0, 1.0, 0.0)
                color = box(color(0, 0, 0, 0.3))
            }
            topNotch = true
        }

        resizeable = true

        layerProperty.onChange {
            updatePreviewType()
        }
    }

    override val preview = textShape(nameProperty)

    private val isCompleteViewStringProperty = pictogram.data("complete-view")
    private val isCompleteViewProperty = property(object : FunctionAccessor<Boolean> {
        val default: Boolean
            get() = pictogram.parent?.parent == null

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

            /*
            button("Log") {
                fun log(shape: Shape): dynamic {
                    val h = js("{}")
                    h.type = shape::class.simpleName
                    h.width = shape.width
                    h.height = shape.height
                    h.id = shape.id?.toInt()
                    if (shape is BoxShape) {
                        h.children = js("[]")
                        for (s in shape.shapes) {
                            h.children.push(log(s))
                        }
                    }
                    return h
                }

                console.log(log(pictogram))
            }
            */
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
            checkBox("Complete view", isCompleteViewProperty, CheckBox.Type.SWITCH)
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
    }

    private lateinit var contextStepIn: ListView
    private lateinit var contextStepOut: ListView
    private lateinit var contextDelete: ListView

    override val contextMenu = contextMenu {
        titleProperty.bind(nameProperty.mapBinding { "Package: $it" })

        contextStepIn = addItem(MaterialIcon.ARROW_FORWARD, "Step in") {
            ControllerManager.display(this@PackageLinker)
        }
        contextStepOut = addItem(MaterialIcon.ARROW_BACK, "Step out") {
            parent?.let { ControllerManager.display(it) }
        }

        addItem(MaterialIcon.ADD, "Add class") { event ->
            add(Class(), LayerData(event.diagram.x, event.diagram.y), event.target).focus(event.target)
        }
        addItem(MaterialIcon.ADD, "Add package") { event ->
            add(Package(), LayerData(event.diagram.x, event.diagram.y), event.target).focus(event.target)
        }
        addItem(MaterialIcon.ADD, "Add event") { event ->
            add(Event(), LayerData(event.diagram.x, event.diagram.y), event.target).focus(event.target)
        }
        addItem(MaterialIcon.ADD, "Add compartment") { event ->
            add(Compartment(), LayerData(event.diagram.x, event.diagram.y), event.target).focus(event.target)
        }

        contextDelete = addItem(MaterialIcon.DELETE, "Delete") {
            delete()
        }
    }

    override fun remove(linker: ShapeLinker<*, *>) {
        if (linker is ClassLinker || linker is CompartmentLinker || linker is PackageLinker || linker is EventLinker) {
            children -= linker
        } else super.remove(linker)
        checkBorder()
    }


    override fun add(model: ModelElement<*>): ShapeLinker<*, *> {
        val linker = when (model) {
            is Class -> ClassLinker(model, this)
            is Compartment -> CompartmentLinker(model, connectionManager, this)
            is Package -> PackageLinker(model, connectionManager, this)
            is Event -> EventLinker(model, this)
            else -> super.add(model)
        }
        children += linker
        checkBorder()
        return linker
    }


    override fun redraw(linker: ShapeLinker<*, *>) {
        when (linker) {
            is ClassLinker -> children.redraw(linker)
            is CompartmentLinker -> children.redraw(linker)
            is PackageLinker -> children.redraw(linker)
            is EventLinker -> children.redraw(linker)

            else -> super.remove(linker)
        }
        checkBorder()
    }

    override fun ContextMenu.onOpen(event: ContextEvent) {
        contextStepIn.display = event.target == pictogram
        contextStepOut.display = event.target != pictogram && parent != null
        contextDelete.display = parent != null
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

    override fun checkSize() {
        var maxH = 0.0
        val headlineHeight = this.autoLayoutBox.topOffset - this.pictogram.topOffset
        val contentHeight = this.pictogram.height - headlineHeight
        for(child in children.linkers){
            val cH = child.pictogram.topOffset + child.pictogram.height
            if(cH > contentHeight){
                maxH = cH
            }
        }
        if(maxH > 0.0){
            this.pictogram.height = maxH + 20 + headlineHeight
        }
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
        children.view.visibleProperty.bind(isCompleteViewProperty)
        children.previewBox?.visibleProperty?.bind(isCompleteViewProperty)

        for (element in model.children) {
            add(element)
        }

        LinkerManager.setup(this)
        connectionManager.addModel(this)

        connectionManager.onConnectionAdd { checkBorder() }
        connectionManager.onConnectionRemove { checkBorder() }
        checkBorder()
    }

    companion object : LinkerInfoItem {
        override fun canCreateIn(container: ModelElement<*>): Boolean {
            return container is Package
        }

        override fun isLinkerOfType(element: ModelElement<*>): Boolean = element is Package

        override val name: String = "Package"
    }
}
