package io.framed.controller

import io.framed.model.CompartmentType
import io.framed.view.ListView
import io.framed.view.TextView
import io.framed.view.View

/**
 * @author lars
 */
class CompartmentTypeController(
        val compartmentType: CompartmentType
) : Controller {

    override val view: View<*>
        get() = compartmentView

    private val compartmentView = ListView()

    private val titleList = ListView().also {
        compartmentView += it
    }
    private val attributeList = ListView().also {
        compartmentView += it
    }
    private val methodList = ListView().also {
        compartmentView += it
    }

    init {
        compartmentView.classes += "compartment-type-view"
        val header = TextView().also {
            titleList += it
        }
        header.text = compartmentType.name
        header.contentEditable = true
        header.singleLine = true

        header.change.on {
            compartmentType.name = it.trim()
        }

        compartmentType.attributes.forEach {
            attributeList += AttributeController(it).view
        }

        compartmentType.methods.forEach {
            methodList += MethodController(it).view
        }
    }
}