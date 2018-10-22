package io.framed.framework

import io.framed.framework.pictogram.Layer
import io.framed.framework.pictogram.Shape
import io.framed.framework.pictogram.ViewModel
import io.framed.framework.pictogram.ViewModelHandler
import io.framed.framework.util.Dimension
import io.framed.framework.util.History
import io.framed.framework.util.Property

class Controller(
        val linker: ModelLinker<*, *, *>,
        val layer: Layer
) {

    val handler = object : ViewModelHandler {
        override fun canConnectionStart(source: Shape): Boolean =
                linker.connectionManager.canConnectionStart(source).isNotEmpty()

        override fun canConnectionCreate(source: Shape, target: Shape): Boolean =
                linker.connectionManager.canConnectionCreate(source, target).isNotEmpty()

        override fun createConnection(source: Shape, target: Shape) =
                linker.connectionManager.createConnection(source, target)

        override fun canDropShape(shape: Shape, target: Shape): Boolean = linker.canDropShape(shape, target)

        override fun dropShape(shape: Shape, target: Shape) = linker.dropShape(shape, target)

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
        linker.setPosition { event: SetPosition ->
            layer[event.linker.pictogram] = Dimension(event.position)
        }

        linker.connectionManager.init()
    }
}
