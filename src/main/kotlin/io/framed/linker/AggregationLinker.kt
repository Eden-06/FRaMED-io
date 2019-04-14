package io.framed.linker

import de.westermann.kobserve.property.property
import io.framed.framework.*
import io.framed.framework.pictogram.*
import io.framed.framework.util.trackHistory
import io.framed.framework.view.MaterialIcon
import io.framed.framework.view.sidebar
import io.framed.model.Aggregation

/**
 * @author lars
 */

class AggregationLinker(
        override val model: Aggregation,
        override val manager: ConnectionManager
) : ConnectionLinker<Aggregation> {

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
        }

        targetStyle = null
        sourceStyle = connectionEnd {
            width = 10
            length = 10
            foldback = 2.0
            paintStyle {
                stroke = Color(0, 0, 0)
                strokeWidth = 1
                fill = Color(255, 255, 255)
            }
        }
    }

    override val sidebar = sidebar {
        title("Aggregation")

        group("General") {
            input("Name", nameProperty)
            input("Source cardinality", sourceCardinalityProperty)
            input("Target cardinality", targetCardinalityProperty)
        }
    }

    override val contextMenu = defaultContextMenu()

    override fun updateLabelBindings() {
        val ids = pictogram.labels.mapNotNull { it.id }.distinct().toSet()
        if ("name" !in ids) {
            pictogram.labels += Label(id = "name", position = 0.5)
        }
        if ("source" !in ids) {
            pictogram.labels += Label(id = "source", position = 31.0)
        }
        if ("target" !in ids) {
            pictogram.labels += Label(id = "target", position = -30.0)
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
        LinkerManager.setup(this, AggregationLinker)
    }

    companion object : LinkerInfoConnection {
        override val info = ConnectionInfo("Aggregation", MaterialIcon.ADD)

        override fun canStart(source: Linker<*, *>): Boolean {
            return source is RoleTypeLinker || source is ClassLinker
        }

        override fun canCreate(source: Linker<*, *>, target: Linker<*, *>): Boolean {
            return source != target && (
                    (target is RoleTypeLinker && source is RoleTypeLinker) ||
                            (target is ClassLinker && source is ClassLinker)
                    )
        }

        override fun isLinkerFor(element: ModelConnection<*>): Boolean = element is Aggregation
        override fun isLinkerFor(linker: Linker<*, *>): Boolean = linker is AggregationLinker

        override fun createModel(source: Long, target: Long): ModelConnection<*> = Aggregation(source, target)
        override fun createLinker(model: ModelConnection<*>, connectionManager: ConnectionManager): ConnectionLinker<*> =
                if (model is Aggregation) AggregationLinker(model, connectionManager)
                else throw IllegalArgumentException("Cannot create ${info.name} linker for model element ${model::class}")
    }
}