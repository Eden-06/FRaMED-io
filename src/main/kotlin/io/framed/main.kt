package io.framed

import io.framed.controller.ContainerController
import io.framed.model.*
import io.framed.util.async
import io.framed.view.Application
import io.framed.view.Root
import kotlin.browser.window

/**
 * Entry point.
 */
@Suppress("UNUSED")
fun main(args: Array<String>) {
    // Wait for the body to load.
    window.onload = {
        init()
    }
}

/**
 * Startup the application
 */
fun init() {
    val app = Application()

    // Create a dummy diagram
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
            val shelf = clazz("Shelf") {
                method("add", "void") {
                    param("book", "Book")
                }
            }
            relation(book, shelf)
        }
        relation(bank, account, "customers") {
            type = Relation.Type.AGGREGATION
            sourceCardinality = "1..*"
            targetCardinality = "5"
        }
    }

    val controller = ContainerController(container)

    Root += app

    app.controller = controller

    // Wait for the sizes to be calculated and perform simple auto layouting
    async {
        controller.autoLayout()
    }
}
