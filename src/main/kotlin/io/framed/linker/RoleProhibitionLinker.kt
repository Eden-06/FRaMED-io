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
import io.framed.model.RoleProhibition

/**
 * ProhibitionLinker for the Prohibition role connection.
 *
 * @author David Oberacker
 */

class RoleProhibitionLinker(
    override val model: RoleProhibition,
    override val manager: ConnectionManager
) : ConnectionLinker<RoleProhibition> {

    override val nameProperty = property(model::name).trackHistory()
    override val name by nameProperty

    override val sourceIdProperty = property(model::sourceId).trackHistory()
    override val targetIdProperty = property(model::targetId).trackHistory()

    //TODO: Optimize the display of the prohibition arrow.
    override val pictogram = connection(sourceIdProperty, targetIdProperty) {
        line(ConnectionLine.Type.RECTANGLE) {
            stroke = Color(0, 0, 0)
            strokeWidth = 1

            dashArray = arrayOf(4, 4)
        }

        sourceStyle = connectionEnd {
            width = 15
            length = 1
            foldback = 0.0
            paintStyle {
                stroke = Color(0, 0, 0)
                strokeWidth = 1
            }
            location = 0.05
            direction = 1
        }
        targetStyle = connectionEnd {
            width = 15
            length = 1
            foldback = 0.0

            paintStyle {
                stroke = Color(0, 0, 0)
                strokeWidth = 1
            }
            location = 0.95
            direction = -1
        }
    }

    override val sidebar = sidebar {
        title("Role Prohibition")

        group("General") {
            input("Name", nameProperty)
        }
    }

    override val contextMenu = contextMenu {
        title = "Role Prohibition"
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
        LinkerManager.setup(this, RoleProhibitionLinker)
    }

    companion object : LinkerInfoConnection {
        override val info = ElementInfo("Role Prohibition", FramedIcon.PROHIBITION)

        override fun canStart(source: Linker<*, *>): Boolean {
            return (source is RoleTypeLinker || source is RoleGroupLinker)
        }

        override fun canCreate(source: Linker<*, *>, target: Linker<*, *>): Boolean {
            return canStart(source) &&
                    (target is RoleTypeLinker || target is RoleGroupLinker)  &&
                    source != target &&
                    source.parent == target.parent
        }

        override fun isLinkerFor(element: ModelConnection): Boolean = element is RoleProhibition
        override fun isLinkerFor(linker: Linker<*, *>): Boolean = linker is RoleProhibitionLinker

        override fun createModel(source: Long, target: Long): ModelConnection = RoleProhibition(source, target)
        override fun createLinker(model: ModelConnection, connectionManager: ConnectionManager): ConnectionLinker<*> =
            if (model is RoleProhibition) RoleProhibitionLinker(model, connectionManager)
            else throw IllegalArgumentException("Cannot create ${info.name} linker for model element ${model::class}")
    }
}
