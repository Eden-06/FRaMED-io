package io.framed.linker

import io.framed.model.Attribute
import io.framed.model.Class
import io.framed.model.Method
import io.framed.picto.*
import io.framed.util.RegexValidator
import io.framed.util.property
import io.framed.view.ContextMenu
import io.framed.view.MaterialIcon
import io.framed.view.Sidebar
import io.framed.view.contextMenu

/**
 * @author lars
 */
class ClassLinker(
        val clazz: Class,
        override val parent: ContainerLinker
) : Linker<BoxShape>(clazz, parent) {

    val nameProperty = property(clazz::name, RegexValidator("[a-zA-Z]([a-zA-Z0-9])*".toRegex()))
    var name by nameProperty

    private lateinit var attributeBox:BoxShape
    private lateinit var methodBox: BoxShape

    override val picto = boxShape {
        boxShape {
            textShape(nameProperty)
        }
        attributeBox = boxShape {  }
        methodBox = boxShape {  }

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

        acceptRelation = true
    }.also(this::initPicto)

    /**
     * The map stores all attriubtes of the related class
     */
    private var attributeMap: Map<Attribute, AttributeLinker> = emptyMap()

    /**
     * The method adds a new attribute to the class
     * @param attribute new attribute to add
     */
    private fun addAttribute(attribute: Attribute) = AttributeLinker(attribute, this).also {
        attributeBox += it.picto
        attributeMap += attribute to it
    }

    /**
     * The method removes an attribute of the class
     */
    fun removeAttribute(attribute: Attribute) {
        attributeMap[attribute]?.let {
            attributeBox -= it.picto
            attributeMap -= attribute
            clazz.attributes -= attribute
        }
        showSidebar()
    }

    private var methodMap: Map<Method, MethodLinker> = emptyMap()

    private fun addMethod(method: Method) = MethodLinker(method, this).also {
        methodBox += it.picto
        methodMap += method to it
    }

    fun removeMethod(method: Method) {
        methodMap[method]?.let {
            methodBox -= it.picto
            methodMap -= method
            clazz.methods -= method
        }
        showSidebar()
    }

    override fun createSidebar(sidebar: Sidebar) = sidebar.setup {
        title("Class")
        group("General") {
            input("Name", nameProperty)
        }
    }

    override fun createContextMenu(event: ContextEvent): ContextMenu? = contextMenu {
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
    }

    init {
        clazz.attributes.forEach { addAttribute(it) }
        clazz.methods.forEach { addMethod(it) }
    }
}