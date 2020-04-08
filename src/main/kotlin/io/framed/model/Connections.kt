package io.framed.model

import io.framed.framework.model.ModelConnection
import kotlinx.serialization.Serializable

@Serializable
class Connections {

    var connections: Set<ModelConnection> = emptySet()

    fun maxId(): Long = listOfNotNull(
            connections.maxBy { it.id }?.id
    ).max() ?: 0
}
