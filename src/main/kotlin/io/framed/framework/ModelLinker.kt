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

    fun checkSize()

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


    fun delete(idList: List<Long>) {
        connectionManager.delete(idList)
        for (linker in shapeLinkers) {
            if (linker.id in idList) {
                linker.delete()
            } else if (linker is ModelLinker<*, *, *>) {
                linker.delete(idList)
            }
        }
    }

    fun copy(idList: List<Long>, source: Pictogram): List<Copy> {
        var elements = emptyList<Copy>()
        for (linker in shapeLinkers) {
            if (linker.id in idList) {
                elements += Copy(linker.model, linker.model.copy(), linker.pictogram.export())
            } else if (linker is ModelLinker<*, *, *>) {
                elements += linker.copy(idList, source)
            }
        }
        val found = elements.map { it.original.id }
        for (connection in connectionManager.connections) {
            if (connection.id in idList && connection.id !in found) {
                val model = connection.model
                val sourceCopy = elements.find { it.original.id == model.sourceId }
                val targetCopy = elements.find { it.original.id == model.targetId }
                if (sourceCopy != null && targetCopy != null) {
                    val copy = model.copy()
                    copy.sourceId = sourceCopy.copy.id
                    copy.targetId = targetCopy.copy.id
                    elements += Copy(model, copy)
                }
            }
        }
        return elements
    }

    fun cut(shapes: List<Long>, source: Pictogram): List<Copy> {
        val elements = copy(shapes, source)
        delete(shapes)
        return elements
    }

    fun paste(target: Long?, elements: List<Copy>, targetContainer: Pictogram?) {
        fun paste() {
            for ((_, element, layerData) in elements) {
                if (element is ModelConnection<*>) {
                    connectionManager.add(element)
                } else if (LinkerManager.itemLinkerFor(element).canCreateIn(model)) {
                    add(element, layerData ?: LayerData(), container)
                }
            }
        }

        if (target == null || id == target || targetContainer == null) {
            paste()
        } else {
            for (linker in shapeLinkers) {
                if (linker is ModelLinker<*, *, *>) {
                    linker.paste(target, elements, targetContainer)
                } else if (linker.id == target) {
                    paste()
                    break
                }
            }
        }
    }


    fun add(model: ModelElement<*>, layerData: LayerData, container: Pictogram): ShapeLinker<*, *> {
        val linker = add(model)
        linker.pictogram.import(layerData)
        return linker
    }
}
