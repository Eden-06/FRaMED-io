package io.framed.visitor

import io.framed.Project
import io.framed.model.*


abstract class ProjectTreeVisitor<T>(startObject : T) : Visitor<T> {

    var output : T = startObject

    override fun getResult(): T {
        return output
    }

    override fun visit(project: Project) {
        traverseProjectPreorder(project)
        for (connection in project.connections.connections) {
            connection.acceptVisitor(this)
        }
        project.root.acceptVisitor(this)
        traverseProjectPostorder(project)
    }

    override fun visit(aggregation: Aggregation) {
        traverseAggregationPreorder(aggregation)
        traverseAggregationPostorder(aggregation)
    }

    override fun visit(attribute: Attribute)  {
        traverseAttributePreorder(attribute)
        traverseAttributePostorder(attribute)
    }

    override fun visit(compartment: Compartment)  {
        traverseCompartmentPreorder(compartment)
        for (attribute in compartment.attributes) {
            attribute.acceptVisitor(this)
        }
        for (method in compartment.methods) {
            method.acceptVisitor(this)
        }
        for (element in compartment.children) {
            element.acceptVisitor(this)
        }
        traverseCompartmentPostorder(compartment)
    }

    override fun visit(clazz: Class)  {
        traverseClassPreorder(clazz)
        for (attribute in clazz.attributes) {
            attribute.acceptVisitor(this)
        }
        for (method in clazz.methods) {
            method.acceptVisitor(this)
        }
        traverseClassPostorder(clazz)
    }

    override fun visit(composition: Composition)  {
        traverseCompositionPreorder(composition)
        traverseCompositionPostorder(composition)
    }

    override fun visit(connections: Connections)  {
        traverseConnectionsPreorder(connections)
        for (connection in connections.connections) {
            connection.acceptVisitor(this)
        }
        traverseConnectionsPostorder(connections)
    }

    override fun visit(createRelationship: CreateRelationship)  {
        traverseCreateRelationshipPreorder(createRelationship)
        traverseCreateRelationshipPostorder(createRelationship)
    }

    override fun visit(destroyRelationship: DestroyRelationship)  {
        traverseDestroyRelationshipPreorder(destroyRelationship)
        traverseDestroyRelationshipPostorder(destroyRelationship)
    }

    override fun visit(event: Event)  {
        traverseEventPreorder(event)
        traverseEventPostorder(event)
    }

    override fun visit(eventType: EventType)  {
        TODO("Not yet implemented")
    }

    override fun visit(fulfillment: Fulfillment)  {
        traverseFulfillmentPreorder(fulfillment)
        traverseFulfillmentPostorder(fulfillment)
    }

    override fun visit(inheritance: Inheritance)  {
        traverseInheritancePreorder(inheritance)
        traverseInheritancePostorder(inheritance)
    }

    override fun visit(metadata: Metadata)  {
        TODO("Not yet implemented")
    }

    override fun visit(method: Method)  {
        traverseMethodPreorder(method)
        for (parameter in method.parameters) {
            parameter.acceptVisitor(this)
        }
        traverseMethodPostorder(method)
    }

    override fun visit(modelElementMetadata: ModelElementMetadata)  {
        TODO("Not yet implemented")
    }

    override fun visit(packageObj: Package)  {
        traversePackagePreorder(packageObj)
        for (modelElement in packageObj.children) {
            modelElement.acceptVisitor(this)
        }
        traversePackagePostorder(packageObj)
    }

    override fun visit(parameter: Parameter)  {
        traverseParameterPreorder(parameter)
        traverseParameterPostorder(parameter)
    }

    override fun visit(relationship: Relationship)  {
        traverseRelationshipPreorder(relationship)
        traverseRelationshipPostorder(relationship)
    }

    override fun visit(returnEvent: ReturnEvent)  {
        traverseReturnEventPreorder(returnEvent)
        traverseReturnEventPostorder(returnEvent)
    }

