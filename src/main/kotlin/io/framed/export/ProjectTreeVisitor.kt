package io.framed.export

import io.framed.Project
import io.framed.model.*


// TODO Rekursiv????
abstract class ProjectTreeVisitor<T>(startObject : T) : Visitor<T> {

    var output : T = startObject

    override fun visit(project: Project): T {

        traverseProjectPreorder(project)
        for (connection in project.connections.connections) {
            println(connection::class.js)
            connection.acceptVisitor(this)
        }
        for (modelElement in project.root.children) {
            modelElement.acceptVisitor(this)
        }
        traverseProjectPostorder(project)
        println("Finish visiting")
        print(this.output)
        return this.output
    }

    override fun visit(aggregation: Aggregation) : T {
        traverseAggregationPreorder(aggregation)
        traverseAggregationPostorder(aggregation)
        return this.output
    }

    override fun visit(attribute: Attribute) : T {
        traverseAttributePreorder(attribute)
        traverseAttributePostorder(attribute)
        return this.output
    }

    override fun visit(compartment: Compartment) : T {
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
        return this.output
    }

    override fun visit(clazz: Class) : T {
        traverseClassPreorder(clazz)
        for (attribute in clazz.attributes) {
            attribute.acceptVisitor(this)
        }
        for (method in clazz.methods) {
            method.acceptVisitor(this)
        }
        traverseClassPostorder(clazz)
        return this.output
    }

    override fun visit(composition: Composition) : T {
        traverseCompositionPreorder(composition)
        traverseCompositionPostorder(composition)
        return this.output
    }

    override fun visit(connections: Connections) : T {
        traverseConnectionsPreorder(connections)
        for (connection in connections.connections) {
            connection.acceptVisitor(this)
        }
        traverseConnectionsPostorder(connections)
        return this.output
    }

    override fun visit(createRelationship: CreateRelationship) : T {
        traverseCreateRelationshipPreorder(createRelationship)
        traverseCreateRelationshipPostorder(createRelationship)
        return this.output
    }

    override fun visit(destroyRelationship: DestroyRelationship) : T {
        traverseDestroyRelationshipPreorder(destroyRelationship)
        traverseDestroyRelationshipPostorder(destroyRelationship)
        return this.output
    }

    override fun visit(event: Event) : T {
        traverseEventPreorder(event)
        traverseEventPostorder(event)
        return this.output
    }

    override fun visit(eventType: EventType) : T {
        TODO("Not yet implemented")
    }

    override fun visit(fulfillment: Fulfillment) : T {
        traverseFulfillmentPreorder(fulfillment)
        traverseFulfillmentPostorder(fulfillment)
        return this.output
    }

    override fun visit(inheritance: Inheritance) : T {
        traverseInheritancePreorder(inheritance)
        traverseInheritancePostorder(inheritance)
        return this.output
    }

    override fun visit(metadata: Metadata) : T {
        TODO("Not yet implemented")
    }

    override fun visit(method: Method) : T {
        traverseMethodPreorder(method)
        for (parameter in method.parameters) {
            parameter.acceptVisitor(this)
        }
        traverseMethodPostorder(method)
        return this.output
    }

    override fun visit(modelElementMetadata: ModelElementMetadata) : T {
        TODO("Not yet implemented")
    }

    override fun visit(packageObj: Package) : T {
        traversePackagePreorder(packageObj)
        for (modelElement in packageObj.children) {
            modelElement.acceptVisitor(this)
        }
        traversePackagePostorder(packageObj)
        return this.output
    }

    override fun visit(parameter: Parameter) : T {
        traverseParameterPreorder(parameter)
        traverseParameterPostorder(parameter)
        return this.output
    }

    override fun visit(relationship: Relationship) : T {
        traverseRelationshipPreorder(relationship)
        traverseRelationshipPostorder(relationship)
        return this.output
    }

    override fun visit(returnEvent: ReturnEvent) : T {
        traverseReturnEventPreorder(returnEvent)
        traverseReturnEventPostorder(returnEvent)
        return this.output
    }

    override fun visit(roleType: RoleType) : T {
        traverseRoleTypePreorder(roleType)
        for (method in roleType.methods) {
            method.acceptVisitor(this)
        }
        for (attribute in roleType.attributes) {
            attribute.acceptVisitor(this)
        }
        traverseRoleTypePostorder(roleType)
        return this.output
    }

    override fun visit(scene: Scene) : T {
        traverseScenePreorder(scene)
        for (attribute in scene.attributes) {
            attribute.acceptVisitor(this)
        }
        for (child in scene.children) {
            child.acceptVisitor(this)
        }
        traverseScenePostorder(scene)
        return this.output
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