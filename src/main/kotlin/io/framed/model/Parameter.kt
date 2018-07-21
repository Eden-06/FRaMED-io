package io.framed.model

/**
 * Model class for an uml parameter.
 *
 * @author lars
 */
class Parameter:Model {

    /**
     * Name of this parameter.
     */
    var name: String = ""

    /**
     * Type of this parameter.
     */
    var type: String = ""

    override fun toString(): String =
            name + type.let {
                if (type.isBlank()) "" else ": $it"
            }

}

/**
 * Create a new parameter within the current method.
 *
 * @param name Name of the new parameter.
 * @param type Optional type of the new parameter.
 *
 * @return The new parameter.
 */
fun Method.param(name: String, type: String = ""): Parameter {
    val parameter = Parameter()
    parameters += parameter
    parameter.name = name
    parameter.type = type
    return parameter
}