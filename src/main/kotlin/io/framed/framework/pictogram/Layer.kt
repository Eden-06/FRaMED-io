package io.framed.framework.pictogram

import io.framed.framework.util.Dimension
import io.framed.framework.util.EventHandler
import io.framed.framework.util.History
import io.framed.framework.util.HistoryLayer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlin.reflect.KProperty

/**
 * @author lars
 */
@Serializable
class Layer {
    val position: MutableMap<Long, Dimension> = mutableMapOf()

    @Transient
    val listener: MutableMap<Long, EventHandler<Boolean>> = mutableMapOf()

    operator fun get(shape: Shape): Dimension? = position[shape.id]
    operator fun set(shape: Shape, dimension: Dimension?) {
        if (dimension == null) {
            position.remove(shape.id)
        } else {
            position[shape.id] = dimension
        }

        listener[shape.id]?.fire(true)
    }

    operator fun get(shape: Shape, prop: Prop): Double? =
            position[shape.id]?.let { dimension ->
                when (prop) {
                    Prop.LEFT -> dimension.left
                    Prop.TOP -> dimension.top
                    Prop.WIDTH -> dimension.width
                    Prop.HEIGHT -> dimension.height
                }
            }

    operator fun set(shape: Shape, prop: Prop, value: Double?) {
        val dimension = position[shape.id] ?: Dimension(0.0, 0.0, null, null)

        val oldValue = when (prop) {
            Prop.LEFT -> dimension.left
            Prop.TOP -> dimension.top
            Prop.WIDTH -> dimension.width
            Prop.HEIGHT -> dimension.height
        }

        position[shape.id] = when (prop) {
            Prop.LEFT -> dimension.copy(left = value ?: 0.0)
            Prop.TOP -> dimension.copy(top = value ?: 0.0)
            Prop.WIDTH -> dimension.copy(width = value)
            Prop.HEIGHT -> dimension.copy(height = value)
        }
        listener[shape.id]?.fire(false)

        History.push(HistoryLayer(this, shape, prop, oldValue, value))
    }

    operator fun getValue(shape: Shape, property: KProperty<*>): Double? =
            get(
                    shape,
                    when (property.name) {
                        "left" -> Prop.LEFT
                        "top" -> Prop.TOP
                        "width" -> Prop.WIDTH
                        "height" -> Prop.HEIGHT
                        else -> throw IllegalArgumentException()
                    }
            )

    operator fun setValue(shape: Shape, property: KProperty<*>, value: Double?) =
            set(
                    shape,
                    when (property.name) {
                        "left" -> Prop.LEFT
                        "top" -> Prop.TOP
                        "width" -> Prop.WIDTH
                        "height" -> Prop.HEIGHT
                        else -> throw IllegalArgumentException()
                    },
                    value
            )

    fun onUpdate(shape: Shape): EventHandler<Boolean> {
        return listener[shape.id] ?: run {
            val e = EventHandler<Boolean>()
            listener[shape.id] = e
            e
        }
    }

    enum class Prop {
        LEFT, TOP, WIDTH, HEIGHT
    }
}