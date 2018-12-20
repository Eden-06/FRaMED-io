package io.framed.framework.pictogram

import kotlinx.serialization.Serializable

/**
 * @author lars
 */
@Serializable
class Layer {
    private val data: MutableMap<Long, LayerData> = mutableMapOf()

    operator fun get(id: Long?): LayerData {
        return if (id == null) {
            LayerData()
        } else {
            return data.getOrPut(id) {
                LayerData()
            }
        }
    }
}
