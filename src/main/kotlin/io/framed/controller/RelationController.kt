package io.framed.controller

import io.framed.model.Relation
import io.framed.model.RelationMultiplicity
import io.framed.model.RelationType
import io.framed.picto.ContextEvent
import io.framed.picto.relation
import io.framed.picto.textShape
import io.framed.util.Validator
import io.framed.util.property
import io.framed.view.ContextMenu
import io.framed.view.MaterialIcon
import io.framed.view.Sidebar
import io.framed.view.contextMenu

/**
 * @author lars
 */

class RelationController(
        private val relation: Relation,
        override val parent: ContainerController
) : Controller<io.framed.picto.Relation>(relation, parent) {

    private val nameProperty = property(relation::name)
    private val sourceCardinalityProperty = property(relation::sourceCardinality)
    private val targetCardinalityProperty = property(relation::targetCardinality)

    private val sourceProperty = property(relation::source)
    private val sourceShapeProperty = property(sourceProperty, getter = {
        parent[relation.source]
    }, setter = { Validator.Result.ERROR })
    var source by sourceProperty

    private val targetProperty = property(relation::target)
    private val targetShapeProperty = property(targetProperty, getter = {
        parent[relation.target]
    }, setter = { Validator.Result.ERROR })
    var target by targetProperty

    private val typeProperty = property(relation::type)
    var relationType by typeProperty

    override val picto = relation(sourceShapeProperty, targetShapeProperty, typeProperty) {
        labels += textShape(nameProperty) to 0.5
        labels += textShape(sourceCardinalityProperty, RelationMultiplicity.STRING_VALUES) to 0.1
        labels += textShape(targetCardinalityProperty, RelationMultiplicity.STRING_VALUES) to 0.9

        hasContext = true
        hasSidebar = true
    }.also(this::initPicto)

    override fun createSidebar(sidebar: Sidebar) = sidebar.setup {
        title("Relation")

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
        title = "Relation"
        addItem(MaterialIcon.DELETE, "Delete") {
            parent.removeRelation(relation)
        }
    }
}