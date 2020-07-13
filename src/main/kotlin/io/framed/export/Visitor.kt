package io.framed.export

import io.framed.Project
import io.framed.model.*

interface Visitor<T> {
    fun visit(project: Project): T

    fun visit(aggregation: Aggregation): T

    fun visit(attribute: Attribute): T

    fun visit(compartment: Compartment): T

    fun visit(clazz: Class): T

    fun visit(composition: Composition): T

    fun visit(connections: Connections): T

    fun visit(createRelationship: CreateRelationship): T

    fun visit(destroyRelationship: DestroyRelationship): T

    fun visit(event: Event): T

    fun visit(eventType: EventType): T

    fun visit(fulfillment: Fulfillment): T

    fun visit(inheritance: Inheritance): T

    fun visit(metadata: Metadata): T

    fun visit(method: Method): T

    fun visit(modelElementMetadata: ModelElementMetadata): T

    fun visit(packageObj: Package): T

    fun visit(parameter: Parameter): T

    fun visit(relationship: Relationship): T

    fun visit(returnEvent: ReturnEvent): T

    fun visit(roleType: RoleType): T

    fun visit(scene: Scene): T
}