package io.framed.framework.pictogram

import de.westermann.kobserve.Property
import de.westermann.kobserve.basic.property
import io.framed.framework.util.trackHistory
import kotlinx.serialization.Optional
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class LayerData(
        private var left: Double = 0.0,
        private var top: Double = 0.0,
        @Optional
        private var width: Double = 0.0,
        @Optional
        private var height: Double = 0.0,
        @Optional
        private var autosize: Boolean = true,
        @Optional
        private val data: MutableMap<String, String> = mutableMapOf(),
        @Optional
        private var labels: List<Label> = emptyList(),
        @Transient
        private val trackHistory: Boolean = true
) {

    @Transient
    val leftProperty = property(this::left)

    @Transient
    val topProperty = property(this::top)

    @Transient
    val widthProperty = property(this::width)

    @Transient
    val heightProperty = property(this::height)

    @Transient
    val autosizeProperty = property(this::autosize)

    @Transient
    val propertyMap: MutableMap<String, Property<String?>> = mutableMapOf()

    @Transient
    val labelsProperty = property(this::labels)

    init {
        if (trackHistory) {
            leftProperty.trackHistory(this)
            topProperty.trackHistory(this)
            widthProperty.trackHistory(this)
            heightProperty.trackHistory(this)
            autosizeProperty.trackHistory(this)
            labelsProperty.trackHistory(this)
        }
    }

    fun data(name: String): Property<String?> {
        return propertyMap.getOrPut(name) {
            val prop = property(data[name])
            if (trackHistory) {
                prop.trackHistory()
            }
            prop.onChange {
                val value = prop.value
                if (value == null) {
                    data -= name
                } else {
                    data[name] = value
                }
            }
            prop
        }
    }

    fun import(layerData: LayerData) {
        leftProperty.value = layerData.leftProperty.value
        topProperty.value = layerData.topProperty.value
        widthProperty.value = layerData.widthProperty.value
        heightProperty.value = layerData.heightProperty.value
        autosizeProperty.value = layerData.autosizeProperty.value
        labelsProperty.value = layerData.labelsProperty.value.map { it.copy() }

        propertyMap.clear()
        for ((key, value) in layerData.propertyMap.entries) {
            data(key).value = value.value
        }
    }

    fun export(): LayerData {
        return copy()
    }

    fun move(x: Double, y: Double) {
        leftProperty.value += x
        topProperty.value += y
    }
}