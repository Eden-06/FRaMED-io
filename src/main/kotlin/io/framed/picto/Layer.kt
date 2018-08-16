package io.framed.picto

import io.framed.util.Dimension
import kotlin.reflect.KProperty

/**
 * @author lars
 */
class Layer {
    val position: MutableMap<Shape, Dimension> = mutableMapOf()

    operator fun get(shape: Shape): Dimension? = position[shape]
    operator fun set(shape: Shape, dimension: Dimension?) {
        if (dimension==null) {
            position.remove(shape)
        } else {
            position[shape] = dimension
        }
    }

    operator fun getValue(shape: Shape, property: KProperty<*>): Double? =
            position[shape]?.let { dimension ->
                when (property.name) {
                    "left" -> dimension.left
                    "top" -> dimension.top
                    "width" -> dimension.width
                    "height" -> dimension.height
                    else -> null
                }
            }

    operator fun setValue(shape: Shape, property: KProperty<*>, value: Double?) {
        val dimension = position[shape] ?: Dimension(0.0, 0.0, null, null)
        position[shape] = when (property.name) {
            "left" -> dimension.copy(left = value ?: 0.0)
            "top" -> dimension.copy(top = value ?: 0.0)
            "width" -> dimension.copy(width = value)
            "height" -> dimension.copy(height = value)
            else -> dimension
        }
    }
}