package io.framed.framework

import de.westermann.kobserve.basic.FunctionAccessor
import de.westermann.kobserve.basic.FunctionProperty
import io.framed.framework.pictogram.ConnectionInfo
import io.framed.framework.pictogram.Pictogram
import io.framed.framework.util.History
import io.framed.framework.util.async
import io.framed.framework.view.ContextMenu
import io.framed.framework.view.MaterialIcon

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

        pictogram.layerProperty.onChange {
            linker.updateLabelBindings()
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
        linker.updateLabelBindings()
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

    fun itemLinkerFor(element: ModelElement<*>): LinkerInfoItem {
        return linkerItemList.find { it.isLinkerFor(element) } ?: TODO()
    }

    fun contextMenu(linker: ShapeLinker<*, *>, contextMenu: ContextMenu) {
        for (item in linkerItemList) {
            if (item.canCreateIn(linker.model)) {
                contextMenu.addItem(MaterialIcon.ADD, "Create ${item.name}") {
                    linker.add(item.createModel()).focus(it.target)
                }
            }
        }
    }

    inline fun <reified L : Linker<*, *>> createLinker(model: ModelElement<*>, parent: Linker<*, *>, connectionManager: ConnectionManager? = null): L {
        val linker = linkerItemList.firstOrNull { it.isLinkerFor(model) && it.canCreateIn(parent.model) }
                ?.createLinker(model, parent, connectionManager)
                ?: throw UnsupportedOperationException("Could not find linker for model of type ${model::class.simpleName}")

        if (linker is L) {
            return linker
        } else {
            throw UnsupportedOperationException("Could not cast linker of type ${linker::class.simpleName} to ${L::class.simpleName}")
        }
    }

    inline fun <reified L : Linker<*, *>> createLinker(model: ModelConnection<*>, connectionManager: ConnectionManager): L {
        val linker = linkerConnectionList.firstOrNull { it.isLinkerFor(model) }
                ?.createLinker(model, connectionManager)
                ?: throw UnsupportedOperationException("Could not find linker for model of type ${model::class.simpleName}")

        if (linker is L) {
            return linker
        } else {
            throw UnsupportedOperationException("Could not cast linker of type ${linker::class.simpleName} to ${L::class.simpleName}")
        }
    }

    fun createConnectionModelByType(type: ConnectionInfo, source: Long, target: Long): ModelConnection<*> {
        return linkerConnectionList.firstOrNull { it.info == type }?.createModel(source, target)
                ?: throw UnsupportedOperationException("Could not find linker for model of type ${type.name}")
    }
}
