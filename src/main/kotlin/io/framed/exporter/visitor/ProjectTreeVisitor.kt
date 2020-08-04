package io.framed.exporter.visitor

import io.framed.Project
import io.framed.framework.model.ModelElement
import io.framed.model.*

/**
 * Abstract implementation of a Visitor that traverses the project tree (while traversing the "Package" of a project
 * two times in the beginning) and provides an interface to preorder as well as postorder traversal.
 */
abstract class ProjectTreeVisitor<T>(val initialValue : T) : Visitor<T> {

    protected var result : T = initialValue

    private var parentStack : ArrayList<ModelElement> = ArrayList()

    override fun getResultAndReset(): T {
        val finalResult = result
        result = initialValue
        parentStack = ArrayList()
        return finalResult
    }

    override fun visit(project: Project) {
        traverseProjectPreorder(project)
        project.root.acceptVisitor(this)
        for (connection in project.connections.connections) {
            connection.acceptVisitor(this)
        }
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
        parentStack.add(compartment)
        for (attribute in compartment.attributes) {
            attribute.acceptVisitor(this)
        }
        for (method in compartment.methods) {
            method.acceptVisitor(this)
        }
        for (element in compartment.children) {
            element.acceptVisitor(this)
        }
        parentStack.removeAt(parentStack.lastIndex)
        traverseCompartmentPostorder(compartment)
    }

    override fun visit(clazz: Class)  {
        traverseClassPreorder(clazz)
        parentStack.add(clazz)
        for (attribute in clazz.attributes) {
            attribute.acceptVisitor(this)
        }
        for (method in clazz.methods) {
            method.acceptVisitor(this)
        }
        parentStack.removeAt(parentStack.lastIndex)
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

    override fun visit(fulfillment: Fulfillment)  {
        traverseFulfillmentPreorder(fulfillment)
        traverseFulfillmentPostorder(fulfillment)
    }

    override fun visit(inheritance: Inheritance)  {
        traverseInheritancePreorder(inheritance)
        traverseInheritancePostorder(inheritance)
    }

    override fun visit(method: Method)  {
        traverseMethodPreorder(method)
        parentStack.add(method)
        for (parameter in method.parameters) {
            parameter.acceptVisitor(this)
        }
        parentStack.removeAt(parentStack.lastIndex)
        traverseMethodPostorder(method)
    }

    override fun visit(packageObj: Package)  {
        traversePackagePreorder(packageObj)
        parentStack.add(packageObj)
        for (modelElement in packageObj.children) {
            modelElement.acceptVisitor(this)
        }
        // Double traversal because Types have to be detected beforehand
        for (modelElement in packageObj.children) {
            modelElement.acceptVisitor(this)
        }
        parentStack.removeAt(parentStack.lastIndex)
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
        parentStack.add(roleType)
        for (method in roleType.methods) {
            method.acceptVisitor(this)
        }
        for (attribute in roleType.attributes) {
            attribute.acceptVisitor(this)
        }
        parentStack.removeAt(parentStack.lastIndex)
        traverseRoleTypePostorder(roleType)
    }

    override fun visit(scene: Scene)  {
        traverseScenePreorder(scene)
        parentStack.add(scene)
        for (attribute in scene.attributes) {
            attribute.acceptVisitor(this)
        }
        for (child in scene.children) {
            child.acceptVisitor(this)
        }
        parentStack.removeAt(parentStack.lastIndex)
        traverseScenePostorder(scene)
    }

    protected fun getParent() : ModelElement {
        return parentStack[parentStack.lastIndex]
    }

    protected abstract fun traverseProjectPreorder(project: Project)

    protected abstract fun traverseProjectPostorder(project: Project)

    protected abstract fun traverseAggregationPreorder(aggregation: Aggregation)

    protected abstract fun traverseAggregationPostorder(aggregation: Aggregation)

    protected abstract fun traverseAttributePreorder(attribute: Attribute)

    protected abstract fun traverseAttributePostorder(attribute: Attribute)

    protected abstract fun traverseCompartmentPreorder(compartment: Compartment)

    protected abstract fun traverseCompartmentPostorder(compartment: Compartment)

    protected abstract fun traverseClassPreorder(clazz: Class)

    protected abstract fun traverseClassPostorder(clazz: Class)

    protected abstract fun traverseCompositionPreorder(composition: Composition)

    protected abstract fun traverseCompositionPostorder(composition: Composition)

    protected abstract fun traverseConnectionsPreorder(connections: Connections)

    protected abstract fun traverseConnectionsPostorder(connections: Connections)

    protected abstract fun traverseCreateRelationshipPreorder(createRelationship: CreateRelationship)

    protected abstract fun traverseCreateRelationshipPostorder(createRelationship: CreateRelationship)

    protected abstract fun traverseDestroyRelationshipPreorder(destroyRelationship: DestroyRelationship)

    protected abstract fun traverseDestroyRelationshipPostorder(destroyRelationship: DestroyRelationship)

    protected abstract fun traverseEventPreorder(event: Event)

    protected abstract fun traverseEventPostorder(event: Event)

    protected abstract fun traverseEventTypePreorder(eventType: EventType)

    protected abstract fun traverseEventTypePostorder(eventType: EventType)

    protected abstract fun traverseFulfillmentPreorder(fulfillment: Fulfillment)

    protected abstract fun traverseFulfillmentPostorder(fulfillment: Fulfillment)

    protected abstract fun traverseInheritancePreorder(inheritance: Inheritance)

    protected abstract fun traverseInheritancePostorder(inheritance: Inheritance)

    protected abstract fun traverseMetadataPreorder(metadata: Metadata)

    protected abstract fun traverseMetadataPostorder(metadata: Metadata)

    protected abstract fun traverseModelElementMetadataPreorder(modelElementMetadata: ModelElementMetadata)

    protected abstract fun traverseModelElementMetadataPostorder(modelElementMetadata: ModelElementMetadata)

    protected abstract fun traverseMethodPreorder(method: Method)

    protected abstract fun traverseMethodPostorder(method: Method)

    protected abstract fun traversePackagePreorder(packageObj: Package)

    protected abstract fun traversePackagePostorder(packageObj: Package)

    protected abstract fun traverseParameterPreorder(parameter: Parameter)

    protected abstract fun traverseParameterPostorder(parameter: Parameter)

    protected abstract fun traverseRelationshipPreorder(relationship: Relationship)

    protected abstract fun traverseRelationshipPostorder(relationship: Relationship)

    protected abstract fun traverseReturnEventPreorder(returnEvent: ReturnEvent)

    protected abstract fun traverseReturnEventPostorder(returnEvent: ReturnEvent)

    protected abstract fun traverseRoleTypePreorder(roleType: RoleType)

    protected abstract fun traverseRoleTypePostorder(roleType: RoleType)

    protected abstract fun traverseScenePreorder(scene: Scene)

    protected abstract fun traverseScenePostorder(scene: Scene)

}