package io.framed.model

/**
 * @author lars
 */
class Method {
    var name: String = ""
    var type: String = ""
    var visibility: Visibility = Visibility.NONE
    var parameters: List<Parameter> = emptyList()

    override fun toString(): String =
            "${visibility.symbol ?: ""} $name(" + parameters.joinToString(", ") { it.toString() } + ")" + type.let {
                if (type.isBlank()) "" else ": $it"
            }.trim()
}

fun CompartmentType.method(name: String, type: String = "", visibility: Visibility = Visibility.NONE, init: Method.() -> Unit = {}): Method {
    val method = Method()
    methods += method
    method.name = name
    method.type = type
    method.visibility = visibility
    method.init()
    return method
}