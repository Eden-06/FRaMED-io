package io.framed

import io.framed.framework.Controller
import io.framed.framework.ControllerManager
import io.framed.framework.LinkerManager
import io.framed.framework.pictogram.Layer
import io.framed.framework.view.Application
import io.framed.framework.view.Root
import io.framed.linker.*
import io.framed.model.*
import kotlinx.serialization.json.JSON
import org.w3c.xhr.XMLHttpRequest
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

    LinkerManager.register(AttributeLinker)
    LinkerManager.register(ClassLinker)
    LinkerManager.register(ContainerLinker)
    LinkerManager.register(EventLinker)
    LinkerManager.register(MethodLinker)
    LinkerManager.register(RoleTypeLinker)

    LinkerManager.register(RelationLinker)


    /*
    // Create a dummy diagram
    val model = container {
        name = "Economy"

        val account = clazz("Account") {
            attr("amount", "Money")
            attr("shape", "String")
            method("doSomething", "void") {
                param("foo", "bar")
            }
        }

        val bank = clazz("Bank") {
            attr("name", "String")
            method("insolvency", "void")
        }

        roleType("RoleType") {}

        event(EventType.MESSAGE, "terminate contract"){}

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
    */

    Application.init()

    loadFile("demo.json") {
        val file = JSON.parse<File>(it)

        ControllerManager.layers = file.layer

        ControllerManager.display(ContainerLinker(file.root))
    }
}

fun loadFile(url: String, onError: (Int) -> Unit = {}, onSuccess: (String) -> Unit) {
    val xhttp = XMLHttpRequest();
    xhttp.open("GET", url, true);
    xhttp.onreadystatechange = {
        if (xhttp.readyState == 4.toShort()) {
            if (xhttp.status == 200.toShort() || xhttp.status == 304.toShort()) {
                onSuccess(xhttp.responseText)
            } else {
                onError(xhttp.status.toInt())
            }
        }
    }
    xhttp.send();
}