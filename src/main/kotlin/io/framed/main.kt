package io.framed

import io.framed.linker.ContainerLinker
import io.framed.model.*
import io.framed.view.Application
import io.framed.view.Root
import kotlinx.serialization.json.JSON
import kotlin.browser.window

/**
 * Entry point of the application.
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
    val model = container {
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

        val rtype = roleType("RoleType"){

        }

        val stop = even

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

    val json = JSON.indented.stringify(model)
    val container = JSON.parse<Container>(json)

    val linker = ContainerLinker(container, app)

    Root += app

    app.linker = linker
}
