package io.framed.controller

import io.framed.JsPlumb
import io.framed.jsPlumbDragOptionsInit
import io.framed.jsPlumbDropOptionsInit
import io.framed.jsPlumbEndpointOptions
import io.framed.model.Class
import io.framed.model.Container
import io.framed.model.Model
import io.framed.model.Relation
import io.framed.util.Point
import io.framed.util.Property
import io.framed.util.async
import io.framed.util.point
import io.framed.view.*
import org.w3c.dom.Element

/**
 * @author lars
 */
class ContainerController(
        val container: Container,
        val parent: ContainerController? = null
) : Controller {

    val nameProperty = Property(container::name)
    var name by nameProperty

    private var sidebars: List<Sidebar> = emptyList()

    fun createSidebar() = Sidebar().also {
        it.application = application
        sidebars += it
    }

    override val sidebar = createSidebar()

    var application: Application? = null
        set(value) {
            field = value
            childContainer.forEach {
                it.application = value
            }

            sidebars.forEach {
                it.application = value
            }

            async {
                jsPlumbInstance.repaintEverything()
            }
        }

    fun autoLayout() {
        val padding = 20
        views.values.fold(Point(padding, padding)) { acc, controller ->
            controller.left = acc.x
            controller.top = acc.y
            acc + Point(controller.clientWidth + padding, 0)
        }

        async {
            jsPlumbInstance.repaintEverything()
        }
    }

    override val view: View<*>
        get() = listView

    val navigationView = NavigationView()
    var touchpadControl: Boolean
        get() = navigationView.touchpadControl
        set(value) {
            navigationView.touchpadControl = value
        }

    private val listView = ListView()
    private val titleList = ListView().also {
        listView += it
    }
    private val contentList = ListView().also {
        listView += it
    }

    var views: Map<Model, View<*>> = emptyMap()
    var childContainer: List<ContainerController> = emptyList()

    fun getControllerById(id: String): Controller? =
            (classMap.values.map { it.first } + containerMap.values.map { it.first }).find { it.view.id == id }

    fun getClassById(id: String): Class? {
        val controller = getControllerById(id)
        if(controller != null){
            classMap.entries.forEach {
                if(it.value.first.equals(controller)){
                    return it.key
                }
            }
        }
        return null;
    }

    val jsPlumbInstance = JsPlumb.getInstance().apply {
        setContainer(navigationView.container.html)

        navigationView.onZoom {
            setZoom(it)
        }
        setZoom(1.0)
        bind("beforeDrop", {
            val sourceClass = getClassById(it.sourceId)
            val targetClass = getClassById(it.targetId)
            if(sourceClass is Class && targetClass is Class) {
                val newRelation = Relation(sourceClass, targetClass)
                val c = addRelation(newRelation)
                this.deleteConnection(it.connection)
            }
        })
    }

    private var classMap: Map<Class, Pair<ClassController, InputView>> = emptyMap()
    fun addClass(clazz: Class, position: Point = Point.ZERO): ClassController {
        // As root view
        val c = ClassController(clazz, this)
        navigationView.container += c.view
        views += clazz to c.view

        c.view.left = position.x
        c.view.top = position.y

        c.view.onMouseDown {
            select(c.view, it.ctrlKey)
        }
        c.view.onDblClick { _ ->
            views.values.forEach { it.selectedView = false }
            c.view.selectedView = true
        }

        c.view.draggable = View.DragType.ABSOLUTE
        c.view.onDrag { event ->
            jsPlumbInstance.revalidate(c.view.html)
            navigationView.container.toForeground(c.view)

            if (event.direct) {
                val s = selectedViews() - c.view
                s.forEach { it.performDrag(event.indirect) }
            }
        }
        jsPlumbInstance.addEndpoint(c.view.html, jsPlumbEndpointOptions {
            anchors = arrayOf("Bottom")
            isSource = true
            isTarget = true
            endpoint = "Dot"

            dropOptions = jsPlumbDropOptionsInit {
                drop = {
                    // NOT WORKING
                }
            }

            dragOptions = jsPlumbDragOptionsInit() {
                drag = { e, ui ->
                    // WORKING
                }
            }
        })
        // As content view
        val input = InputView().also {
            contentList += it
        }
        input.bind(c.nameProperty)

        classMap += clazz to (c to input)
        return c
    }

    fun removeClass(clazz: Class) {
        classMap[clazz]?.let { (c, input) ->
            navigationView.container -= c.view
            contentList -= input
            container.classes -= clazz
        }

        container.relations.filter { it.source == clazz || it.target == clazz }.forEach {
            removeRelation(it)
        }
        sidebar.display()
    }

    private var containerMap: Map<Container, Pair<ContainerController, InputView>> = emptyMap()
    private fun addContainer(cont: Container, position: Point = Point.ZERO): ContainerController {
        // As root view
        val c = ContainerController(cont, this)
        c.application = application
        navigationView.container += c.listView
        views += cont to c.listView
        childContainer += c

        c.view.left = position.x
        c.view.top = position.y

        c.view.onMouseDown {
            select(c.view, it.ctrlKey)
        }

        c.view.draggable = View.DragType.ABSOLUTE
        c.view.onDrag { event ->
            jsPlumbInstance.revalidate(c.listView.html)
            navigationView.container.toForeground(c.view)

            if (event.direct) {
                val s = selectedViews() - c.view
                s.forEach { it.performDrag(event.indirect) }
            }
        }

        // As content view
        val input = InputView().also {
            contentList += it
        }
        input.bind(c.nameProperty)

        containerMap += cont to (c to input)
        return c
    }

    private fun removeContainer(cont: Container) {
        containerMap[cont]?.let { (c, input) ->
            navigationView.container += c.listView
            contentList -= input
            container.containers -= cont
        }
        sidebar.display()
    }

    private var relationMap: Map<Relation, RelationController> = emptyMap()
    private fun addRelation(relation: Relation): RelationController {
        val c = RelationController(relation, this)
        relationMap += relation to c
        return c
    }

    fun removeRelation(relation: Relation) {
        relationMap[relation]?.let {
            container.relations -= relation
            it.remove()
        }
        sidebar.display()
    }

    private fun openContextMenu(open: Boolean, client: Point) = contextMenu {
        title = "Package: $name"
        if (open) {
            addItem(MaterialIcon.ARROW_FORWARD, "Step in") {
                application?.controller = this@ContainerController
            }
        } else {
            parent?.let {
                addItem(MaterialIcon.ARROW_BACK, "Step out") {
                    application?.controller = it
                }
            }
        }
        addItem(MaterialIcon.ADD, "Add class") {
            val c = Class()
            c.name = "Unnamed class"

            container.classes += c

            addClass(c, navigationView.mouseToCanvas(client))
        }
        addItem(MaterialIcon.ADD, "Add package") {
            val c = Container()
            c.name = "Unnamed package"

            container.containers += c

            addContainer(c, navigationView.mouseToCanvas(client))
        }
        parent?.let {
            addItem(MaterialIcon.DELETE, "Delete") {
                it.removeContainer(container)
                application?.controller = it
            }
        }
    }.open(client)

    private fun select(view: View<*>, addToSelection: Boolean) {
        if (!view.selectedView) {
            if (!addToSelection) {
                views.values.forEach { it.selectedView = false }
            }
            view.selectedView = true
        }
    }

    fun selectedViews(): List<View<*>> = views.values.filter { it.selectedView }

    init {
        navigationView.onSelect { select ->
            if (select == null) {
                views.values.forEach { it.selectedView = false }
            } else {
                views.values.forEach {
                    it.selectedView = it.dimension in select
                }
            }
        }

        container.classes.forEach { addClass(it) }

        container.containers.forEach { addContainer(it) }

        navigationView.onZoom {
            Root.innerZoom = it
        }

        //console.log(JsPlumb.bind("beforeDrop"))

        Root.innerZoom = 1.0

        // As root view
        async {
            container.relations.forEach { addRelation(it) }
        }

        navigationView.onContext {
            it.stopPropagation()
            openContextMenu(false, it.point())
        }

        // As content view
        listView.classes += "container-view"
        val header = InputView().also {
            titleList += it
        }
        header.bind(nameProperty)

        listView.onContext {
            it.stopPropagation()
            openContextMenu(true, it.point())
        }

        sidebar.setup(navigationView, listView, header) {
            title("Container")
            input("Name").bind(nameProperty)
            button("Auto layout") {
                autoLayout()
            }
            button("Reset zoom") {
                navigationView.zoomTo(1.0)
            }
            button("Reset pan") {
                navigationView.panTo(Point.ZERO)
            }
        }
    }
}