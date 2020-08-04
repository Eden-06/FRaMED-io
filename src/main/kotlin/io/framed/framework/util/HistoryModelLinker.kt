package io.framed.framework.util

import io.framed.framework.ControllerManager
import io.framed.framework.linker.ModelLinker

class HistoryModelLinker(
        private val oldModel: ModelLinker<*, *, *>,
        private val newModel: ModelLinker<*, *, *>
) : HistoryItem {
    override val description: String = "io.framed.export.ecore.Switch tab"

    override fun undo() {
        ControllerManager.display(oldModel)
    }

    override fun redo() {
        ControllerManager.display(newModel)
    }
}