    override fun visit(roleType: RoleType)  {
        traverseRoleTypePreorder(roleType)
        for (method in roleType.methods) {
            method.acceptVisitor(this)
        }
        for (attribute in roleType.attributes) {
            attribute.acceptVisitor(this)
        }
        traverseRoleTypePostorder(roleType)
    }

    override fun visit(scene: Scene)  {
        traverseScenePreorder(scene)
        for (attribute in scene.attributes) {
            attribute.acceptVisitor(this)
        }
        for (child in scene.children) {
            child.acceptVisitor(this)
        }
        traverseScenePostorder(scene)
    }

    abstract fun traverseProjectPreorder(project: Project)

    abstract fun traverseProjectPostorder(project: Project)

    abstract fun traverseAggregationPreorder(aggregation: Aggregation)

    abstract fun traverseAggregationPostorder(aggregation: Aggregation)

    abstract fun traverseAttributePreorder(attribute: Attribute)

    abstract fun traverseAttributePostorder(attribute: Attribute)

    abstract fun traverseCompartmentPreorder(compartment: Compartment)

    abstract fun traverseCompartmentPostorder(compartment: Compartment)

    abstract fun traverseClassPreorder(clazz: Class)

    abstract fun traverseClassPostorder(clazz: Class)

    abstract fun traverseCompositionPreorder(composition: Composition)

    abstract fun traverseCompositionPostorder(composition: Composition)

    abstract fun traverseConnectionsPreorder(connections: Connections)

    abstract fun traverseConnectionsPostorder(connections: Connections)

    abstract fun traverseCreateRelationshipPreorder(createRelationship: CreateRelationship)

    abstract fun traverseCreateRelationshipPostorder(createRelationship: CreateRelationship)

    abstract fun traverseDestroyRelationshipPreorder(destroyRelationship: DestroyRelationship)

    abstract fun traverseDestroyRelationshipPostorder(destroyRelationship: DestroyRelationship)

    abstract fun traverseEventPreorder(event: Event)

    abstract fun traverseEventPostorder(event: Event)

    abstract fun traverseEventTypePreorder(eventType: EventType)

    abstract fun traverseEventTypePostorder(eventType: EventType)

    abstract fun traverseFulfillmentPreorder(fulfillment: Fulfillment)

    abstract fun traverseFulfillmentPostorder(fulfillment: Fulfillment)

    abstract fun traverseInheritancePreorder(inheritance: Inheritance)

    abstract fun traverseInheritancePostorder(inheritance: Inheritance)

    abstract fun traverseMetadataPreorder(metadata: Metadata)

    abstract fun traverseMetadataPostorder(metadata: Metadata)

    abstract fun traverseModelElementMetadataPreorder(modelElementMetadata: ModelElementMetadata)

    abstract fun traverseModelElementMetadataPostorder(modelElementMetadata: ModelElementMetadata)

    abstract fun traverseMethodPreorder(method: Method)

    abstract fun traverseMethodPostorder(method: Method)

    abstract fun traversePackagePreorder(packageObj: Package)

    abstract fun traversePackagePostorder(packageObj: Package)

    abstract fun traverseParameterPreorder(parameter: Parameter)

    abstract fun traverseParameterPostorder(parameter: Parameter)

    abstract fun traverseRelationshipPreorder(relationship: Relationship)

    abstract fun traverseRelationshipPostorder(relationship: Relationship)

    abstract fun traverseReturnEventPreorder(returnEvent: ReturnEvent)

    abstract fun traverseReturnEventPostorder(returnEvent: ReturnEvent)

    abstract fun traverseRoleTypePreorder(roleType: RoleType)

    abstract fun traverseRoleTypePostorder(roleType: RoleType)

    abstract fun traverseScenePreorder(scene: Scene)

    abstract fun traverseScenePostorder(scene: Scene)

}