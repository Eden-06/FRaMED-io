package io.framed

/**
 * @author lars
 */

/**
 * Transform a camel cased string to a dash cased string.
 *
 * @return Dash cased string.
 */
fun String.toDashCase() = this.replace("([a-z])([A-Z])".toRegex(), "$1-$2").toLowerCase()