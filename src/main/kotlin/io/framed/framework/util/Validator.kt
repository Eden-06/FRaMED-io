package io.framed.framework.util

class RegexValidator(private val regex: Regex) {
    fun validate(value: String): Boolean = this.regex.matches(value)
}
