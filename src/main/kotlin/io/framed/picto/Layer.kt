package io.framed.picto

import io.framed.util.Dimension
import io.framed.util.EventHandler
import kotlin.reflect.KProperty

/**
 * @author lars
 */
open class Layer {
    val position: MutableMap<Shape, Dimension> = mutableMapOf()
    val listener: MutableMap<Shape, EventHandler<Boolean>> = mutableMapOf()

    open operator fun get(shape: Shape): Dimension? = position[shape]
    open operator fun set(shape: Shape, dimension: Dimension?) {
        if (dimension == null) {
            position.remove(shape)
        } else {
            position[shape] = dimension
        }

        listener[shape]?.fire(true)
    }

    open operator fun get(shape: Shape, prop: Prop): Double? =
            position[shape]?.let { dimension ->
                when (prop) {
                    Prop.LEFT -> dimension.left
                    Prop.TOP -> dimension.top
                    Prop.WIDTH -> dimension.width
                    Prop.HEIGHT -> dimension.height
                }
            }

    open operator fun set(shape: Shape, prop: Prop, value: Double?) {
        val dimension = position[shape] ?: Dimension(0.0, 0.0, null, null)
        position[shape] = when (prop) {
            Prop.LEFT -> dimension.copy(left = value ?: 0.0)
            Prop.TOP -> dimension.copy(top = value ?: 0.0)
            Prop.WIDTH -> dimension.copy(width = value)
            Prop.HEIGHT -> dimension.copy(height = value)
        }
        listener[shape]?.fire(false)
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
        return listener[shape] ?: run {
            val e = EventHandler<Boolean>()
            listener[shape] = e
            e
        }
    }

    enum class Prop {
        LEFT, TOP, WIDTH, HEIGHT
    }
}

object NoneLayer : Layer() {
    override fun get(shape: Shape): Dimension? = null
    override fun set(shape: Shape, dimension: Dimension?) =
            throw UnsupportedOperationException()

    override fun get(shape: Shape, prop: Prop): Double? = null

    override fun set(shape: Shape, prop: Prop, value: Double?) =
            throw UnsupportedOperationException()
}