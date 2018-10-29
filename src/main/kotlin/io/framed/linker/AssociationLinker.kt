package io.framed.linker

import io.framed.framework.*
import io.framed.framework.pictogram.*
import io.framed.framework.util.*
import io.framed.framework.view.MaterialIcon
import io.framed.framework.view.contextMenu
import io.framed.framework.view.sidebar
import io.framed.model.Association

/**
 * @author lars
 */

class AssociationLinker(
        override val model: Association,
        override val manager: ConnectionManager
) : ConnectionLinker<Association> {

    private val nameProperty = property(model::name, TrueValidator()).trackHistory()
    private val sourceCardinalityProperty = property(model::sourceCardinality).trackHistory()
    private val targetCardinalityProperty = property(model::targetCardinality).trackHistory()

    override val sourceIdProperty = property(model::sourceId).trackHistory()
    override val targetIdProperty = property(model::targetId).trackHistory()

    override val pictogram = connection(sourceIdProperty, targetIdProperty) {
        labels += textShape(nameProperty) to 0.5
        labels += textShape(sourceCardinalityProperty, CardinalityPreset.STRING_VALUES) to -30.0
        labels += textShape(targetCardinalityProperty, CardinalityPreset.STRING_VALUES) to 31.0

        line(ConnectionLine.Type.RECTANGLE) {
            stroke = Color(0, 0, 0)
            strokeWidth = 1
        }

        sourceStyle = null
        targetStyle = null
    }

    override val sidebar = sidebar {
        title("Connection")

        group("General") {
            input("Name", nameProperty)
            input("Source cardinality", sourceCardinalityProperty)
            input("Target cardinality", targetCardinalityProperty)
        }
    }

    override val contextMenu = contextMenu {
        title = "Connection"
        addItem(MaterialIcon.DELETE, "Delete") {
            delete()
        }
    }

    init {
        LinkerManager.setup(this, AssociationLinker)
    }

    companion object : LinkerInfoConnection {
        override val info = ConnectionInfo("Association", MaterialIcon.ADD)

        override fun canStart(source: Linker<*, *>): Boolean {
            return source is ClassLinker || source is RoleTypeLinker || source is EventLinker || source is CompartmentLinker
        }

        override fun canCreate(source: Linker<*, *>, target: Linker<*, *>): Boolean {
            return canStart(source) && (target is ClassLinker || target is RoleTypeLinker || target is EventLinker || target is CompartmentLinker)
        }
    }
}