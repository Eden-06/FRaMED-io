package io.framed.visitor

import io.framed.Project
import io.framed.model.*

/**
 * Provides an interface for visitors that are able to visit all model structures and return a (generic) result.
 */
interface Visitor<T> {
    fun getResult() : T

    fun visit(project: Project)

    fun visit(aggregation: Aggregation)

    fun visit(attribute: Attribute)

    fun visit(compartment: Compartment)

    fun visit(clazz: Class)

    fun visit(composition: Composition)

    fun visit(connections: Connections)

    fun visit(createRelationship: CreateRelationship)

    fun visit(destroyRelationship: DestroyRelationship)

    fun visit(event: Event)

    fun visit(eventType: EventType)

    fun visit(fulfillment: Fulfillment)

    fun visit(inheritance: Inheritance)

    fun visit(metadata: Metadata)

    fun visit(method: Method)

    fun visit(modelElementMetadata: ModelElementMetadata)

    fun visit(packageObj: Package)

    fun visit(parameter: Parameter)

    fun visit(relationship: Relationship)

    fun visit(returnEvent: ReturnEvent)

    fun visit(roleType: RoleType)

    fun visit(scene: Scene)
}