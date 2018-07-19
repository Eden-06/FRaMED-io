package io.framed

import io.framed.controller.ContainerController
import io.framed.model.*
import io.framed.view.Application
import kotlin.browser.document
import kotlin.browser.window

@Suppress("UNUSED")
fun main(args: Array<String>) {
    window.onload = {
        init()
    }
}

fun init() {
    val app = Application()

    val container = container {
        name = "Economy"
        val account = clazz("Account") {
            attr("amount", "Money")
            attr("id", "String")
            method("doSomething", "void") {
                param("foo", "bar")
            }
        }

        val bank = clazz("Bank") {
            attr("name", "String")
            method("insolvency", "void")
        }

        container {
            name = "Library"
            val book = clazz("Book") {
                attr("isbn", "String")
            }
        }

        relation(bank, account, "customers")
    }

    val controller = ContainerController(container)

    async {
        document.body?.appendChild(app.html)

        app.controller = controller

        async {
            controller.autoLayout()
        }
    }
}

fun async(block: () -> Unit) {
    window.setTimeout(block, 1)
}
