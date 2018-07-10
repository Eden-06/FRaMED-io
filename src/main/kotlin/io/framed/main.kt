package io.framed

import io.framed.controller.CompartmentTypeController
import io.framed.model.*
import kotlin.browser.document
import kotlin.browser.window

@Suppress("UNUSED")
fun main(args: Array<String>) {
    window.onload = {
        init()
    }
}

fun init() {
    async {
        val c1 = compartmentType("Account") {
            attr("amount", "Money")
            attr("id", "String")
            method("doSomething", "void") {
                param("foo", "bar")
            }
        }

        val controller = CompartmentTypeController(c1)

        document.body?.appendChild(controller.compartmentView.html)
    }
}

fun async(block: () -> Unit) {
    window.setTimeout(block, 1)
}
