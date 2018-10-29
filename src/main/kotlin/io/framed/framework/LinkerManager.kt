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
            }, setter = { connectionInfo ->
                async {
                    History.group("Set connection type to ${connectionInfo.name}") {
                        val source = linker.sourceIdProperty.get()
                        val target = linker.targetIdProperty.get()
                        val manager = linker.manager
                        linker.delete()

                        manager.createConnection(source, target, connectionInfo).focus()
                    }
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
            setupPictogram(linker, linker.listPreview)
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