package io.framed.framework

import de.westermann.kobserve.Property
import de.westermann.kobserve.basic.mapBinding
import de.westermann.kobserve.basic.property
import io.framed.framework.pictogram.*

class Controller(
        val linker: ModelLinker<*, *, *>,
        private val layer: Layer
) {

    val handler = object : ViewModelHandler {
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

    val viewModel: ViewModel = ViewModel(linker.container, handler).also { viewModel ->
        viewModel.layer = layer
        viewModel.connections = linker.connectionManager.connections.asSequence().map { it.pictogram }.toSet()
    }

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

    companion object {
        val clipboardProperty = property(emptyList<Copy>())
        var clipboard by clipboardProperty
        val clipboardEmptyProperty = clipboardProperty.mapBinding { it.isEmpty() }
    }
}

data class Copy(
        val original: ModelElement<*>,
        val copy: ModelElement<*>,
        val layerData: LayerData? = null
)