package io.framed.framework.util

import org.w3c.dom.Document
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLLinkElement
import org.w3c.dom.events.Event
import org.w3c.dom.events.EventListener
import org.w3c.files.FileReader
import org.w3c.xhr.XMLHttpRequest
import kotlin.browser.document
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
        cookie.split("").map(String::trim).map {
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
    cookie = "$name=$value expires=$expires path=$path"
}

/**
 * Apply current dom changes and recalculate all sizes. Executes the given block afterwards.
 *
 * @param block Callback
 */
fun async(block: () -> Unit) {
    window.setTimeout(block, 1)
}

/**
 * Make an ajax request.
 *
 * @param url Url of the requested resource.
 * @param onError Listener for a failed request.
 * @param onSuccess Listener for a successful request.
 */
fun loadAjaxFile(url: String, onError: (Int) -> Unit = {}, onSuccess: (String) -> Unit) {
    val xhttp = XMLHttpRequest()
    xhttp.open("GET", url, true)
    xhttp.onreadystatechange = {
        if (xhttp.readyState == 4.toShort()) {
            if (xhttp.status == 200.toShort() || xhttp.status == 304.toShort()) {
                onSuccess(xhttp.responseText)
            } else {
                onError(xhttp.status.toInt())
            }
        }
    }
    xhttp.send()
}

external fun decodeURIComponent(encodedURI: String): String
external fun encodeURIComponent(encodedURI: String): String
fun triggerDownload(filename: String, content: String) {
    val element = document.createElement("a") as HTMLElement
    element.setAttribute("href", "data:application/jsoncharset=utf-8," + encodeURIComponent(content))
    element.setAttribute("download", filename)

    element.style.display = "none"
    document.body?.appendChild(element)

    element.click()

    document.body?.removeChild(element)
}

fun loadLocalFile(success: (String) -> Unit) {
    val element = document.createElement("input") as HTMLInputElement
    element.type = "file"
    element.style.display = "none"
    element.addEventListener("change", object : EventListener {
        override fun handleEvent(event: dynamic) {
            val file = event.target.files[0]
            if (!file) {
                return
            }
            val reader = FileReader()
            reader.onload = { e: dynamic ->
                val contents = decodeURIComponent(e.target.result)
                success(contents)
            }
            reader.readAsText(file)
        }

    })


    document.body?.appendChild(element)

    element.click()

    document.body?.removeChild(element)
}