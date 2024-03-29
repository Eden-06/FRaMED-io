package io.framed

import io.framed.framework.*
import io.framed.framework.linker.LinkerManager
import io.framed.framework.util.loadAjaxFile
import io.framed.framework.view.Application
import io.framed.linker.*
import io.framed.model.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.modules.SerializersModule
import kotlinx.browser.window

/**
 * Entry point of the application.
 */
@Suppress("UNUSED")
fun main() {
    // Wait for the body to load.
    window.onload = {
        init()
    }
}

/**
 * Startup the application
 */
fun init() {
    LinkerManager.register(RelationshipLinker)
    LinkerManager.register(FulfillmentLinker)
    LinkerManager.register(CompositionLinker)
    LinkerManager.register(AggregationLinker)
    LinkerManager.register(InheritanceLinker)
    LinkerManager.register(CreateRelationshipLinker)
    LinkerManager.register(DestroyRelationshipLinker)
    LinkerManager.register(RoleImplicationLinker)
    LinkerManager.register(RoleEquivalenceLinker)
    LinkerManager.register(RoleProhibitionLinker)

    LinkerManager.register(AttributeLinker)
    LinkerManager.register(MethodLinker)
    LinkerManager.register(RoleTypeLinker)
    LinkerManager.register(EventLinker)
    LinkerManager.register(ReturnEventLinker)
    LinkerManager.register(ClassLinker)
    LinkerManager.register(PackageLinker)
    LinkerManager.register(RoleGroupLinker)
    LinkerManager.register(CompartmentLinker)
    LinkerManager.register(SceneLinker)

    // Startup ui
    Application.init()

    // Load demo project
    loadAjaxFile("demo.json") {
        ControllerManager.project = Project.fromJSON(it) ?: Project.empty()
    }
}
