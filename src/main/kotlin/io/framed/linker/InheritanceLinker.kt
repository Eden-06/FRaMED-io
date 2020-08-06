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
import io.framed.framework.view.sidebar
import io.framed.model.Inheritance

/**
 * @author lars
 */

class InheritanceLinker(
        override val model: Inheritance,
        override val manager: ConnectionManager
) : ConnectionLinker<Inheritance> {

    override val nameProperty = property(model::name).trackHistory()
    override val name by nameProperty

    override val sourceIdProperty = property(model::sourceId).trackHistory()
    override val targetIdProperty = property(model::targetId).trackHistory()

    override val pictogram = connection(sourceIdProperty, targetIdProperty) {
        line(ConnectionLine.Type.RECTANGLE) {
            stroke = Color(0, 0, 0)
            strokeWidth = 1
        }

        sourceStyle = null
        targetStyle = connectionEnd {
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

    override val sidebar = sidebar {
        title("Connection")

        group("General") {
            input("Name", nameProperty)
        }
    }

    override val contextMenu = defaultContextMenu()


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
        LinkerManager.setup(this, InheritanceLinker)
    }

    companion object : LinkerInfoConnection {
        override val info = ElementInfo("Inheritance", FramedIcon.INHERITANCE)

        override fun canStart(source: Linker<*, *>): Boolean {
            return source is ClassLinker
        }

        override fun canCreate(source: Linker<*, *>, target: Linker<*, *>): Boolean {
            return canStart(source) && target is ClassLinker && source != target
        }

        override fun isLinkerFor(element: ModelConnection): Boolean = element is Inheritance
        override fun isLinkerFor(linker: Linker<*, *>): Boolean = linker is InheritanceLinker

        override fun createModel(source: Long, target: Long): ModelConnection = Inheritance(source, target)
        override fun createLinker(model: ModelConnection, connectionManager: ConnectionManager): ConnectionLinker<*> =
                if (model is Inheritance) InheritanceLinker(model, connectionManager)
                else throw IllegalArgumentException("Cannot create ${info.name} linker for model element ${model::class}")
    }
}