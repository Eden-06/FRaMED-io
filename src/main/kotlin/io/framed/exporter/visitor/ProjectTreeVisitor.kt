package io.framed.exporter.visitor

import io.framed.Project
import io.framed.exporter.crom.Crom_l1_composedFactoryImpl
import io.framed.framework.model.ModelElement
import io.framed.model.*

/**
 * Abstract implementation of a Visitor that traverses the project tree (while traversing the "Package" of a project
 * two times in the beginning) and provides an interface to preorder as well as postorder traversal of each element.
 * @param T the type of the result
 * @property initialValue the pre-initialization for the result (e.g. an empty string)
 */
abstract class ProjectTreeVisitor<T>(val initialValue: T) : Visitor {

    /**
     * The result of the traversal
     */
    protected var result: T = initialValue

    /**
     * A stack that contains the parent element of the element that is currently traversed at the top,
     * so that the parent element of a ModelElement can be called in each traversal method.
     */
    private var parentStack: ArrayList<ModelElement> = ArrayList()

    private var typesInitialized: Boolean = false

    /**
     * Returns the result of the traversal and resets the result value to its initial value.
     * @return the result of the traversal
     */
    fun getResultAndReset(): T {
        val finalResult = result
        result = initialValue
        parentStack = ArrayList()
        return finalResult
    }

    override fun visit(project: Project) {
        traverseProjectBeforeChildren(project)
        project.root.acceptVisitor(this)
        for (connection in project.connections.connections) {
            connection.acceptVisitor(this)
        }
        traverseProjectAfterChildren(project)
    }

    override fun visit(aggregation: Aggregation) {
        if (!typesInitialized) return

        traverseAggregationBeforeChildren(aggregation)
        traverseAggregationAfterChildren(aggregation)
    }

    override fun visit(attribute: Attribute) {
        if (!typesInitialized) return

        traverseAttributeBeforeChildren(attribute)
        traverseAttributeAfterChildren(attribute)
    }

    override fun visit(compartment: Compartment) {
        traverseCompartmentBeforeChildren(compartment)
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
        traverseCompartmentAfterChildren(compartment)
    }

    override fun visit(clazz: Class) {
        traverseClassBeforeChildren(clazz)
        parentStack.add(clazz)
        for (attribute in clazz.attributes) {
            attribute.acceptVisitor(this)
        }
        for (method in clazz.methods) {
            method.acceptVisitor(this)
        }
        parentStack.removeAt(parentStack.lastIndex)
        traverseClassAfterChildren(clazz)
    }

    override fun visit(composition: Composition) {
        if (!typesInitialized) return

        traverseCompositionBeforeChildren(composition)
        traverseCompositionAfterChildren(composition)
    }

    override fun visit(connections: Connections) {
        traverseConnectionsBeforeChildren(connections)
        for (connection in connections.connections) {
            connection.acceptVisitor(this)
        }
        traverseConnectionsAfterChildren(connections)
    }

    override fun visit(createRelationship: CreateRelationship) {
        if (!typesInitialized) return

        traverseCreateRelationshipBeforeChildren(createRelationship)
        traverseCreateRelationshipAfterChildren(createRelationship)
    }

    override fun visit(destroyRelationship: DestroyRelationship) {
        if (!typesInitialized) return

        traverseDestroyRelationshipBeforeChildren(destroyRelationship)
        traverseDestroyRelationshipAfterChildren(destroyRelationship)
    }

    override fun visit(event: Event) {
        traverseEventBeforeChildren(event)
        traverseEventAfterChildren(event)
    }

    override fun visit(fulfillment: Fulfillment) {
        traverseFulfillmentBeforeChildren(fulfillment)
        traverseFulfillmentAfterChildren(fulfillment)
    }

    override fun visit(inheritance: Inheritance) {
        traverseInheritanceBeforeChildren(inheritance)
        traverseInheritanceAfterChildren(inheritance)
    }

    override fun visit(method: Method) {
        traverseMethodBeforeChildren(method)
        parentStack.add(method)
        for (parameter in method.parameters) {
            parameter.acceptVisitor(this)
        }
        parentStack.removeAt(parentStack.lastIndex)
        traverseMethodAfterChildren(method)
    }

    override fun visit(packageObj: Package) {
        traversePackageBeforeChildren(packageObj)
        parentStack.add(packageObj)
        for (modelElement in packageObj.children) {
            modelElement.acceptVisitor(this)
        }
        // Double traversal because Types have to be detected beforehand
        for (modelElement in packageObj.children) {
            modelElement.acceptVisitor(this)
        }
        parentStack.removeAt(parentStack.lastIndex)
        traversePackageAfterChildren(packageObj)
    }

    override fun visit(parameter: Parameter) {
        traverseParameterBeforeChildren(parameter)
        traverseParameterAfterChildren(parameter)
    }

    override fun visit(relationship: Relationship) {
        traverseRelationshipBeforeChildren(relationship)
        traverseRelationshipAfterChildren(relationship)
    }

