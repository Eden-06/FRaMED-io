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
        var account: CompartmentType? = null
        var bank: CompartmentType? = null

        val d = diagram {
            account = compartmentType("Account") {
                attr("amount", "Money")
                attr("id", "String", Visibility.PRIVATE)
                method("doSomething", "void") {
                    param("foo", "bar")
                }
            }

            bank = compartmentType("Bank") {
                attr("name", "String")
                method("insolvency", "void")
            }

            relation(bank!!, account!!, "customers")
        }

        val controller = d.controller()

        controller.compartments[account!!]?.top = 10.0
        controller.compartments[account!!]?.left = 10.0
        controller.compartments[account!!]?.width = 300.0
        controller.compartments[account!!]?.height = 200.0

        controller.compartments[bank!!]?.top = 300.0
        controller.compartments[bank!!]?.left = 300.0
        controller.compartments[bank!!]?.width = 200.0
        controller.compartments[bank!!]?.height = 200.0

        document.body?.appendChild(controller.view.html)
    }
}

fun async(block: () -> Unit) {
    window.setTimeout(block, 1)
}
