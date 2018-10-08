package io.framed.framework

import io.framed.framework.pictogram.Layer
import io.framed.framework.pictogram.ViewModel
import io.framed.framework.util.Dimension
import io.framed.framework.util.History
import io.framed.framework.util.Property

class Controller(
        val linker: ModelLinker<*, *, *>,
        val layer: Layer
) {

    val viewModel: ViewModel = ViewModel(linker.container).also { viewModel ->
        viewModel.layer = layer
        viewModel.onRelationDraw {
            linker.createConnection(it.first, it.second)
        }
        viewModel.connections = linker.connections.map { it.pictogram }
    }

    val tabNameProperty: Property<String> = linker.nameProperty

    init {
        linker.onConnectionAdd {
            viewModel += it.pictogram
        }
        linker.onConnectionRemove {
            viewModel -= it.pictogram
        }
        linker.setPosition { event: SetPosition ->
            layer[event.linker.pictogram] = Dimension(event.position)
        }
    }
}
