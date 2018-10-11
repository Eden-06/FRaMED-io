package io.framed.framework

import io.framed.framework.pictogram.Pictogram
import io.framed.framework.util.History
import io.framed.framework.util.Validator
import io.framed.framework.util.async
import io.framed.framework.util.property

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
            pictogram.onContextMenu { event ->
                linker.apply {
                    contextMenu.onOpen(event)
                    contextMenu.open(event)
                }
            }
        }
    }

    fun setup(linker: ConnectionLinker<*>, info: LinkerInfoConnection) {
        val convert = linker.canConvert()
        if (convert.size > 1) {
            val infoProperty = property(getter = {
                info.info
            }, setter = {
                async {
                    History.startGroup()
                    val source = linker.sourceShapeProperty.get()
                    val target = linker.targetShapeProperty.get()
                    val parent = linker.parent
                    linker.delete()

                    parent.createConnection(source, target, it).focus()

                    History.endGroup()
                }

                Validator.Result.VALID
            })

            linker.sidebar.group("Structure") {
                select("Type", convert, infoProperty) {
                    it.name
                }
            }
        }
        if (linker.canSwap(info.info)) {
            linker.sidebar.group("Structure") {
                button("Swap direction") {
                    linker.swap()
                }
            }
        }

        setup(linker)
    }

    fun setup(linker: Linker<*, *>) {
        setupPictogram(linker, linker.pictogram)

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

    fun register(linkerInfo: LinkerInfoConnection) {
        linkerConnectionList += linkerInfo
    }

    var linkerItemList: List<LinkerInfoItem> = emptyList()
    var linkerConnectionList: List<LinkerInfoConnection> = emptyList()
}