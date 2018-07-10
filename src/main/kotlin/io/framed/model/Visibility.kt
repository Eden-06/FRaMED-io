package io.framed.model

/**
 * @author lars
 */
enum class Visibility(
        val symbol: Char
) {
    PUBLIC('+'),
    INTERNAL('~'),
    PROTECTED('#'),
    PRIVATE('-'),
    NONE(' ')
}