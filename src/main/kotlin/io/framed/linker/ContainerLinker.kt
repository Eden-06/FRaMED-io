package io.framed.linker

import io.framed.model.*
import io.framed.picto.*
import io.framed.util.*
import io.framed.view.*

/**
 * @author lars
 */
class ContainerLinker(
        val container: Container,
        val application: Application,
        override val parent: ContainerLinker? = null
) : Linker<BoxShape>(container, parent) {

    val nameProperty = property(container::name, RegexValidator("[a-zA-Z]([a-zA-Z0-9])*".toRegex()))
    var name by nameProperty

    override fun internalCreateSidebar(): Sidebar = Sidebar(application)

    private lateinit var contentBox: BoxShape

    override val picto = boxShape {
        boxShape {
            textShape(nameProperty)
        }
        contentBox = boxShape { }

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

        hasSidebar = true
        hasContext = true
    }.also(this::initPicto)

    operator fun get(clazz: Class): Shape = classMap[clazz]?.first?.picto ?: throw IllegalArgumentException()
    operator fun get(clazzId: Long): Shape = classMap.entries.find { it.key.id == clazzId }?.value?.first?.picto
            ?: throw IllegalArgumentException()

    val viewModel = ViewModel(boxShape {
        hasSidebar = true
        hasContext = true

        layer = Layer()
    }.also(this::initPicto))

    /**
     * The map stores all classes and their related linkers and textshapes
     */
    private var classMap: Map<Class, Pair<ClassLinker, TextShape>> = emptyMap()

    /**
     * The method adds a new class to the current container
     */
    fun addClass(clazz: Class, position: Point = Point.ZERO): ClassLinker {
        // As normal view
        val linker = ClassLinker(clazz, this)
        viewModel.container += linker.picto
        viewModel.layer[linker.picto] = Dimension(position.x, position.y)

        // As list entry
        val input = contentBox.textShape(linker.nameProperty)

        classMap += clazz to (linker to input)
        return linker
    }

    /**
     * The method removes a class of the current container
     */
    fun removeClass(clazz: Class) {
        classMap[clazz]?.let { (linker, input) ->
            // As normal view
            viewModel.container -= linker.picto
            viewModel.layer[linker.picto] = null

            // As list entry
            contentBox -= input

            classMap -= clazz
            container.classes -= clazz

            container.relations.filter { it.sourceId == clazz.id || it.targetId == clazz.id }.forEach(this::removeRelation)
        }
        showSidebar()
    }

    /**
     * The map stores all role types and their related linkers and textshapes
     */
    private var roleTypeMap: Map<RoleType, Pair<RoleTypeLinker, TextShape>> = emptyMap()

    /**
     * The method adds a new class to the current container
     */
    fun addRoleType(type: RoleType, position: Point = Point.ZERO): RoleTypeLinker {
        // As normal view
        val linker = RoleTypeLinker(type, this)
        viewModel.container += linker.picto
        viewModel.layer[linker.picto] = Dimension(position.x, position.y)

        // As list entry
        val input = contentBox.textShape(linker.nameProperty)

        roleTypeMap += type to (linker to input)
        return linker
    }

    /**
     * The method removes a class of the current container
     */
    fun removeRoleType(type: RoleType) {
        roleTypeMap[type]?.let { (linker, input) ->
            // As normal view
            viewModel.container -= linker.picto
            viewModel.layer[linker.picto] = null

            // As list entry
            contentBox -= input

            roleTypeMap -= type
            container.roleTypes -= type
        }
        showSidebar()
    }

    /**
     * The map stores all containers
     */
    private var containerMap: Map<Container, Pair<ContainerLinker, TextShape>> = emptyMap()

    /**
     * The method adds a new container to the linker
     */
    private fun addContainer(cont: Container, position: Point = Point.ZERO): ContainerLinker {
        // As normal view
        val linker = ContainerLinker(cont, application, this)
        viewModel.container += linker.picto
        viewModel.layer[linker.picto] = Dimension(position.x, position.y)

        // As list entry
        val input = contentBox.textShape(linker.nameProperty)

        containerMap += cont to (linker to input)
        return linker
    }

    /**
     * The method removes a container of the linker
     */
    private fun removeContainer(cont: Container) {
        containerMap[cont]?.let { (linker, input) ->
            // As normal view
            viewModel.container -= linker.picto
            viewModel.layer[linker.picto] = null

            // As list entry
            contentBox -= input

            containerMap -= cont
            container.containers -= cont
        }

        showSidebar()
    }

    /**
     * The map contains all events of the current model and their related controllers and shapes.
     */
    private var eventMap: Map<Event, Pair<EventLinker, IconShape>> = emptyMap()

    /**
     * The method adds a new event to the linker
     */
    private fun addEvent(evt: Event, position: Point = Point.ZERO): EventLinker {
        // As normal view
        val linker = EventLinker(evt, this)
        viewModel.container += linker.picto
        viewModel.layer[linker.picto] = Dimension(position.x, position.y)

        // As list entry
        val input = contentBox.iconShape(linker.symbolProperty)

        eventMap += evt to (linker to input)
        return linker
    }

    /**
     * The method removes an event of the linker
     */
    fun removeEvent(evt: Event) {
        eventMap[evt]?.let { (linker, input) ->
            // As normal view
            viewModel.container -= linker.picto
            viewModel.layer[linker.picto] = null

            // As list entry
            contentBox -= input

            eventMap -= evt
            container.events -= evt
        }

        showSidebar()
    }

    /**
     * The map stores all relations and their related linkers
     */
    private var relationMap: Map<Relation, RelationLinker> = emptyMap()

    /**
     * The method adds a new relation and returns a new controller
     * @param relation new relation
     * @return new linker to manage the relation
     */
    private fun addRelation(relation: Relation): RelationLinker {
        val linker = RelationLinker(relation, this)
        viewModel += linker.picto

        relationMap += relation to linker
        return linker
    }

    /**
     * The method deletes a relation
     * @param relation relation to delete
     */
    fun removeRelation(relation: Relation) {
        relationMap[relation]?.let { linker ->
            viewModel -= linker.picto

            relationMap -= relation
            container.relations -= relation
        }

        showSidebar()
    }

    private lateinit var sidebarActionsGroup: SidebarGroup

    override fun createSidebar(sidebar: Sidebar) = sidebar.setup {
        title("Container")
        group("General") {
            input("Name", nameProperty)
        }
        sidebarActionsGroup = group("Actions") {
            button("Auto layout") {
                //autoLayout()
            }
            button("Reset zoom") {
                application.renderer.zoomTo(1.0)
            }
            button("Reset pan") {
                application.renderer.panTo(Point.ZERO)
            }
        }
    }

    override fun prepareSidebar(sidebar: Sidebar, event: SidebarEvent) {
        val h = event.target != picto
        sidebarActionsGroup.visible = h
    }

    override fun createContextMenu(event: ContextEvent): ContextMenu? = contextMenu {
        title = "Package: $name"

        if (event.target == picto) {

            addItem(MaterialIcon.ARROW_FORWARD, "Step in") {
                application.linker = this@ContainerLinker
            }
        } else {
            parent?.let {
                addItem(MaterialIcon.ARROW_BACK, "Step out") {
                    application.linker = it
                }
            }
        }

        addItem(MaterialIcon.ADD, "Add class") {
            val c = Class()
            c.name = "Unnamed class"

            container.classes += c

            addClass(c, event.position)
        }
        addItem(MaterialIcon.ADD, "Add package") {
            val c = Container()
            c.name = "Unnamed package"

            container.containers += c

            addContainer(c, event.position)
        }
        parent?.let {
            addItem(MaterialIcon.DELETE, "Delete") {
                it.removeContainer(container)
                application.linker = it
            }
        }
    }

    /**
     * The method initializes a new instance of the linker
     */
    init {
        container.classes.forEach { addClass(it) }

        container.containers.forEach { addContainer(it) }

        container.relations.forEach { addRelation(it) }

        container.roleTypes.forEach { addRoleType(it) }

        container.events.forEach { addEvent(it) }

        viewModel.onRelationDraw { (sourceShape, targetShape) ->
            val source = classMap.entries.find { (_, pair) ->
                pair.first.picto == sourceShape
            }?.key
            val target = classMap.entries.find { (_, pair) ->
                pair.first.picto == targetShape
            }?.key

            if (source != null && target != null) {
                val relation = Relation(source.id, target.id)
                container.relations += relation

                addRelation(relation)
            }
        }
    }
}