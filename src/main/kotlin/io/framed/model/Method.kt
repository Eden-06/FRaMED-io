package io.framed.model

/**
 * @author lars
 */
class Method:Model {
    var name: String = ""
    var type: String = ""
    var parameters: List<Parameter> = emptyList()

    override fun toString(): String =
            "$name(" + parameters.joinToString(", ") { it.toString() } + ")" + type.let {
                if (type.isBlank()) "" else ": $it"
            }.trim()
}

fun Class.method(name: String, type: String = "", init: Method.() -> Unit = {}): Method {
    val method = Method()
    methods += method
    method.name = name
    method.type = type
    method.init()
    return method
}