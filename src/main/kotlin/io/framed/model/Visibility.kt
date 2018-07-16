package io.framed.model

/**
 * @author lars
 */
enum class Visibility(
        val symbol: Char?
) {
    PUBLIC('+'),
    INTERNAL('~'),
    PROTECTED('#'),
    PRIVATE('-'),
    NONE(null);

    companion object {
        fun parse(char: Char) = values().find { char == it.symbol } ?: NONE
    }
}