    override fun visit(returnEvent: ReturnEvent) {
        traverseReturnEventBeforeChildren(returnEvent)
        traverseReturnEventAfterChildren(returnEvent)
    }

    override fun visit(roleType: RoleType) {
        traverseRoleTypeBeforeChildren(roleType)
        parentStack.add(roleType)
        for (method in roleType.methods) {
            method.acceptVisitor(this)
        }
        for (attribute in roleType.attributes) {
            attribute.acceptVisitor(this)
        }
        parentStack.removeAt(parentStack.lastIndex)
        traverseRoleTypeAfterChildren(roleType)
    }

    override fun visit(scene: Scene) {
        traverseSceneBeforeChildren(scene)
        parentStack.add(scene)
        for (attribute in scene.attributes) {
            attribute.acceptVisitor(this)
        }
        for (child in scene.children) {
            child.acceptVisitor(this)
        }
        parentStack.removeAt(parentStack.lastIndex)
        traverseSceneAfterChildren(scene)
    }

    /**
     * Returns the parent of the currently traversed element (which is the top element of the stack)
     * @return the parent of the currently traversed element
     */
    protected fun getParent(): ModelElement {
        return parentStack[parentStack.lastIndex]
    }

    protected abstract fun traverseProjectBeforeChildren(project: Project)

    protected abstract fun traverseProjectAfterChildren(project: Project)

    protected abstract fun traverseAggregationBeforeChildren(aggregation: Aggregation)

    protected abstract fun traverseAggregationAfterChildren(aggregation: Aggregation)

    protected abstract fun traverseAttributeBeforeChildren(attribute: Attribute)

    protected abstract fun traverseAttributeAfterChildren(attribute: Attribute)

    protected abstract fun traverseCompartmentBeforeChildren(compartment: Compartment)

    protected abstract fun traverseCompartmentAfterChildren(compartment: Compartment)

    protected abstract fun traverseClassBeforeChildren(clazz: Class)

    protected abstract fun traverseClassAfterChildren(clazz: Class)

    protected abstract fun traverseCompositionBeforeChildren(composition: Composition)

    protected abstract fun traverseCompositionAfterChildren(composition: Composition)

    protected abstract fun traverseConnectionsBeforeChildren(connections: Connections)

    protected abstract fun traverseConnectionsAfterChildren(connections: Connections)

    protected abstract fun traverseCreateRelationshipBeforeChildren(createRelationship: CreateRelationship)

    protected abstract fun traverseCreateRelationshipAfterChildren(createRelationship: CreateRelationship)

    protected abstract fun traverseDestroyRelationshipBeforeChildren(destroyRelationship: DestroyRelationship)

    protected abstract fun traverseDestroyRelationshipAfterChildren(destroyRelationship: DestroyRelationship)

    protected abstract fun traverseEventBeforeChildren(event: Event)

    protected abstract fun traverseEventAfterChildren(event: Event)

    protected abstract fun traverseEventTypeBeforeChildren(eventType: EventType)

    protected abstract fun traverseEventTypeAfterChildren(eventType: EventType)

    protected abstract fun traverseFulfillmentBeforeChildren(fulfillment: Fulfillment)

    protected abstract fun traverseFulfillmentAfterChildren(fulfillment: Fulfillment)

    protected abstract fun traverseInheritanceBeforeChildren(inheritance: Inheritance)

    protected abstract fun traverseInheritanceAfterChildren(inheritance: Inheritance)

    protected abstract fun traverseMetadataBeforeChildren(metadata: Metadata)

    protected abstract fun traverseMetadataAfterChildren(metadata: Metadata)

    protected abstract fun traverseModelElementMetadataBeforeChildren(modelElementMetadata: ModelElementMetadata)

    protected abstract fun traverseModelElementMetadataAfterChildren(modelElementMetadata: ModelElementMetadata)

    protected abstract fun traverseMethodBeforeChildren(method: Method)

    protected abstract fun traverseMethodAfterChildren(method: Method)

    protected abstract fun traversePackageBeforeChildren(packageObj: Package)

    protected abstract fun traversePackageAfterChildren(packageObj: Package)

    protected abstract fun traverseParameterBeforeChildren(parameter: Parameter)

    protected abstract fun traverseParameterAfterChildren(parameter: Parameter)

    protected abstract fun traverseRelationshipBeforeChildren(relationship: Relationship)

    protected abstract fun traverseRelationshipAfterChildren(relationship: Relationship)

    protected abstract fun traverseReturnEventBeforeChildren(returnEvent: ReturnEvent)

    protected abstract fun traverseReturnEventAfterChildren(returnEvent: ReturnEvent)

    protected abstract fun traverseRoleTypeBeforeChildren(roleType: RoleType)

    protected abstract fun traverseRoleTypeAfterChildren(roleType: RoleType)

    protected abstract fun traverseSceneBeforeChildren(scene: Scene)

    protected abstract fun traverseSceneAfterChildren(scene: Scene)

}