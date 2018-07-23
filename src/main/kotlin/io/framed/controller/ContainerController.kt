package io.framed.controller

import io.framed.async
import io.framed.jsPlumb
import io.framed.model.Class
import io.framed.model.Container
import io.framed.model.Model
import io.framed.model.Relation
import io.framed.view.*

/**
 * @author lars
 */
class ContainerController(
        val container: Container,
        val parent: ContainerController? = null
) : Controller {

    var application: Application? = null
        set(value) {
            field = value
            childContainer.forEach {
                it.application = value
            }

            async {
                jsPlumbInstance.repaintEverything()
            }
        }

    data class Position(
            val x: Int,
            val y: Int
    ) {
        operator fun plus(other: Position): Position = Position(x + other.x, y + other.y)
    }

    fun autoLayout() {
        val padding = 20
        views.values.fold(Position(padding, padding)) { acc, controller ->
            controller.left = acc.x.toDouble()
            controller.top = acc.y.toDouble()
            acc + Position(controller.clientWidth + padding, 0)
        }

        async {
            jsPlumbInstance.repaintEverything()
        }
    }

    override val view: View<*>
        get() = navigationView

    private val navigationView = NavigationView()
    var touchpadControl: Boolean
        get() = navigationView.touchpadControl
        set(value) {
            navigationView.touchpadControl = value
        }

    val listView = ListView()
    private val titleList = ListView().also {
        listView += it
    }
    private val contentList = ListView().also {
        listView += it
    }

    var views: Map<Model, View<*>> = emptyMap()
    var childContainer: List<ContainerController> = emptyList()

    val jsPlumbInstance = jsPlumb.getInstance().apply {
        setContainer(navigationView.container)

        navigationView.zoomListener.on {
            setZoom(it)
        }
        setZoom(1.0)
    }

    private var classMap: Map<Class, Pair<ClassController, InputView>> = emptyMap()
    fun addClass(clazz: Class, x: Double = 0.0, y: Double = 0.0): ClassController {
        // As root view
        val c = ClassController(clazz, this)
        navigationView.container.appendChild(c.view.html)
        views += clazz to c.view

        c.view.left = x
        c.view.top = y

        async {
            jsPlumbInstance.draggable(c.view.html)
        }

        // As content view
        val input = InputView().also {
            contentList += it
        }
        input.value = clazz.name

        input.change.on {
            clazz.name = it.trim()
        }

        classMap += clazz to (c to input)
        return c
    }

    fun removeClass(clazz: Class) {
        classMap[clazz]?.let { (c, input) ->
            navigationView.container.removeChild(c.view.html)
            contentList -= input
            container.classes -= clazz
        }

        container.relations.filter { it.source == clazz || it.target == clazz }.forEach {
            removeRelation(it)
        }
    }

    private var containerMap: Map<Container, Pair<ContainerController, InputView>> = emptyMap()
    fun addContainer(cont: Container, x: Double = 0.0, y: Double = 0.0): ContainerController {
        // As root view
        val c = ContainerController(cont, this)
        c.application = application
        navigationView.container.appendChild(c.listView.html)
        views += cont to c.listView
        childContainer += c

        c.view.left = x
        c.view.top = y

        async {
            jsPlumbInstance.draggable(c.listView.html)
        }

        // As content view
        val input = InputView().also {
            contentList += it
        }
        input.value = cont.name

        input.change.on {
            cont.name = it.trim()
        }

        containerMap += cont to (c to input)
        return c
    }

    fun removeContainer(cont: Container) {
        containerMap[cont]?.let { (c, input) ->
            navigationView.container.removeChild(c.listView.html)
            contentList -= input
            container.containers -= cont
        }
    }

    private var relationMap: Map<Relation, RelationController> = emptyMap()
    fun addRelation(relation: Relation): RelationController {
        val c = RelationController(relation, this)
        relationMap += relation to c
        return c
    }

    fun removeRelation(relation: Relation) {
        relationMap[relation]?.let {
            container.relations -= relation
            it.remove()
        }
    }

    private fun openContextMenu(open: Boolean, clientX: Double, clientY: Double) = contextMenu {
        title = "Package: " + container.name
        if (open) {
            addItem(MaterialIcon.ARROW_FORWARD, "Step in") {
                application?.controller = this@ContainerController
            }
        }
        addItem(MaterialIcon.ADD, "Add class") {
            val c = Class()
            c.name = "Unnamed class"

            container.classes += c

            val (x, y) = navigationView.mouseToCanvas(clientX, clientY)
            addClass(c, x, y)
        }
        addItem(MaterialIcon.ADD, "Add package") {
            val c = Container()
            c.name = "Unnamed package"

            container.containers += c

            val (x, y) = navigationView.mouseToCanvas(clientX, clientY)
            addContainer(c, x, y)
        }
        parent?.let {
            addItem(MaterialIcon.DELETE, "Delete") {
                it.removeContainer(container)
                application?.controller = it
            }
        }
    }.open(clientX, clientY)

    init {
        container.classes.forEach { addClass(it) }

        container.containers.forEach { addContainer(it) }

        // As root view
        async {
            container.relations.forEach { addRelation(it) }
        }

        view.context.on {
            it.stopPropagation()
            openContextMenu(false, it.clientX.toDouble(), it.clientY.toDouble())
        }


        // As content view
        listView.classes += "container-view"
        val header = InputView().also {
            titleList += it
        }
        header.value = container.name

        header.change.on {
            container.name = it.trim()
        }

        listView.context.on {
            it.stopPropagation()
            openContextMenu(true, it.clientX.toDouble(), it.clientY.toDouble())
        }
    }
}