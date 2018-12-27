package io.framed.framework.util

import de.westermann.kobserve.EventHandler
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import org.w3c.dom.events.EventListener
import org.w3c.files.FileReader
import org.w3c.xhr.XMLHttpRequest
import kotlin.browser.document
import kotlin.browser.window

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
 * Apply current dom changes and recalculate all sizes. Executes the given block afterwards.
 *
 * @param timeout Optionally set a timeout for this call. Defaults to 1.
 * @param block Callback
 */
fun async(timeout: Int = 1, block: () -> Unit) {
    if (timeout < 1) throw IllegalArgumentException("Timeout must be greater than 0!")
    window.setTimeout(block, timeout)
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

@Suppress("UNCHECKED_CAST")
fun <T> EventHandler<T>.eventListener(): EventListener = object : EventListener {
    override fun handleEvent(event: Event) {
        (event as? T)?.let(this@eventListener::emit)
    }
}
