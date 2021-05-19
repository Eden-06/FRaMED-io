package io.framed.linker

import de.westermann.kobserve.property.property
import io.framed.framework.*
import io.framed.framework.linker.ConnectionLinker
import io.framed.framework.linker.Linker
import io.framed.framework.linker.LinkerInfoConnection
import io.framed.framework.linker.LinkerManager
import io.framed.framework.model.ModelConnection
import io.framed.framework.pictogram.*
import io.framed.framework.util.trackHistory
import io.framed.framework.view.FramedIcon
import io.framed.framework.view.MaterialIcon
import io.framed.framework.view.contextMenu
import io.framed.framework.view.sidebar
import io.framed.model.Equivalence

/**
 * EquivalenceLinker for the Equivalence role connection.
 *
 * @author David Oberacker
 */

class EquivalenceLinker(
        override val model: Equivalence,
        override val manager: ConnectionManager
) : ConnectionLinker<Equivalence> {

    override val nameProperty = property(model::name).trackHistory()
    override val name by nameProperty

    override val sourceIdProperty = property(model::sourceId).trackHistory()
    override val targetIdProperty = property(model::targetId).trackHistory()

    override val pictogram = connection(sourceIdProperty, targetIdProperty) {
        line(ConnectionLine.Type.RECTANGLE) {
            stroke = Color(0, 0, 0)
            strokeWidth = 1

            dashArray = arrayOf(4, 4)
        }

        sourceStyle = connectionEnd {
            width = 15
            length = 15
            foldback = 1.0
            direction = -1
            location = 0.0
            paintStyle {
                stroke = Color(0, 0, 0)
                strokeWidth = 1
                fill = Color(255, 255, 255)
            }
        }
        targetStyle = connectionEnd {
            width = 15
            length = 15
            foldback = 1.0
            paintStyle {
                stroke = Color(0, 0, 0)
                strokeWidth = 1
                fill = Color(255, 255, 255)
            }
        }
    }

    override val sidebar = sidebar {
        title("Role Equivalence")

        group("General") {
            input("Name", nameProperty)
        }
    }

    override val contextMenu = contextMenu {
        title = "Connection"
        addItem(MaterialIcon.DELETE, "Delete") {
            delete()
        }
    }


    override fun updateLabelBindings() {
        val ids = pictogram.labels.mapNotNull { it.id }.distinct().toSet()
        if ("name" !in ids) {
            pictogram.labels += Label(id = "name", position = 0.5)
        }

        for (label in pictogram.labels) {
            if (label.textProperty.isBound) {
                label.textProperty.unbind()
            }
            when {
                label.id == "name" -> label.textProperty.bindBidirectional(nameProperty)
            }
        }

        super.updateLabelBindings()
    }

    init {
        LinkerManager.setup(this, EquivalenceLinker)
    }

    companion object : LinkerInfoConnection {
        override val info = ElementInfo("Equivalence", FramedIcon.EQUIVALENCE)

        override fun canStart(source: Linker<*, *>): Boolean {
            return source is RoleTypeLinker
        }

        override fun canCreate(source: Linker<*, *>, target: Linker<*, *>): Boolean {
            return canStart(source) && target is RoleTypeLinker && source != target
        }

        override fun isLinkerFor(element: ModelConnection): Boolean = element is Equivalence
        override fun isLinkerFor(linker: Linker<*, *>): Boolean = linker is EquivalenceLinker

        override fun createModel(source: Long, target: Long): ModelConnection = Equivalence(source, target)
        override fun createLinker(model: ModelConnection, connectionManager: ConnectionManager): ConnectionLinker<*> =
            if (model is Equivalence) EquivalenceLinker(model, connectionManager)
            else throw IllegalArgumentException("Cannot create ${info.name} linker for model element ${model::class}")
    }
}
