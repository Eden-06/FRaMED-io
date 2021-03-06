package io.framed.model

import io.framed.framework.model.ModelElement
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable

/**
 * Model class for an uml model.
 *
 * @author lars
 */
@Serializable
class Method() : ModelElement() {

    constructor(init: (Method) -> Unit) : this() {
        init(this)
    }

    /**
     * Name of this model.
     */
    var name: String = "unnamed"

    /**
     * Return model of this model.
     */
    var type: String = ""

    /**
     * List of parameters for this model.
     */
    var parameters: List<@Polymorphic Parameter> = emptyList()

    override fun maxId(): Long = listOf(
            id,
            parameters.map { it.maxId() }.maxOrNull() ?: 0
    ).maxOrNull() ?: id

    override fun copy() = Method { new ->
        new.name = name
        new.type = type
        new.parameters = parameters.map { it.copy() }
    }

}
