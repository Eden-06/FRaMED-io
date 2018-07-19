package io.framed.model

/**
 * @author lars
 */
class Parameter:Model {
    var name: String = ""
    var type: String = ""

    override fun toString(): String =
            name + type.let {
                if (type.isBlank()) "" else ": $it"
            }

}

fun Method.param(name: String, type: String = ""): Parameter {
    val parameter = Parameter()
    parameters += parameter
    parameter.name = name
    parameter.type = type
    return parameter
}