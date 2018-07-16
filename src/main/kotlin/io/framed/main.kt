package io.framed

import io.framed.controller.controller
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
        val d = diagram {
            val account = compartmentType("Account") {
                attr("amount", "Money")
                attr("id", "String", Visibility.PRIVATE)
                method("doSomething", "void") {
                    param("foo", "bar")
                }
            }

            val bank = compartmentType("Bank") {
                attr("name", "String")
                method("insolvency", "void")
            }

            relation(bank, account, "customers")
        }

        val controller = d.controller()

        document.body?.appendChild(controller.view.html)
    }
}

fun async(block: () -> Unit) {
    window.setTimeout(block, 1)
}
