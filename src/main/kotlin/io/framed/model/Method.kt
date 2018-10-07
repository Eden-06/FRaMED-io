package io.framed.model

import io.framed.framework.ModelElement
import kotlinx.serialization.Serializable

/**
 * Model class for an uml model.
 *
 * @author lars
 */
@Serializable
class Method: ModelElement {

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

}

/**
 * Create a new model within the current class.
 *
 * @param name Name of the new model.
 * @param type Optional return model of the new model.
 * @param init Optional builder callback for this model.
 *
 * @return The new model.
 */
fun Class.method(name: String, type: String = "", init: Method.() -> Unit = {}): Method {
    val method = Method()
    methods += method
    method.name = name
    method.type = type
    method.init()
    return method
}