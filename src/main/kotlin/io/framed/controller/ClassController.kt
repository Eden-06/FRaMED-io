package io.framed.controller

import io.framed.model.Attribute
import io.framed.model.Class
import io.framed.model.Method
import io.framed.util.Property
import io.framed.util.point
import io.framed.view.*

/**
 * @author lars
 */
class ClassController(
        val clazz: Class,
        val parent: ContainerController
) : Controller {

    override val view: View<*>
        get() = classView

    override val sidebar: Sidebar = parent.createSidebar()

    fun createSidebar() = parent.createSidebar()

    val nameProperty = Property(clazz::name)
    var name by nameProperty

    private val classView = ListView()

    private val titleList = ListView().also {
        classView += it
    }
    private val attributeList = ListView().also {
        classView += it
    }
    private val methodList = ListView().also {
        classView += it
    }

    private var attributeMap: Map<Attribute, AttributeController> = emptyMap()
    private fun addAttribute(attribute: Attribute) = AttributeController(attribute, this).also {
        attributeList += it.view
        attributeMap += attribute to it
    }

    fun removeAttribute(attribute: Attribute) {
        attributeMap[attribute]?.let {
            attributeList -= it.view
            attributeMap -= attribute
            clazz.attributes -= attribute
        }
        sidebar.display()
    }

    private var methodMap: Map<Method, MethodController> = emptyMap()
    private fun addMethod(method: Method) = MethodController(method, this).also {
        methodList += it.view
        methodMap += method to it
    }

    fun removeMethod(method: Method) {
        methodMap[method]?.let {
            methodList -= it.view
            methodMap -= method
            clazz.methods -= method
        }
        sidebar.display()
    }

    init {
        classView.classes += "class-view"
        val header = InputView().also {
            titleList += it
        }
        header.bind(nameProperty)

        clazz.attributes.forEach { addAttribute(it) }

        clazz.methods.forEach { addMethod(it) }

        view.onContext {
            it.stopPropagation()
            contextMenu {
                title = "Class: $name"
                addItem(MaterialIcon.ADD, "Add attribute") {
                    val a = Attribute()
                    a.name = "unnamed"
                    clazz.attributes += a

                    addAttribute(a)
                }
                addItem(MaterialIcon.ADD, "Add method") {
                    val m = Method()
                    m.name = "unnamed"
                    clazz.methods += m

                    addMethod(m)
                }
                addItem(MaterialIcon.DELETE, "Delete") {
                    parent.removeClass(clazz)
                }
            }.open(it.point())
        }

        sidebar.setup(view, header) {
            title("Class")
            input("Name").bind(nameProperty)
        }
    }
}