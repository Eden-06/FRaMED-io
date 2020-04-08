package io.framed.framework

import de.westermann.kobserve.Property
import de.westermann.kobserve.property.mapBinding
import de.westermann.kobserve.property.property
import io.framed.framework.linker.ModelLinker
import io.framed.framework.model.ModelElement
import io.framed.framework.pictogram.*

/**
 * Represents a tab.
 */
class Controller(
        val linker: ModelLinker<*, *, *>,
        private val layer: Layer
) {

    /**
     * Intermediate object to handle actions of the renderer.
     */
    val handler = object : ViewModelHandler {

        override fun isConnectable(shape: Long): Boolean =
                linker.connectionManager.isConnectable(shape)

        override fun canConnectionStart(source: Long): Boolean =
                linker.connectionManager.canConnectionStart(source).isNotEmpty()

        override fun canConnectionCreate(source: Long, target: Long): Boolean =
                linker.connectionManager.canConnectionCreate(source, target).isNotEmpty()

        override fun createConnection(source: Long, target: Long) =
                linker.connectionManager.createConnection(source, target)

        override fun canDropShape(shape: Long, target: Long): Boolean = linker.canDropShape(shape, target)

        override fun dropShape(shape: Long, target: Long) = linker.dropShape(shape, target)

        override fun delete(shapes: List<Long>) = linker.delete(shapes)

        override fun copy(shapes: List<Long>) {
            clipboard = linker.copy(shapes, linker.container)
        }

        override fun cut(shapes: List<Long>) {
            clipboard = linker.cut(shapes, linker.container)
        }

        override fun paste(target: Long?, targetContainer: Pictogram?): List<Long> {
            clipboard.forEach {
                it.layerData?.move(10.0, 10.0)
            }
            linker.paste(target, clipboard, targetContainer)
            val idList = clipboard.map { it.copy.id }
            clipboard = emptyList()
            return idList
        }
    }

    /**
     * Renderable object of this controller.
     */
    val viewModel: ViewModel = ViewModel(linker.container, handler).also { viewModel ->
        viewModel.layer = layer
        viewModel.connections = linker.connectionManager.connections.asSequence().map { it.pictogram }.toSet()
    }

    /**
     * Name of the tab.
     */
    val tabNameProperty: Property<String> = linker.nameProperty

    init {
        linker.connectionManager.onConnectionAdd {
            viewModel += it.pictogram
        }
        linker.connectionManager.onConnectionRemove {
            viewModel -= it.pictogram
        }
        linker.connectionManager.init()
    }

    /**
     * The clipboard is shared between all controller to allow interaction between tabs.
     */
    companion object {
        private val clipboardProperty = property(emptyList<Copy>())
        var clipboard by clipboardProperty
        val clipboardEmptyProperty = clipboardProperty.mapBinding { it.isEmpty() }
    }
}

/**
 * Helper class to manage clipboard entries.
 */
data class Copy(
        val original: ModelElement,
        val copy: ModelElement,
        val layerData: LayerData? = null
)