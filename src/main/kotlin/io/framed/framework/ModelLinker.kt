package io.framed.framework

import de.westermann.kobserve.Property
import io.framed.framework.pictogram.BoxShape
import io.framed.framework.pictogram.LayerData
import io.framed.framework.pictogram.Pictogram
import io.framed.framework.pictogram.Shape
import io.framed.framework.util.History
import io.framed.framework.view.dialog
import io.framed.framework.view.textView

/**
 * @author lars
 */
interface ModelLinker<M : ModelElement<M>, P : Shape, R : Shape> : PreviewLinker<M, P, R> {

    override val nameProperty: Property<String>

    val shapeLinkers: Set<ShapeLinker<*, *>>

    val connectionManager: ConnectionManager

    val container: BoxShape

    fun getLinkerById(id: Long): ShapeLinker<*, *>? =
            shapeLinkers.find { it.id == id }

    fun redraw(linker: ShapeLinker<*, *>)

    fun canDropShape(element: Long, target: Long): Boolean {
        val shapeLinker = shapeLinkers.find { it.id == element } ?: return false
        val targetLinker = shapeLinkers.find { it.id == target } ?: return false

        return LinkerManager.linkerItemList.find { it.isLinkerOfType(shapeLinker.model) }?.canCreateIn(targetLinker.model)
                ?: false
    }

    fun dropShape(element: Long, target: Long) {
        val elementLinker = getLinkerById(element) ?: throw IllegalArgumentException()
        val targetLinker = getLinkerById(target) ?: throw IllegalArgumentException()

        val connectionCount = connectionManager.listConnections(elementLinker.id).size

        val elementName = elementLinker.model::class.simpleName?.toLowerCase() ?: "element"
        val targetName = targetLinker.model::class.simpleName?.toLowerCase() ?: "container"

        if (connectionCount > 0) {
            dialog {
                title = "Move $elementName to $targetName"
                contentView.textView("How should $connectionCount connection(s) be handled.")
                closable = true
                addButton("Move and delete", true) {
                    History.group("Move $elementName to $targetName") {
                        remove(elementLinker)
                        targetLinker.add(elementLinker.model.copy())
                    }
                }
                addButton("Move and keep") {
                    History.group("Move $elementName to $targetName") {
                        val connectionList = connectionManager.listConnections(elementLinker.id).map { it.model }

                        val oldId = elementLinker.id
                        remove(elementLinker)
                        val model = elementLinker.model.copy()
                        targetLinker.add(model)
                        val newId = model.id

                        connectionList.forEach {
                            if (it.sourceId == oldId) {
                                it.sourceId = newId
                            }
                            if (it.targetId == oldId) {
                                it.targetId = newId
                            }
                            connectionManager.add(it)
                        }
                    }
                }
                addButton("Abort")
            }.open()
        } else {
            History.group("Move $elementName to $targetName") {
                remove(elementLinker)
                targetLinker.add(elementLinker.model.copy())
            }
        }
    }


    fun delete(shapes: List<Long>) {
        for (linker in shapeLinkers) {
            if (linker.id in shapes) {
                linker.delete()
            } else if (linker is ModelLinker<*, *, *>) {
                linker.delete(shapes)
            }
        }
    }

    fun copy(shapes: List<Long>, source: Pictogram): List<Pair<ModelElement<*>, LayerData>> {
        var elements = emptyList<Pair<ModelElement<*>, LayerData>>()
        for (linker in shapeLinkers) {
            if (linker.id in shapes) {
                val layerData = if (container == source || linker !is PreviewLinker<*, *, *>) {
                    linker.pictogram.export()
                } else {
                    linker.flatPreview.export()
                }
                elements += linker.model.copy() to layerData
            } else if (linker is ModelLinker<*, *, *>) {
                elements += linker.copy(shapes, source)
            }
        }
        return elements
    }

    fun cut(shapes: List<Long>, source: Pictogram): List<Pair<ModelElement<*>, LayerData>> {
        val elements = copy(shapes, source)
        delete(shapes)
        return elements
    }

    fun paste(target: Long?, elements: List<Pair<ModelElement<*>, LayerData>>, targetContainer: Pictogram?) {
        if (target == null || id == target || targetContainer == null) {
            for ((element, layerData) in elements) {
                if (LinkerManager.itemLinkerFor(element).canCreateIn(model)) {
                    add(element, layerData, container)
                }
            }
        } else {
            for (linker in shapeLinkers) {
                if (linker is ModelLinker<*, *, *>) {
                    linker.paste(target, elements, targetContainer)
                } else if (linker.id == target) {
                    for ((element, layerData) in elements) {
                        if (LinkerManager.itemLinkerFor(element).canCreateIn(model)) {
                            add(element, layerData, targetContainer)
                        }
                    }
                    break
                }
            }
        }
    }


    fun add(model: ModelElement<*>, layerData: LayerData, container: Pictogram): ShapeLinker<*, *> {
        val linker = add(model)
        if (container == pictogram) {
            if (linker is PreviewLinker<*, *, *>) {
                linker.flatPreview.import(layerData)
            }
        } else if (container == this.container) {
            linker.pictogram.import(layerData)
        }
        return linker
    }
}