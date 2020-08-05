package io.framed.framework.linker

import de.westermann.kobserve.property.FunctionAccessor
import de.westermann.kobserve.property.FunctionProperty
import io.framed.framework.ConnectionManager
import io.framed.framework.model.ModelConnection
import io.framed.framework.model.ModelElement
import io.framed.framework.pictogram.ElementInfo
import io.framed.framework.pictogram.Pictogram
import io.framed.framework.pictogram.Shape
import io.framed.framework.util.History
import io.framed.framework.util.async
import io.framed.framework.view.ContextMenu

/**
 * Utility object that performs repetitive tasks at linker initialisation.
 */
object LinkerManager {

    /**
     * Setup sidebar, contextmenu and layer change event for a pictogram.
     */
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

    /**
     * Generate type change and swap fields if possible.
     */
    fun setup(linker: ConnectionLinker<*>, info: LinkerInfoConnection) {
        val convert = linker.canConvert()
        var exists = true
        if (convert.size > 1) {
            val infoProperty = FunctionProperty(object : FunctionAccessor<ElementInfo> {
                override fun set(value: ElementInfo): Boolean {
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

                override fun get(): ElementInfo {
                    return info.info
                }

            })

            linker.sidebar.group("Structure") {
                select("io.framed.exporter.crom.Type", convert, infoProperty) {
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

    /**
     * Setup all pictogram elements of a linker
     */
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

    /**
     * Add a model info entry.
     */
    fun register(linkerInfo: LinkerInfoItem) {
        linkerItemList += linkerInfo
    }

    /**
     * Add a connection info entry.
     */
    fun register(linkerInfo: LinkerInfoConnection) {
        linkerConnectionList += linkerInfo
    }

    var linkerItemList: List<LinkerInfoItem> = emptyList()
    var linkerConnectionList: List<LinkerInfoConnection> = emptyList()

    fun itemLinkerFor(element: ModelElement): LinkerInfoItem {
        return linkerItemList.find { it.isLinkerFor(element) }
                ?: throw NoSuchElementException("Cannot find linker info for model ${element::class.simpleName}")
    }

    fun itemLinkerFor(linker: Linker<*, *>): LinkerInfoItem {
        return linkerItemList.find { it.isLinkerFor(linker) }
                ?: throw NoSuchElementException("Cannot find linker info for linker ${linker::class.simpleName}")
    }

    /**
     * Generate model add entries to the [contextMenu].
     */
    fun contextMenu(linker: ShapeLinker<*, *>, contextMenu: ContextMenu) {
        for (item in linkerItemList) {
            if (item.canCreateIn(linker.model)) {
                contextMenu.addItem(item.info.icon, "Create ${item.info.name}") {
                    val new = linker.add(item.createModel())
                    new.focus(it.target)

                    val left: Double
                    val top: Double

                    if (it.target == linker.pictogram) {
                        left = linker.pictogram.leftOffset - linker.pictogram.root.left
                        top = linker.pictogram.topOffset - linker.pictogram.root.top
                    } else {
                        left = 0.0
                        top = 0.0
                    }
                    new.pictogram.left = it.diagram.x - left
                    new.pictogram.top = it.diagram.y - top
                }
            }
        }
    }

    /**
     * Create a [Linker] instance for the given [model].
     */
    inline fun <reified L : Linker<*, *>> createLinker(model: ModelElement, parent: Linker<*, *>, connectionManager: ConnectionManager? = null): L {
        val linker = linkerItemList.firstOrNull { it.isLinkerFor(model) && it.canCreateIn(parent.model) }
                ?.createLinker(model, parent, connectionManager)
                ?: throw UnsupportedOperationException("Could not find linker for model of type ${model::class.simpleName}")

        if (linker is L) {
            return linker
        } else {
            throw UnsupportedOperationException("Could not cast linker of type ${linker::class.simpleName} to ${L::class.simpleName}")
        }
    }

    /**
     * Create a [Linker] instance for the given model [model].
     */
    inline fun <reified L : Linker<*, *>> createLinker(model: ModelConnection, connectionManager: ConnectionManager): L {
        val linker = linkerConnectionList.firstOrNull { it.isLinkerFor(model) }
                ?.createLinker(model, connectionManager)
                ?: throw UnsupportedOperationException("Could not find linker for model of type ${model::class.simpleName}")

        if (linker is L) {
            return linker
        } else {
            throw UnsupportedOperationException("Could not cast linker of type ${linker::class.simpleName} to ${L::class.simpleName}")
        }
    }

    /**
     * Create a [ModelConnection] by the given [type], [source] and [target].
     */
    fun createConnectionModelByType(type: ElementInfo, source: Long, target: Long): ModelConnection {
        return linkerConnectionList.firstOrNull { it.info == type }?.createModel(source, target)
                ?: throw NoSuchElementException("Could not find connection linker for model of type ${type.name}")
    }
}

/**
 * Recursively  get the root shape.
 */
private val Shape.root: Shape
    get() = parent?.root ?: this