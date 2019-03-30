package io.framed.framework.pictogram

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

/**
 * @author lars
 */
@Serializable
class Layer {
    private val data: MutableMap<Long, LayerData> = mutableMapOf()

    @Transient
    private val transient: MutableMap<Pictogram, LayerData> = mutableMapOf()

    operator fun get(id: Long?, picto: Pictogram): LayerData = if (id == null) {
        transient.getOrPut(picto) {
            LayerData()
        }
    } else {
        data.getOrPut(id) {
            LayerData()
        }
    }

    operator fun contains(id: Long): Boolean = id in data
}
