package io.framed.framework.linker

import de.westermann.kobserve.Property
import de.westermann.kobserve.property.mapBinding
import de.westermann.kobserve.property.property
import io.framed.framework.ConnectionManager
import io.framed.framework.Copy
import io.framed.framework.model.ModelConnection
import io.framed.framework.model.ModelElement
import io.framed.framework.pictogram.*
import io.framed.framework.util.History
import io.framed.framework.util.Point
import io.framed.framework.view.Icon
import io.framed.framework.view.dialog
import io.framed.framework.view.textView
import io.framed.model.Fulfillment
import kotlin.math.absoluteValue

/**
 * This interface adds control methods to the [Linker].
 *
 * A [ModelLinker] is a Linker which can be represented as a tab. It allows auto layout and copy paste.
 * A ModelLinker can be shown in flat- or list-preview. The root Linker must be an ModelLinker
 *
 * @author lars
 */
interface ModelLinker<M : ModelElement, P : Shape, R : Shape> : PreviewLinker<M, P, R> {

    override val nameProperty: Property<String>

    /**
     * Set of direct child linkers to this linker.
     */
    val shapeLinkers: Set<ShapeLinker<*, *>>

    fun generateChildrenIdList(): List<Long> = shapeLinkers.flatMap {
        (if (it is ModelLinker<*, *, *>) it.generateChildrenIdList() else emptyList()) + it.id
    }

    /**
     * Reference to the [ConnectionManager] of the project.
     */
    val connectionManager: ConnectionManager

    val container: BoxShape
    val borderBox: BoxShape
    val autoLayoutBox: BoxShape

    val borderShapes: MutableList<Shape>

    fun getLinkerById(id: Long): ShapeLinker<*, *>? =
        shapeLinkers.find { it.id == id }

    /**
     * Function to query all linkers with the specified id from all child elements of this linker.
     * This includes all linkers where this instance is an ancestor.
     */
    fun getChildLinkerById(id: Long): ShapeLinker<*, *>? {

        // Create a queue for all linkers that need to be checked.
        val todoLinkerQueue = ArrayDeque(shapeLinkers)
        while (todoLinkerQueue.isNotEmpty()) {
            val linker = todoLinkerQueue.removeFirst()

            if (linker.id == id) {
                return linker
            } else {
                // If the linker has children at them to the todoList.
                if (linker is ModelLinker<*, *, *>) {
                    todoLinkerQueue.addAll(linker.shapeLinkers)
                }
                continue
            }
        }
        // The linker was not found.
        return null
    }

    fun redraw(linker: ShapeLinker<*, *>)

    /**
     * Checks if a shape can be dropped in another element.
     */
    fun canDropShape(element: Long, target: Long): Boolean {
        val shapeLinker = this.getChildLinkerById(element) ?: return false
        val targetLinker = this.getChildLinkerById(target) ?: return false

        return LinkerManager.linkerItemList
            .find {
                it.isLinkerFor(shapeLinker.model)
            }?.canCreateIn(targetLinker.model) ?: false
    }

