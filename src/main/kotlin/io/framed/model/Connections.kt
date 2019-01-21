package io.framed.model

import io.framed.PolymorphicSetSerializer
import io.framed.framework.ModelConnection
import kotlinx.serialization.Serializable

@Serializable
class Connections {

    @Serializable(with = PolymorphicSetSerializer::class)
    var connections: Set<ModelConnection<*>> = emptySet()

    fun maxId(): Long = listOfNotNull(
            connections.maxBy { it.id }?.id
    ).max() ?: 0
}