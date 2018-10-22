package io.framed.model

import kotlinx.serialization.Serializable

@Serializable
class Connections {
    var associations: Set<Association> = emptySet()
    var aggregations: Set<Aggregation> = emptySet()
    var compositions: Set<Composition> = emptySet()
    var inheritances: Set<Inheritance> = emptySet()
}