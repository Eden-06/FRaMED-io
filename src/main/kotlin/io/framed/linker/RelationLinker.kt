package io.framed.linker

import io.framed.framework.ConnectionLinker
import io.framed.framework.Linker
import io.framed.framework.LinkerInfoRelation
import io.framed.framework.LinkerManager
import io.framed.framework.pictogram.*
import io.framed.framework.util.RegexValidator
import io.framed.framework.util.Validator
import io.framed.framework.util.property
import io.framed.framework.view.MaterialIcon
import io.framed.framework.view.contextMenu
import io.framed.framework.view.sidebar
import io.framed.model.Relation
import io.framed.model.RelationMultiplicity
import io.framed.model.RelationType

/**
 * @author lars
 */

class RelationLinker(
        override val model: Relation,
        override val parent: ContainerLinker
) : ConnectionLinker<Relation, Connection> {

    private val nameProperty = property(model::name, RegexValidator("[a-zA-Z]([a-zA-Z0-9 ])*".toRegex()))
    private val sourceCardinalityProperty = property(model::sourceCardinality)
    private val targetCardinalityProperty = property(model::targetCardinality)

    override val sourceIdProperty = property(model::sourceId)
    override val sourceShapeProperty = property(sourceIdProperty, getter = {
        parent.getShapeById(model.sourceId)!!
    }, setter = { Validator.Result.ERROR })
    var source by sourceIdProperty

    override val targetIdProperty = property(model::targetId)
    override val targetShapeProperty = property(targetIdProperty, getter = {
        parent.getShapeById(model.targetId)!!
    }, setter = { Validator.Result.ERROR })
    var target by targetIdProperty

    private val typeProperty = property(model::type)
    private var relationType by typeProperty

    override val pictogram = connection(sourceShapeProperty, targetShapeProperty) {
        labels += textShape(nameProperty) to 0.5
        labels += textShape(sourceCardinalityProperty, RelationMultiplicity.STRING_VALUES) to -30.0
        labels += textShape(targetCardinalityProperty, RelationMultiplicity.STRING_VALUES) to 31.0

        line(ConnectionLine.Type.RECTANGLE) {
            stroke = Color(0, 0, 0)
            strokeWidth = 1
        }
    }

    /**
     * The model styles the model based on their model
     */
    private fun updateEndStyle() {
        when (relationType) {
            RelationType.INHERITANCE -> {
                pictogram.sourceStyle = null
                pictogram.targetStyle = connectionEnd {
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
                pictogram.sourceStyle = null
                pictogram.targetStyle = null
            }
            RelationType.AGGREGATION -> {
                pictogram.sourceStyle = null
                pictogram.targetStyle = connectionEnd {
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

    override val sidebar = sidebar {
        title("Connection")

        group("General") {
            input("Name", nameProperty)
            input("Source cardinality", sourceCardinalityProperty, RelationMultiplicity.STRING_VALUES)
            input("Target cardinality", targetCardinalityProperty, RelationMultiplicity.STRING_VALUES)

            select("Type", RelationType.values().toList(), typeProperty)

            button("Toggle direction") {
                val h = source
                source = target
                target = h
            }
        }
    }

    override val contextMenu = contextMenu {
        title = "Connection"
        addItem(MaterialIcon.DELETE, "Delete") {
            parent.removeRelation(this@RelationLinker)
        }
    }

    init {
        updateEndStyle()

        typeProperty.onChange {
            updateEndStyle()
        }

        LinkerManager.setup(this)
    }

    companion object : LinkerInfoRelation {
        override fun canStart(source: Linker<*, *>): Boolean {
            return source is ClassLinker || source is RoleTypeLinker || source is EventLinker
        }

        override fun canCreate(source: Linker<*, *>, target: Linker<*, *>): Boolean {
            return target is ClassLinker || target is RoleTypeLinker || target is EventLinker
        }
    }
}