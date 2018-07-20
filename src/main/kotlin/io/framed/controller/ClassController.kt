package io.framed.controller

import io.framed.model.Class
import io.framed.view.*

/**
 * @author lars
 */
class ClassController(
        val clazz: Class
) : Controller {

    override val view: View<*>
        get() = classView

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

    init {
        classView.classes += "class-view"
        val header = InputView().also {
            titleList += it
        }
        header.value = clazz.name

        header.change.on {
            clazz.name = it.trim()
        }

        clazz.attributes.forEach {
            attributeList += AttributeController(it).view
        }

        clazz.methods.forEach {
            methodList += MethodController(it).view
        }

        view.context.on {
            it.stopPropagation()
            contextMenu {
                title = "Class: " + clazz.name
                addItem(MaterialIcon.ADD, "Add attribute") {

                }
                addItem(MaterialIcon.ADD, "Add method") {

                }
                addItem(MaterialIcon.DELETE, "Delete") {

                }
            }.open(it.clientX.toDouble(), it.clientY.toDouble())
        }
    }
}