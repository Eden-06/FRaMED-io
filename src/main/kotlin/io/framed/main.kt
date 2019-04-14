package io.framed

import io.framed.framework.*
import io.framed.framework.util.loadAjaxFile
import io.framed.framework.view.Application
import io.framed.linker.*
import io.framed.model.*
import kotlinx.serialization.KSerializer
import kotlin.browser.window

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
    // Register relations
    register(RelationshipLinker, Relationship.serializer())
    register(FulfillmentLinker, Fulfillment.serializer())
    register(CompositionLinker, Composition.serializer())
    register(AggregationLinker, Aggregation.serializer())
    register(InheritanceLinker, Inheritance.serializer())
    register(CreateRelationshipLinker, CreateRelationship.serializer())
    register(DestroyRelationshipLinker, DestroyRelationship.serializer())

    // Register model elements
    // The order is important for the context menu
    register(Parameter.serializer())
    register(AttributeLinker, Attribute.serializer())
    register(MethodLinker, Method.serializer())
    register(RoleTypeLinker, RoleType.serializer())
    register(EventLinker, Event.serializer())
    register(ReturnEventLinker, ReturnEvent.serializer())
    register(ClassLinker, Class.serializer())
    register(PackageLinker, Package.serializer())
    register(CompartmentLinker, Compartment.serializer())
    register(SceneLinker, Scene.serializer())

    // Startup ui
    Application.init()

    // Load demo file
    loadAjaxFile("demo.json") {
        ControllerManager.file = File.fromJSON(it) ?: File.empty()
    }
}

const val PACKAGE_NAME = "io.framed.model"

/**
 * Register model element for polymorphic serializer. Through reified generic parameter and common package
 * the instance and qualified name can be inferred.
 *
 * @param serializer The serializer instance. A generator function is automatically created by the `Serializable` annotation.
 */
inline fun <reified M : ModelElement<M>> register(serializer: KSerializer<M>) {
    val className = M::class.simpleName!!
    PolymorphicSerializer.registerSerializer(M::class, serializer, "$PACKAGE_NAME.$className")
}

/**
 * Register linkerInfo and serializer in one step to prevent omitting one of it.
 * @see register
 *
 * @param info LinkerInfoItem of a model shape
 * @param serializer The serializer instance. A generator function is automatically created by the `Serializable` annotation.
 */
inline fun <reified M : ModelElement<M>> register(info: LinkerInfoItem, serializer: KSerializer<M>) {
    register(serializer)
    LinkerManager.register(info)
}

/**
 * Register linkerInfo and serializer in one step to prevent omitting one of it.
 * @see register
 *
 * @param info LinkerInfoConnection of a model relation
 * @param serializer The serializer instance. A generator function is automatically created by the `Serializable` annotation.
 */
inline fun <reified M : ModelElement<M>> register(info: LinkerInfoConnection, serializer: KSerializer<M>) {
    register(serializer)
    LinkerManager.register(info)
}
