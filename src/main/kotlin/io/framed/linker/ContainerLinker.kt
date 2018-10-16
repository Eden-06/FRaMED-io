package io.framed.linker

import io.framed.framework.*
import io.framed.framework.pictogram.*
import io.framed.framework.util.*
import io.framed.framework.view.*
import io.framed.model.*

/**
 * @author lars
 */
class ContainerLinker(
        override val model: Container,
        override val parent: ContainerLinker? = null
) : ModelLinker<Container, BoxShape, TextShape> {

    override val nameProperty = property(model::name, RegexValidator("[a-zA-Z]([a-zA-Z0-9 ])*".toRegex())).trackHistory()
    override var name by nameProperty

    override val container: BoxShape = boxShape { }

    val classes = LinkerShapeBox<Class, ClassLinker>(model::classes).also { box ->
        box.view = container
    }
    val compartments = LinkerShapeBox<Compartment, CompartmentLinker>(model::compartments).also { box ->
        box.view = container
    }
    val containers = LinkerShapeBox<Container, ContainerLinker>(model::containers).also { box ->
        box.view = container
    }
    val roleTypes = LinkerShapeBox<RoleType, RoleTypeLinker>(model::roleTypes).also { box ->
        box.view = container
    }
    val events = LinkerShapeBox<Event, EventLinker>(model::events).also { box ->
        box.view = container
    }

    val associations = LinkerConnectionBox<Association, AssociationLinker>(model::associations, this)
    val inheritances = LinkerConnectionBox<Inheritance, InheritanceLinker>(model::inheritances, this)
    val aggregations = LinkerConnectionBox<Aggregation, AggregationLinker>(model::aggregations, this)
    val compositions = LinkerConnectionBox<Composition, CompositionLinker>(model::compositions, this)

    override val content: List<PreviewLinker<*, *, *>>
        get() = classes.linkers + containers.linkers + roleTypes.linkers

    override val connectable: List<Linker<*, *>>
        get() = classes.linkers + containers.linkers + roleTypes.linkers + events.linkers

    override val connections: List<ConnectionLinker<*>>
        get() = associations.linkers + inheritances.linkers + aggregations.linkers + compositions.linkers

    override val pictogram = boxShape {
        boxShape {
            textShape(nameProperty)
        }

        val box = boxShape { }
        classes.previewBox = box
        containers.previewBox = box
        roleTypes.previewBox = box
        //events.previewBox = box

        style {
            background = linearGradient("to bottom") {
                add(color("#fffbd9"), 0.0)
                add(color("#fff7c4"), 1.0)
            }
            border {
                style = Border.BorderStyle.SOLID
                width = 1.0
                color = color(0, 0, 0, 0.3)
            }
        }
    }

    override val preview: TextShape = textShape(nameProperty)

    private lateinit var sidebarActionsGroup: SidebarGroup

    private val creationProperty = property(model.metadata::creationDate)
    private val creationStringProperty = property(creationProperty,
            getter = {
                creationProperty.get().toUTCString()
            }
    )

    private val modifiedProperty = property(model.metadata::modifiedDate)
    private val modifiedStringProperty = property(modifiedProperty,
            getter = {
                modifiedProperty.get().toUTCString()
            }
    )

    private val authorProperty = property(model.metadata::author)

    override val sidebar = sidebar {
        title("Container")
        group("General") {
            input("Name", nameProperty)
        }
        sidebarActionsGroup = group("Actions") {
            button("Auto layout") {
                //autoLayout()
            }
            button("Reset zoom") {
                //application.renderer.zoomTo(1.0)
            }
            button("Reset pan") {
                //application.renderer.panTo(Point.ZERO)
            }
        }

        group("Metadata") {
            input("Creation date", creationStringProperty)
            input("Modified date", modifiedStringProperty)
            input("Author", authorProperty)

            collapse()
        }
    }

    override fun Sidebar.onOpen(event: SidebarEvent) {
        val h = event.target != pictogram
        sidebarActionsGroup.visible = h
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
            parent?.let(ControllerManager::display)
        }

        addItem(MaterialIcon.ADD, "Add class") { event ->
            this@ContainerLinker.classes += ClassLinker(Class(), this@ContainerLinker).also {
                setPosition.fire(SetPosition(it, event.position))
                it.focus()
            }
        }
        addItem(MaterialIcon.ADD, "Add package") { event ->
            containers += ContainerLinker(Container()).also {
                setPosition.fire(SetPosition(it, event.position))
                it.focus()
            }
        }

        contextDelete = addItem(MaterialIcon.DELETE, "Delete") { _ ->
            delete()
        }
    }

    override fun delete() {
        parent?.let {
            it.containers -= this@ContainerLinker
        }
    }

    override fun ContextMenu.onOpen(event: ContextEvent) {
        contextStepIn.visible = event.target == pictogram
        contextStepOut.visible = event.target != pictogram && parent != null
        contextDelete.visible = parent != null
    }

    override val onConnectionAdd = EventHandler<ConnectionLinker<*>>()
    override val onConnectionRemove = EventHandler<ConnectionLinker<*>>()
    override val setPosition = EventHandler<SetPosition>()

    override fun createConnection(source: Shape, target: Shape) {
        val types = canConnectionCreate(source, target)

        if (types.isEmpty()) return


        if (types.size == 1) {
            createConnection(source, target, types.first())
        } else {
            CyclicChooser(types) { type ->
                iconView(type.icon)
                textView(type.name)

                onClick {
                    createConnection(source, target, type)
                }
            }
        }
    }

    override fun createConnection(source: Shape, target: Shape, type: ConnectionInfo): ConnectionLinker<*> {
        val sourceId = getIdByShape(source) ?: throw IllegalArgumentException()
        val targetId = getIdByShape(target) ?: throw IllegalArgumentException()

        return when (type) {
            AssociationLinker.info -> AssociationLinker(Association(sourceId, targetId), this).also(associations::add)
            AggregationLinker.info -> AggregationLinker(Aggregation(sourceId, targetId), this).also(aggregations::add)
            InheritanceLinker.info -> InheritanceLinker(Inheritance(sourceId, targetId), this).also(inheritances::add)
            CompositionLinker.info -> CompositionLinker(Composition(sourceId, targetId), this).also(compositions::add)
            else -> throw IllegalArgumentException()
        }
    }

    /**
     * The model initializes a new instance of the linker
     */
    init {
        model.classes.forEach { classes += ClassLinker(it, this) }
        model.containers.forEach { containers += ContainerLinker(it, this) }
        model.roleTypes.forEach { roleTypes += RoleTypeLinker(it, this) }
        model.events.forEach { events += EventLinker(it, this) }

        model.associations.forEach { associations += AssociationLinker(it, this) }
        model.aggregations.forEach { aggregations += AggregationLinker(it, this) }
        model.inheritances.forEach { inheritances += InheritanceLinker(it, this) }
        model.compositions.forEach { compositions += CompositionLinker(it, this) }

        LinkerManager.setup(this)
        ControllerManager.register(this)
    }

    companion object : LinkerInfoItem {
        override fun canCreate(container: Linker<*, *>): Boolean = container is ContainerLinker
        override val name: String = "Container"
    }
}
