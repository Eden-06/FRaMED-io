package io.framed.framework.util

import io.framed.framework.pictogram.Layer
import io.framed.framework.pictogram.Shape

class HistoryLayer(
        val layer: Layer,
        val shape: Shape,
        var map: Map<Layer.Prop, Value>
) : HistoryItem {

    constructor(layer: Layer, shape: Shape, prop: Layer.Prop, oldValue: Double?, newValue: Double?) :
            this(layer, shape, mapOf(prop to Value(oldValue, newValue)))

    override fun undo() {
        map.entries.forEach { (prop, value) ->
            layer[shape, prop] = value.oldValue
        }
        layer.onUpdate(shape).fire(true)
    }

    override fun redo() {
        map.entries.forEach { (prop, value) ->
            layer[shape, prop] = value.newValue
        }
        layer.onUpdate(shape).fire(true)
    }

    override fun shouldAdd(item: HistoryItem): Boolean {
        if (item is HistoryLayer && item.shape.id == shape.id) {
            push(item.map)

            return false
        }
        return true
    }

    private fun push(newMap: Map<Layer.Prop, Value>) {
        newMap.entries.forEach { (prop, value) ->
            val item = map[prop]?.copy(newValue = value.newValue) ?: value
            map += prop to item
        }
    }

    data class Value(
            val oldValue: Double?,
            val newValue: Double?
    )
}
