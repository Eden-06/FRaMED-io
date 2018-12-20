package io.framed.framework

import de.westermann.kobserve.Property
import io.framed.framework.pictogram.Layer
import io.framed.framework.pictogram.ViewModel
import io.framed.framework.pictogram.ViewModelHandler

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

        override fun copy(shapes: List<Long>) = linker.copy(shapes)

        override fun cut(shapes: List<Long>) = linker.cut(shapes)

        override fun paste(target: Long) = linker.paste(target)
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
}
