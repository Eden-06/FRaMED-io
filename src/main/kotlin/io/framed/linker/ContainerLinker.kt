package io.framed.linker

import io.framed.framework.*
import io.framed.framework.pictogram.*
import io.framed.framework.util.EventHandler
import io.framed.framework.util.LinkerBox
import io.framed.framework.util.RegexValidator
import io.framed.framework.util.property
import io.framed.framework.view.*
import io.framed.model.*

/**
 * @author lars
 */
class ContainerLinker(
        override val model: Container,
        override val parent: ContainerLinker? = null
) : ModelLinker<Container, BoxShape, TextShape> {

    override val nameProperty = property(model::name, RegexValidator("[a-zA-Z]([a-zA-Z0-9 ])*".toRegex()))
    override var name by nameProperty

    override val container: BoxShape = boxShape { }

    val classes = LinkerBox<Class, ClassLinker>(model::classes).also { box ->
        box.view = container
    }
    val containers = LinkerBox<Container, ContainerLinker>(model::containers).also { box ->
        box.view = container
    }
    val roleTypes = LinkerBox<RoleType, RoleTypeLinker>(model::roleTypes).also { box ->
        box.view = container
    }
    val events = LinkerBox<Event, EventLinker>(model::events).also { box ->
        box.view = container
    }

    override val content: List<PreviewLinker<*, *, *>>
        get() = classes.linkers + containers.linkers + roleTypes.linkers

    override val connectable: List<Linker<*, *>>
        get() = classes.linkers + containers.linkers + roleTypes.linkers + events.linkers

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
            acceptRelation = true
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

        contextDelete = addItem(MaterialIcon.DELETE, "Delete") { event ->
            parent?.let {
                it.containers -= this@ContainerLinker
                //application.linker = it
            }
        }
    }

    override fun ContextMenu.onOpen(event: ContextEvent) {
        contextStepIn.visible = event.target == pictogram
        contextStepOut.visible = event.target != pictogram && parent != null
        contextDelete.visible = parent != null
    }

    var relations: List<RelationLinker> = emptyList()

    override val connections: List<ConnectionLinker<*, *>>
        get() = relations

    override val onConnectionAdd = EventHandler<ConnectionLinker<*, *>>()
    override val onConnectionRemove = EventHandler<ConnectionLinker<*, *>>()
    override val setPosition = EventHandler<SetPosition>()

    private fun addRelation(linker: RelationLinker) {
        if (!model.relations.contains(linker.model)) {
            model.relations += linker.model
        }

        relations += linker
        onConnectionAdd.fire(linker)
    }

    fun removeRelation(linker: RelationLinker) {
        model.relations -= linker.model
        relations -= linker
        onConnectionRemove.fire(linker)
    }

    override fun createConnection(source: Shape, target: Shape) {
        val sourceId = getIdByShape(source)
        val targetId = getIdByShape(target)

        if (sourceId != null && targetId != null) {
            addRelation(RelationLinker(Relation(sourceId, targetId), this))
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

        model.relations.forEach { addRelation(RelationLinker(it, this)) }

        LinkerManager.setup(this)
        ControllerManager.register(this)
    }

    companion object : LinkerInfoItem {
        override fun canCreate(container: Linker<*, *>): Boolean = container is ContainerLinker
        override val name: String = "Container"
    }
}