    /**
     * Moves a dragged and dropped shape from the original parent linker to the new parent linker.
     */
    fun dropShape(element: Long, target: Long) {
        val elementLinker = getChildLinkerById(element)
            ?: throw IllegalArgumentException("Cannot find the moved element for the d&d with id $element")
        val targetLinker = getChildLinkerById(target)
            ?: throw IllegalArgumentException("Cannot find the target element for the d&d with id $target")

        val connectionCount = connectionManager.listConnections(elementLinker.id).size

        val elementName = elementLinker.model::class.simpleName?.lowercase() ?: "element"
        val targetName = targetLinker.model::class.simpleName?.lowercase() ?: "container"

        // Avoid moving if the target is equal to the current parent.
        if (elementLinker.parent == targetLinker) {
            return
        }

        // Check if connections are present that need to be rerouted.
        if (connectionCount > 0) {
            dialog {
                title = "Move $elementName to $targetName"
                contentView.textView("How should $connectionCount connection(s) be handled.")
                closable = true
                addButton("Move and delete", true) {
                    History.group("Move $elementName to $targetName") {
                        // Remove it from the previous parent and add to the new parent.
                        elementLinker.parent?.remove(elementLinker)
                        targetLinker.add(elementLinker.model.copy())
                    }
                }
                addButton("Move and keep") {
                    History.group("Move $elementName to $targetName") {
                        val connectionList = connectionManager.listConnections(elementLinker.id).map { it.model }

                        // Save information of the old linker and remove it from the previous parent.
                        val oldId = elementLinker.id
                        elementLinker.parent?.remove(elementLinker)

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
                // Remove it from the previous parent and add to the new parent.
                elementLinker.parent?.remove(elementLinker)
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
                if (element is ModelConnection) {
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

    fun add(model: ModelElement, layerData: LayerData, container: Pictogram): ShapeLinker<*, *> {
        val linker = add(model)
        linker.pictogram.import(layerData)
        return linker
    }

    /**
     * Create and updated ports for linker that have fulfillment relations that span over a container.
     *
     * This functions creates port if they are necessary and removes them if they are no longer needed.
     *
     * FIXME: Ports use a problematic addressing schema that causes problems for nested elements.
     */
    fun updatePorts(autoPosition: Boolean = false) {
        val directIdList = generateChildrenIdList()

        // Find Fulfillment connections that run over a border.
        var neededBorderViews = connectionManager.connections
            .filter {
                it.model is Fulfillment
            }
            .mapNotNull {
                val s = it.sourceIdProperty.value
                val t = it.targetIdProperty.value

                if (s in directIdList && t !in directIdList) {
                    // Source port will be created, enable targeting the source port.
                    it.enablePortTargetingSource(true)
                    s
                } else if (s !in directIdList && t in directIdList) {
                    // Target port will be created, enable targeting the target port.
                    it.enablePortTargetingTarget(true)
                    t
                } else {
                   null
                }
            }
            .distinct()

        // Remove a shapes that already own a port.
        for (shape in borderShapes) {
            val id = shape.id?.absoluteValue ?: continue
            if (id !in neededBorderViews) {
                borderBox -= shape
                borderShapes -= shape
            } else {
                neededBorderViews -= id
            }
        }
        for (shape in borderBox.shapes) {
            val id = shape.id?.absoluteValue ?: continue
            if (id >= 0 && id in neededBorderViews) {
                neededBorderViews -= id
            }
        }

        val autoLayout: MutableSet<Shape> = mutableSetOf()

        if (autoPosition) {
            autoLayout += borderShapes
        }

        for (id in neededBorderViews) {

            // Create a port for the id
            val shape = iconShape(property<Icon?>(null), id = -id) {
                style {
                    background = color(255, 255, 255)
                    border {
                        style = Border.BorderStyle.SOLID
                        width = box(Border.DEFAULT_WIDTH)
                        color = box(color(0, 0, 0, 0.3))
                    }
                    padding = box(10.0)
                }
            }

            // Check if it is a new border view
            if (-id !in borderBox.layer) {
                autoLayout += shape
            }

            borderBox += shape
            borderShapes += shape
        }

        for (shape in autoLayout) {
            val id = -(shape.id ?: continue)
            // Find connection
            val connection = connectionManager.connections
                .find {
                    val s = it.sourceIdProperty.value
                    val t = it.targetIdProperty.value

                    (s == id || t == id) &&
                            ((s in directIdList && t !in directIdList) ||
                                    (s !in directIdList && t in directIdList))
                }


            if (connection != null) {
                // Get source and target shape
                val source = connectionManager.getLinkerById(connection.sourceIdProperty.value)
                val target = connectionManager.getLinkerById(connection.targetIdProperty.value)

                if (source != null && target != null) {

                    val d = pictogram.dimension
                    val s = source.pictogram.center
                    val t = target.pictogram.center

                    // Calculate intersection points with the four dimension lines
                    val points = listOf(
                        d.left to calcIntersection(s.x, s.y, t.x, t.y, d.left),
                        d.left + d.width to calcIntersection(s.x, s.y, t.x, t.y, d.left + d.width),
                        calcIntersection(s.y, s.x, t.y, t.x, d.top) to d.top,
                        calcIntersection(s.y, s.x, t.y, t.x, d.top + d.height) to d.top + d.height
                    ).mapNotNull { (x, y) ->
                        if (x == null || y == null) null else Point(x, y)
                    }.filter {
                        it in d
                    }

                    // Find the nearest intersection to the outer element
                    val nearest = if (source.id == id) {
                        points.minByOrNull { it.distance(t) }
                    } else {
                        points.minByOrNull { it.distance(s) }
                    }

                    // Set the position
                    if (nearest != null) {
                        shape.left = nearest.x - pictogram.leftOffset
                        shape.top = nearest.y - pictogram.topOffset
                    }
                }
            }
        }

        updateLabelBindings()
    }

    override fun updateLabelBindings() {
        for (shape in borderShapes) {
            var label = shape.labels.find { it.id == "name" }
            if (label == null) {
                label = Label(id = "name")
                shape.labels += label
            }

            val id = -(shape.id ?: continue)
            val linker = shapeLinkers.find { it.id == id } as? PreviewLinker<*, *, *> ?: continue

            val typeName = linker.typeName
            val name = linker.nameProperty.mapBinding { "$typeName: $it" }

            if (label.textProperty.isBound) {
                label.textProperty.unbind()
            }
            label.textProperty.bind(name)

            shape.labelsProperty.onChange.emit(Unit)
        }
    }
}

/**
 * Calculates the `y` for a given `x` and and a given line defined by two points.
 */
private fun calcIntersection(x1: Double, y1: Double, x2: Double, y2: Double, x: Double): Double? {
    if (x1 == x2) return null
    return (y2 - y1) / (x2 - x1) * (x - x1) + y1
}
