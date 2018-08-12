package io.framed.util

import org.w3c.dom.Document
import kotlin.browser.window
import kotlin.js.Date

/**
 * @author lars
 */

/**
 * Transform a camel cased string to a dash cased string.
 *
 * @return Dash cased string.
 */
fun String.toDashCase() = this.replace("([a-z])([A-Z])".toRegex(), "$1-$2").toLowerCase()

/**
 * Get a cookie by name. Null if no cookie with given name exists.
 *
 * @param name Cookie name.
 *
 * @return The cookie value.
 */
fun Document.getCookie(name: String): String? =
        cookie.split(";").map(String::trim).map {
            val h = it.split("=").map(String::trim)
            h.first() to h.getOrNull(1)
        }.toMap()[name]

/**
 * Set a cookie.
 *
 * @param name Name of cookie.
 * @param value Value of cookie.
 * @param validity Period of validity in seconds.
 * @param path Valid path.
 */
fun Document.setCookie(name: String, value: String, validity: Int = 0, path: String = "/") {
    val expires = Date(Date.now() + validity * 1000).toUTCString()
    cookie = "$name=$value; expires=$expires; path=$path"
}

/**
 * Apply current dom changes and recalculate all sizes. Executes the given block afterwards.
 *
 * @param block Callback
 */
fun async(block: () -> Unit) {
    window.setTimeout(block, 1)
}