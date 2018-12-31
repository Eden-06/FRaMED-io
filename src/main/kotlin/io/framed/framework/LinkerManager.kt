package io.framed.framework

import de.westermann.kobserve.basic.FunctionAccessor
import de.westermann.kobserve.basic.FunctionProperty
import io.framed.framework.pictogram.ConnectionInfo
import io.framed.framework.pictogram.Pictogram
import io.framed.framework.util.History
import io.framed.framework.util.async

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
        var exists = true
        if (convert.size > 1) {
            val infoProperty = FunctionProperty(object : FunctionAccessor<ConnectionInfo> {
                override fun set(value: ConnectionInfo): Boolean {
                    if (exists) {
                        exists = false
                        async {
                            History.group("Set connection type to ${value.name}") {
                                val source = linker.sourceIdProperty.get()
                                val target = linker.targetIdProperty.get()
                                val manager = linker.manager
                                linker.delete()

                                manager.createConnection(source, target, value).focus()
                            }
                        }
                    }
                    return true
                }

                override fun get(): ConnectionInfo {
                    return info.info
                }

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