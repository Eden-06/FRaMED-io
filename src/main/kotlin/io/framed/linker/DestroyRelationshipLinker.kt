package io.framed.linker

import de.westermann.kobserve.basic.property
import io.framed.framework.*
import io.framed.framework.pictogram.*
import io.framed.framework.util.trackHistory
import io.framed.framework.view.MaterialIcon
import io.framed.framework.view.contextMenu
import io.framed.framework.view.sidebar
import io.framed.model.DestroyRelationship

class DestroyRelationshipLinker(
        override val model: DestroyRelationship,
        override val manager: ConnectionManager
) : ConnectionLinker<DestroyRelationship> {

    override val nameProperty = property(model::name).trackHistory()
    override val name by nameProperty

    private val sourceCardinalityProperty = property(model::sourceCardinality).trackHistory()
    private val targetCardinalityProperty = property(model::targetCardinality).trackHistory()

    override val sourceIdProperty = property(model::sourceId).trackHistory()
    override val targetIdProperty = property(model::targetId).trackHistory()

    override val pictogram = connection(sourceIdProperty, targetIdProperty) {
        line(ConnectionLine.Type.RECTANGLE) {
            stroke = Color(0, 0, 0)
            strokeWidth = 1

            dashArray = arrayOf(3, 3)
        }
        sourceStyle = null
        targetStyle = connectionEnd {
            width = 20
            length = 20
            foldback = 1.0
            paintStyle {
                stroke = Color(0, 0, 0)
                strokeWidth = 1
                fill = Color(0, 0, 0)
            }
        }
    }

    override val sidebar = sidebar {
        title("Destroy Relationship")

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

    override fun updateLabelBindings() {
        val ids = pictogram.labels.mapNotNull { it.id }.distinct().toSet()
        if ("name" !in ids) {
            pictogram.labels += Label(id = "name", position = 0.5)
        }
        if ("source" !in ids) {
            pictogram.labels += Label(id = "source", position = -30.0)
        }
        if ("target" !in ids) {
            pictogram.labels += Label(id = "target", position = 31.0)
        }

        for (label in pictogram.labels) {
            if (label.textProperty.isBound) {
                label.textProperty.unbind()
            }
            when {
                label.id == "name" -> label.textProperty.bindBidirectional(nameProperty)
                label.id == "source" -> {
                    label.textProperty.bindBidirectional(sourceCardinalityProperty)
                    label.autocomplete = CardinalityPreset.STRING_VALUES
                }
                label.id == "target" -> {
                    label.textProperty.bindBidirectional(targetCardinalityProperty)
                    label.autocomplete = CardinalityPreset.STRING_VALUES
                }
            }
        }

        super.updateLabelBindings()
    }

    init {
        LinkerManager.setup(this, RelationshipLinker)
    }

    companion object : LinkerInfoConnection {
        override val info = ConnectionInfo("Destroy Relationship", MaterialIcon.ADD)

        override fun canStart(source: Linker<*, *>): Boolean {
            return source is RoleTypeLinker
        }

        override fun canCreate(source: Linker<*, *>, target: Linker<*, *>): Boolean {
            return (target is EventLinker) && source is RoleTypeLinker
        }
    }
}