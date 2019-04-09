package io.framed.framework.util

import de.westermann.kobserve.event.EventHandler
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

inline fun <reified V : HTMLElement> createHtmlView(tag: String? = null): V {
    var tagName: String
    if (tag != null) {
        tagName = tag
    } else {
        tagName = V::class.js.name.toLowerCase().replace("html([a-z]*)element".toRegex(), "$1")
        if (tagName.isBlank()) {
            tagName = "div"
        }
    }
    return document.createElement(tagName) as V
}

inline fun <reified T> EventHandler<T>.bind(element: HTMLElement, event: String) {
    val listener = object : EventListener {
        override fun handleEvent(event: Event) {
            this@bind.emit(event as T)
        }
    }
    var isAttached = false

    val updateState = {
        if (isEmpty() && isAttached) {
            element.removeEventListener(event, listener)
            isAttached = false
        } else if (!isEmpty() && !isAttached) {
            element.addEventListener(event, listener)
            isAttached = true
        }
    }

    onAttach = updateState
    onDetach = updateState
    updateState()
}

fun Number.format(digits: Int): String = this.asDynamic().toFixed(digits)

external fun delete(p: dynamic): Boolean = definedExternally

fun delete(thing: dynamic, key: String) {
    delete(thing[key])
}

fun interval(timeout: Int, block: () -> Unit): Int {
    if (timeout < 1) throw IllegalArgumentException("Timeout must be greater than 0!")
    return window.setInterval(block, timeout)
}

fun clearInterval(id: Int) {
    window.clearInterval(id)
}

fun Exception.log() {
    val stack = asDynamic().stack as? String ?: ""
    val type = asDynamic().name as? String ?: ""
    val head = "Exception $type ($message)"
    console.error(head + "\n" + stack.split("\n").joinToString("\n") { "    at $it" })
}
