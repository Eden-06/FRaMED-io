package io.framed.framework

import io.framed.framework.pictogram.Pictogram

object LinkerManager {

    private fun setupPictogram(linker: Linker<*, *>, pictogram: Pictogram) {

        if (!linker.sidebar.isEmpty) {
            pictogram.hasSidebar = true
            pictogram.onSidebar { event ->
                linker.apply {
                    sidebar.onOpen(event)
                    sidebar.open()
                }
            }
        }

        if (!linker.contextMenu.isEmpty) {
            pictogram.hasContextMenu = true
            pictogram.onContext { event ->
                linker.apply {
                    contextMenu.onOpen(event)
                    contextMenu.open(event)
                }
            }
        }
    }

    fun setup(linker: Linker<*, *>) {
        setupPictogram(linker, linker.pictogram)

        linker.pictogram.acceptRelation = linkerRelationList.filter { it.canStart(linker) }.isNotEmpty()

        if (linker is PreviewLinker<*, *, *>) {
            setupPictogram(linker, linker.preview)
        }
        if (linker is ModelLinker<*, *, *>) {
            setupPictogram(linker, linker.container)
        }
    }

    fun register(linkerInfo: LinkerInfoItem) {
        linkerItemList += linkerInfo
    }

    fun register(linkerInfo: LinkerInfoRelation) {
        linkerRelationList += linkerInfo
    }

    var linkerItemList: List<LinkerInfoItem> = emptyList()
    var linkerRelationList: List<LinkerInfoRelation> = emptyList()
}