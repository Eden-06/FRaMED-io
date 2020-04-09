package io.framed.framework.util

class RegexValidator(private val regex: Regex) {

    fun validate(value: String): Boolean = this.regex.matches(value)

    companion object {
        val IDENTIFIER = RegexValidator("[a-zA-Z]([a-zA-Z0-9_])*".toRegex())
        val SCENENAME = RegexValidator("[a-zA-Z\\s]([a-zA-Z0-9_\\s])*".toRegex())
    }
}
