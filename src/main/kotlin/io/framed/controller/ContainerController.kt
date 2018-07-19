package io.framed.controller

import io.framed.async
import io.framed.jsPlumb
import io.framed.model.Container
import io.framed.model.Model
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

    init {
        // As root view
        container.classes.forEach {
            val c = ClassController(it)
            navigationView.container.appendChild(c.view.html)
            views += it to c.view
        }

        container.containers.forEach {
            val c = ContainerController(it, this)
            c.application = application
            navigationView.container.appendChild(c.listView.html)
            views += it to c.listView
            childContainer += c
        }

        async {
            views.values.forEach {
                jsPlumbInstance.draggable(it.html)
            }

            container.relations.forEach {
                RelationController(it, this)
            }
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

        container.classes.forEach { clazz ->
            val input = InputView().also {
                contentList += it
            }
            input.value = clazz.name

            input.change.on {
                clazz.name = it.trim()
            }
        }

        container.containers.forEach { cont ->
            val input = InputView().also {
                contentList += it
            }
            input.value = cont.name

            input.change.on {
                cont.name = it.trim()
            }
        }

        listView.context.on {
            application?.controller = this
        }
    }
}