package io.framed.controller

import io.framed.model.Class
import io.framed.model.Container
import io.framed.model.Relation
import io.framed.picto.*
import io.framed.util.Dimension
import io.framed.util.Point
import io.framed.util.property
import io.framed.view.*

/**
 * @author lars
 */
class ContainerController(
        val container: Container,
        val application: Application,
        override val parent: ContainerController? = null
) : Controller<BoxShape>(container, parent) {

    val nameProperty = property(container::name)
    var name by nameProperty

    override fun internalCreateSidebar(): Sidebar = Sidebar(application)

    private val titleBox = boxShape {
        textShape(nameProperty)
    }
    private val contentBox = boxShape { }

    override val picto = boxShape {
        +titleBox
        +contentBox

        style {
            background = linearGradient("to bottom") {
                add(color("#fffbd9"), 0.0)
                add(color("#fff7c4"), 1.0)
            }
            border = border {
                style = Border.BorderStyle.SOLID
                width = 1.0
                color = color(0, 0, 0, 0.3)
            }
        }

        hasSidebar = true
        hasContext = true
    }.also(this::initPicto)

    operator fun get(clazz: Class): Shape = classMap[clazz]?.first?.picto ?: throw IllegalArgumentException()

    val viewModel = ViewModel(boxShape {
        hasSidebar = true
        hasContext = true
    }.also(this::initPicto)).apply { layer = Layer() }

    private var classMap: Map<Class, Pair<ClassController, TextShape>> = emptyMap()

    fun addClass(clazz: Class, position: Point = Point.ZERO): ClassController {
        // As normal view
        val controller = ClassController(clazz, this)
        viewModel.container += controller.picto
        viewModel.layer[controller.picto] = Dimension(position.x, position.y)

        // As list entry
        val input = contentBox.textShape(controller.nameProperty)

        classMap += clazz to (controller to input)
        return controller
    }

    fun removeClass(clazz: Class) {
        classMap[clazz]?.let { (controller, input) ->
            // As normal view
            viewModel.container -= controller.picto
            viewModel.layer[controller.picto] = null

            // As list entry
            contentBox -= input

            classMap -= clazz
            container.classes -= clazz
        }

        showSidebar()
    }

    private var containerMap: Map<Container, Pair<ContainerController, TextShape>> = emptyMap()
    private fun addContainer(cont: Container, position: Point = Point.ZERO): ContainerController {
        // As normal view
        val controller = ContainerController(cont, application, this)
        viewModel.container += controller.picto
        viewModel.layer[controller.picto] = Dimension(position.x, position.y)

        // As list entry
        val input = contentBox.textShape(controller.nameProperty)

        containerMap += cont to (controller to input)
        return controller
    }

    private fun removeContainer(cont: Container) {
        containerMap[cont]?.let { (controller, input) ->
            // As normal view
            viewModel.container -= controller.picto
            viewModel.layer[controller.picto] = null

            // As list entry
            contentBox -= input

            containerMap -= cont
            container.containers -= cont
        }

        showSidebar()
    }

    private var relationMap: Map<Relation, RelationController> = emptyMap()
    private fun addRelation(relation: Relation): RelationController {
        val controller = RelationController(relation, this)
        viewModel += controller.picto

        relationMap += relation to controller
        return controller
    }

    fun removeRelation(relation: Relation) {
        relationMap[relation]?.let { controller ->
            viewModel -= controller.picto

            relationMap -= relation
            container.relations -= relation
        }

        showSidebar()
    }

    private var sidebarButtonAutoLayout = Button()
    private var sidebarButtonResetZoom = Button()
    private var sidebarButtonResetPan = Button()

    override fun createSidebar(sidebar: Sidebar) = sidebar.setup {
        title("Container")
        group("General") {
            input("Name", nameProperty)
        }
        group("Actions") {
            sidebarButtonAutoLayout = button("Auto layout") {
                //autoLayout()
            }
            sidebarButtonResetZoom = button("Reset zoom") {
                application.renderer.zoomTo(1.0)
            }
            sidebarButtonResetPan = button("Reset pan") {
                application.renderer.panTo(Point.ZERO)
            }
        }
    }

    override fun prepareSidebar(sidebar: Sidebar, event: SidebarEvent) {
        val h = event.target != picto
        sidebarButtonAutoLayout.visible = h
        sidebarButtonResetZoom.visible = h
        sidebarButtonResetPan.visible = h
    }

    override fun createContextMenu(event: ContextEvent): ContextMenu? = contextMenu {
        title = "Package: $name"

        if (event.target == picto) {

            addItem(MaterialIcon.ARROW_FORWARD, "Step in") {
                application.controller = this@ContainerController
            }
        } else {
            parent?.let {
                addItem(MaterialIcon.ARROW_BACK, "Step out") {
                    application.controller = it
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
                application.controller = it
            }
        }
    }

    init {
        container.classes.forEach { addClass(it) }

        container.containers.forEach { addContainer(it) }

        container.relations.forEach { addRelation(it) }

        viewModel.onRelationDraw { (sourceShape, targetShape) ->
            val source = classMap.entries.find { (_, pair) ->
                pair.first.picto == sourceShape
            }?.key
            val target = classMap.entries.find { (_, pair) ->
                pair.first.picto == targetShape
            }?.key

            if (source != null && target != null) {
                val relation = Relation(source, target)
                container.relations += relation

                addRelation(relation)
            }
        }
    }
}