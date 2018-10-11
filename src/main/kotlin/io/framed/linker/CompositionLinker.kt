package io.framed.linker

import io.framed.framework.ConnectionLinker
import io.framed.framework.Linker
import io.framed.framework.LinkerInfoConnection
import io.framed.framework.LinkerManager
import io.framed.framework.pictogram.*
import io.framed.framework.util.RegexValidator
import io.framed.framework.util.Validator
import io.framed.framework.util.property
import io.framed.framework.util.trackHistory
import io.framed.framework.view.MaterialIcon
import io.framed.framework.view.contextMenu
import io.framed.framework.view.sidebar
import io.framed.model.Association
import io.framed.model.Composition

/**
 * @author lars
 */

class CompositionLinker(
        override val model: Composition,
        override val parent: ContainerLinker
) : ConnectionLinker<Composition> {

    private val nameProperty = property(model::name, RegexValidator("[a-zA-Z]([a-zA-Z0-9 ])*".toRegex())).trackHistory()
    private val sourceCardinalityProperty = property(model::sourceCardinality).trackHistory()
    private val targetCardinalityProperty = property(model::targetCardinality).trackHistory()

    override val sourceIdProperty = property(model::sourceId).trackHistory()
    override val sourceShapeProperty = property(sourceIdProperty, getter = {
        parent.getShapeById(model.sourceId)!!
    }, setter = { Validator.Result.ERROR })
    var source by sourceIdProperty

    override val targetIdProperty = property(model::targetId).trackHistory()
    override val targetShapeProperty = property(targetIdProperty, getter = {
        parent.getShapeById(model.targetId)!!
    }, setter = { Validator.Result.ERROR })
    var target by targetIdProperty

    override val pictogram = connection(sourceShapeProperty, targetShapeProperty) {
        labels += textShape(nameProperty) to 0.5
        labels += textShape(sourceCardinalityProperty) to -30.0
        labels += textShape(targetCardinalityProperty) to 31.0

        line(ConnectionLine.Type.RECTANGLE) {
            stroke = Color(0, 0, 0)
            strokeWidth = 1
        }

        sourceStyle = null
        targetStyle = connectionEnd {
            width = 20
            length = 20
            foldback = 2.0
            paintStyle {
                stroke = Color(0, 0, 0)
                strokeWidth = 1
                fill = Color(0, 0, 0)
            }
        }
    }

    override val sidebar = sidebar {
        title("Connection")

        group("General") {
            input("Name", nameProperty)
            input("Source cardinality", sourceCardinalityProperty)
            input("Target cardinality", targetCardinalityProperty)

            /*
            select("Type", RelationType.values().toList(), typeProperty) {
                it.printableName
            }

            button("Toggle direction") {
                val h = source
                source = target
                target = h
            }
            */
        }
    }

    override val contextMenu = contextMenu {
        title = "Connection"
        addItem(MaterialIcon.DELETE, "Delete") {
            delete()
        }
    }

    override fun delete() {
        parent.compositions -= this
    }

    init {
        LinkerManager.setup(this, CompositionLinker)
    }

    companion object : LinkerInfoConnection {
        override val info = ConnectionInfo("Composition", MaterialIcon.ADD)

        override fun canStart(source: Linker<*, *>): Boolean {
            return source is ClassLinker || source is RoleTypeLinker || source is EventLinker
        }

        override fun canCreate(source: Linker<*, *>, target: Linker<*, *>): Boolean {
            return target is ClassLinker || target is RoleTypeLinker || target is EventLinker
        }
    }
}