package io.framed

import io.framed.controller.ContainerController
import io.framed.model.*
import io.framed.picto.Layer
import io.framed.picto.TextShape
import io.framed.util.property
import io.framed.view.Application
import io.framed.view.Root
import io.framed.view.TextView
import kotlin.browser.window

/**
 * Entry point.
 */
var name = ""

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
            type = RelationType.AGGREGATION
            sourceCardinality = RelationMultiplicity.ONCE_TO_MANY.value
            targetCardinality = RelationMultiplicity.ONCE.value
        }
    }

    val controller = ContainerController(container, app)

    Root += app

    app.controller = controller
}
