package io.framed.linker

import io.framed.model.Relation
import io.framed.model.RelationMultiplicity
import io.framed.model.RelationType
import io.framed.picto.*
import io.framed.util.RegexValidator
import io.framed.util.Validator
import io.framed.util.property
import io.framed.view.ContextMenu
import io.framed.view.MaterialIcon
import io.framed.view.Sidebar
import io.framed.view.contextMenu

/**
 * @author lars
 */

class RelationLinker(
        private val relation: Relation,
        override val parent: ContainerLinker
) : Linker<io.framed.picto.Connection>(relation, parent) {

    private val nameProperty = property(relation::name, RegexValidator("[a-zA-Z]([a-zA-Z0-9])*".toRegex()))
    private val sourceCardinalityProperty = property(relation::sourceCardinality)
    private val targetCardinalityProperty = property(relation::targetCardinality)

    private val sourceProperty = property(relation::sourceId)
    private val sourceShapeProperty = property(sourceProperty, getter = {
        parent[relation.sourceId]
    }, setter = { Validator.Result.ERROR })
    var source by sourceProperty

    private val targetProperty = property(relation::targetId)
    private val targetShapeProperty = property(targetProperty, getter = {
        parent[relation.targetId]
    }, setter = { Validator.Result.ERROR })
    var target by targetProperty

    private val typeProperty = property(relation::type)
    private var relationType by typeProperty

    override val picto = connection(sourceShapeProperty, targetShapeProperty) {
        labels += textShape(nameProperty) to 0.5
        labels += textShape(sourceCardinalityProperty, RelationMultiplicity.STRING_VALUES) to -30.0
        labels += textShape(targetCardinalityProperty, RelationMultiplicity.STRING_VALUES) to 31.0

        line(ConnectionLine.Type.RECTANGLE) {
            stroke = Color(0, 0, 0)
            strokeWidth = 1
        }

        hasContext = true
        hasSidebar = true
    }.also(this::initPicto)

    /**
     * The method styles the relation based on their type
     */
    private fun updateEndStyle() {
        when (relationType) {
            RelationType.INHERITANCE -> {
                picto.sourceStyle = null
                picto.targetStyle = connectionEnd {
                    width = 20
                    length = 20
                    foldback = 1.0
                    paintStyle {
                        stroke = Color(0, 0, 0)
                        strokeWidth = 1
                        fill = Color(255, 255, 255)
                    }
                }
            }
            RelationType.ASSOCIATION -> {
                picto.sourceStyle = null
                picto.targetStyle = null
            }
            RelationType.AGGREGATION -> {
                picto.sourceStyle = null
                picto.targetStyle = connectionEnd {
                    width = 20
                    length = 20
                    foldback = 2.0
                    paintStyle {
                        stroke = Color(0, 0, 0)
                        strokeWidth = 1
                        fill = Color(255, 255, 255)
                    }
                }
            }
        }
    }

    override fun createSidebar(sidebar: Sidebar) = sidebar.setup {
        title("Connection")

        group("General") {
            input("Name", nameProperty)
            input("Source cardinality", sourceCardinalityProperty, RelationMultiplicity.STRING_VALUES)
            input("Target cardinality", targetCardinalityProperty, RelationMultiplicity.STRING_VALUES)

            val types = RelationType.values().toList()
            select("Type", types, relationType) {
                relationType = it
            }

            button("Toggle direction") {
                val h = source
                source = target
                target = h
            }
        }
    }

    override fun createContextMenu(event: ContextEvent): ContextMenu? = contextMenu {
        title = "Connection"
        addItem(MaterialIcon.DELETE, "Delete") {
            parent.removeRelation(relation)
        }
    }

    init {
        updateEndStyle()

        typeProperty.onChange {
            updateEndStyle()
        }
    }
}