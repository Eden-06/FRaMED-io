package io.framed.model

import io.framed.framework.ModelElement
import kotlinx.serialization.Serializable

/**
 * Model class for an uml model.
 *
 * @author lars
 */
@Serializable
class Method(): ModelElement<Method> {

    constructor(init: (Method) -> Unit) : this() {
        init(this)
    }

    override val id: Long = ModelElement.lastId++

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
    var parameters: List<Parameter> = emptyList()

    override fun maxId(): Long = listOf(
            id,
            parameters.map { it.maxId() }.max() ?: 0
    ).max() ?: id

    override fun copy() = Method {new ->
        new.name = name
        new.type = type
        new.parameters = parameters
    }

}
