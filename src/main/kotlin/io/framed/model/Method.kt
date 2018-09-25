package io.framed.model

/**
 * Model class for an uml method.
 *
 * @author lars
 */
class Method:Model {

    /**
     * Name of this method.
     */
    var name: String = ""

    /**
     * Return type of this method.
     */
    var type: String = ""

    /**
     * List of parameters for this method.
     */
    var parameters: List<Parameter> = emptyList()

}

/**
 * Create a new method within the current class.
 *
 * @param name Name of the new method.
 * @param type Optional return type of the new method.
 * @param init Optional builder callback for this method.
 *
 * @return The new method.
 */
fun Class.method(name: String, type: String = "", init: Method.() -> Unit = {}): Method {
    val method = Method()
    methods += method
    method.name = name
    method.type = type
    method.init()
    return method
}