package io.framed

import io.framed.framework.ControllerManager
import io.framed.framework.LinkerManager
import io.framed.framework.util.loadAjaxFile
import io.framed.framework.view.Application
import io.framed.linker.*
import io.framed.model.*
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
    PolymorphicSerializer.registerSerializer(Relationship::class, Relationship.serializer(), "io.framed.model.Relationship")
    PolymorphicSerializer.registerSerializer(Fulfillment::class, Fulfillment.serializer(), "io.framed.model.Fulfillment")
    PolymorphicSerializer.registerSerializer(Composition::class, Composition.serializer(), "io.framed.model.Composition")
    PolymorphicSerializer.registerSerializer(Inheritance::class, Inheritance.serializer(), "io.framed.model.Inheritance")
    PolymorphicSerializer.registerSerializer(Attribute::class, Attribute.serializer(), "io.framed.model.Attribute")
    PolymorphicSerializer.registerSerializer(Class::class, Class.serializer(), "io.framed.model.Class")
    PolymorphicSerializer.registerSerializer(Compartment::class, Compartment.serializer(), "io.framed.model.Compartment")
    PolymorphicSerializer.registerSerializer(Event::class, Event.serializer(), "io.framed.model.Event")
    PolymorphicSerializer.registerSerializer(Method::class, Method.serializer(), "io.framed.model.Method")
    PolymorphicSerializer.registerSerializer(Package::class, Package.serializer(), "io.framed.model.Package")
    PolymorphicSerializer.registerSerializer(Parameter::class, Parameter.serializer(), "io.framed.model.Parameter")
    PolymorphicSerializer.registerSerializer(RoleType::class, RoleType.serializer(), "io.framed.model.RoleType")

    LinkerManager.register(AttributeLinker)
    LinkerManager.register(ClassLinker)
    LinkerManager.register(PackageLinker)
    LinkerManager.register(EventLinker)
    LinkerManager.register(MethodLinker)
    LinkerManager.register(RoleTypeLinker)
    LinkerManager.register(CompartmentLinker)

    LinkerManager.register(RelationshipLinker)
    LinkerManager.register(InheritanceLinker)
    LinkerManager.register(FulfillmentLinker)
    LinkerManager.register(CompositionLinker)

    Application.init()

    loadAjaxFile("demo.json") {
        ControllerManager.file = File.fromJSON(it) ?: File.empty()
    }
    //ControllerManager.file = File.empty()
}
