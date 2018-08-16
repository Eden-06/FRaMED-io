package io.framed.picto

import io.framed.util.EventHandler

/**
 * @author lars
 */
class ViewModel(
        val container: BoxShape
) {
    var layer: Layer
        get() = container.layer
        set(value) {
            container.setLayer(value)
            onLayerChange.fire(Unit)
        }

    var relations: List<Relation> = emptyList()

    val onRelationAdd = EventHandler<Relation>()
    val onRelationRemove = EventHandler<Relation>()

    operator fun plusAssign(relation: Relation) {
        relations += relation
        onRelationAdd.fire(relation)
    }
    operator fun minusAssign(relation: Relation) {
        relations -= relation
        onRelationRemove.fire(relation)
    }

    val onLayerChange = EventHandler<Unit>()
}