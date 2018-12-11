package io.framed.framework.pictogram

import de.westermann.kobserve.EventHandler
import io.framed.framework.util.Dimension
import io.framed.framework.util.History
import io.framed.framework.util.HistoryLayer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

/**
 * @author lars
 */
@Serializable
class Layer {
    val position: MutableMap<Long, Dimension> = mutableMapOf()

    val data: MutableMap<Long, MutableMap<String, String>> = mutableMapOf()

    fun getData(id: Long, property: String, default: Any): String {
        return data.getOrElse(id) { mutableMapOf() }.getOrElse(property) { default.toString() }
    }

    fun setData(id: Long, property: String, value: String) {
        data.getOrPut(id) { mutableMapOf() }[property] = value
    }

    @Transient
    val listener: MutableMap<Long, EventHandler<Boolean>> = mutableMapOf()

    operator fun get(shape: Shape): Dimension? = position[shape.id]
    operator fun set(shape: Shape, dimension: Dimension?) {
        val id = shape.id ?: return
        if (dimension == null) {
            position.remove(id)
        } else {
            position[id] = dimension
        }

        listener[id]?.emit(true)
    }

    operator fun get(shape: Shape, prop: Prop): Double? {
        return position[shape.id ?: return null]?.let { dimension ->
            when (prop) {
                Prop.LEFT -> dimension.left
                Prop.TOP -> dimension.top
                Prop.WIDTH -> dimension.width
                Prop.HEIGHT -> dimension.height
            }
        }
    }

    operator fun set(shape: Shape, prop: Prop, value: Double?) {
        val id = shape.id ?: return
        val dimension = position[id] ?: Dimension(0.0, 0.0, null, null)

        val oldValue = when (prop) {
            Prop.LEFT -> dimension.left
            Prop.TOP -> dimension.top
            Prop.WIDTH -> dimension.width
            Prop.HEIGHT -> dimension.height
        }

        position[id] = when (prop) {
            Prop.LEFT -> dimension.copy(left = value ?: 0.0)
            Prop.TOP -> dimension.copy(top = value ?: 0.0)
            Prop.WIDTH -> dimension.copy(width = value)
            Prop.HEIGHT -> dimension.copy(height = value)
        }
        listener[id]?.emit(false)

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

    fun onUpdate(shape: Shape): EventHandler<Boolean>? {
        val id = shape.id ?: return null
        return listener.getOrPut(id) { EventHandler() }
    }

    enum class Prop {
        LEFT, TOP, WIDTH, HEIGHT
    }
}

inline fun <reified T : Any> Pictogram.data(default: T) = PictogramData(T::class, this, default)

@Suppress("UNCHECKED_CAST")
class PictogramData<T : Any>(
        val type: KClass<T>,
        val picto: Pictogram,
        val default: T
) {
    operator fun getValue(thisRef: Any, property: KProperty<*>): T {
        val id = picto.id ?: return default
        val data = picto.layer.getData(id, property.name, default)
        return when (type) {
            String::class -> data as T
            Boolean::class -> data.toBoolean() as T
            else -> default
        }
    }

    operator fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        val id = picto.id ?: return
        picto.layer.setData(id, property.name, value.toString())
    }
}
