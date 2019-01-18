package io.framed.model

import kotlinx.serialization.Serializable

@Serializable
class Connections {
    var associations: Set<Association> = emptySet()
    var aggregations: Set<Aggregation> = emptySet()
    var compositions: Set<Composition> = emptySet()
    var inheritances: Set<Inheritance> = emptySet()

    fun maxId(): Long = listOfNotNull(
            associations.maxBy { it.id }?.id,
            aggregations.maxBy { it.id }?.id,
            compositions.maxBy { it.id }?.id,
            inheritances.maxBy { it.id }?.id
    ).max() ?: 0
}