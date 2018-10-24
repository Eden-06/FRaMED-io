package io.framed

import io.framed.framework.LinkerManager
import io.framed.framework.util.loadAjaxFile
import io.framed.framework.view.Application
import io.framed.linker.*
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
    LinkerManager.register(CompartmentLinker)

    LinkerManager.register(AssociationLinker)
    LinkerManager.register(InheritanceLinker)
    LinkerManager.register(AggregationLinker)
    LinkerManager.register(CompositionLinker)

    Application.init()

    loadAjaxFile("demo.json") {
        File.fromJSON(it)
    }
}
