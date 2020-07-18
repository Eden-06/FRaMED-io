package io.framed.export

import io.framed.Project
import io.framed.model.*
import io.framed.visitor.ProjectTreeVisitor

/**
 * Implementation of a ProjectTreeVisitor that exports the project to XMI in CROM (Compartment Role Object Model).
 */
class XmiExporter : ProjectTreeVisitor<String>("") {
    
    override fun traverseProjectPreorder(project: Project) {
    }

    override fun traverseProjectPostorder(project: Project) {
    }

    override fun traverseAggregationPreorder(aggregation: Aggregation) {
        output += "<aggregation>" + aggregation.name + "</aggregation>\n"
    }

    override fun traverseAggregationPostorder(aggregation: Aggregation) {
        return
    }

    override fun traverseAttributePreorder(attribute: Attribute) {
        output += "<attribute>" + attribute.name + "</attribute>\n"
    }

    override fun traverseAttributePostorder(attribute: Attribute) {
        return
    }

    override fun traverseCompartmentPreorder(compartment: Compartment) {
        output += "<compartment name=\"" + compartment.name + "\">\n"
    }

    override fun traverseCompartmentPostorder(compartment: Compartment) {
        output += "</compartment>\n"
    }

    override fun traverseClassPreorder(clazz: Class) {
        output += "<class name=\"" + clazz.name + "\">\n"
    }

    override fun traverseClassPostorder(clazz: Class) {
        output += "</class>\n"
    }

    override fun traverseCompositionPreorder(composition: Composition) {
        output += "<composition>" + composition.name + "</composition>\n"
    }

    override fun traverseCompositionPostorder(composition: Composition) {
        return
    }

    override fun traverseConnectionsPreorder(connections: Connections) {
        output += "<connections>\n"
    }

    override fun traverseConnectionsPostorder(connections: Connections) {
        output += "</connections>\n"
    }

    override fun traverseCreateRelationshipPreorder(createRelationship: CreateRelationship) {
        output += "<createRelationship>" + createRelationship.name + "</createRelationship>\n"
    }

    override fun traverseCreateRelationshipPostorder(createRelationship: CreateRelationship) {
        return
    }

    override fun traverseDestroyRelationshipPreorder(destroyRelationship: DestroyRelationship) {
        output += "<destroyRelationship>" + destroyRelationship.name + "</destroyRelationship>\n"
    }

    override fun traverseDestroyRelationshipPostorder(destroyRelationship: DestroyRelationship) {
        return
    }

    override fun traverseEventPreorder(event: Event) {
        output += "<event>" + event.desc + "</event>"
    }

    override fun traverseEventPostorder(event: Event) {
        return
    }

    override fun traverseEventTypePreorder(eventType: EventType) {
        return
    }

    override fun traverseEventTypePostorder(eventType: EventType) {
        return
    }

    override fun traverseFulfillmentPreorder(fulfillment: Fulfillment) {
        output += "<fulfillment>" + fulfillment.name + "</fulfillment>\n"
    }

    override fun traverseFulfillmentPostorder(fulfillment: Fulfillment) {
        return
    }

    override fun traverseInheritancePreorder(inheritance: Inheritance) {
        output += "<inheritance>" + inheritance.name + "</inheritance>\n"
    }

    override fun traverseInheritancePostorder(inheritance: Inheritance) {
        return
    }

    override fun traverseMetadataPreorder(metadata: Metadata) {
        return
    }

    override fun traverseMetadataPostorder(metadata: Metadata) {
        return
    }

    override fun traverseModelElementMetadataPreorder(modelElementMetadata: ModelElementMetadata) {
        return
    }

    override fun traverseModelElementMetadataPostorder(modelElementMetadata: ModelElementMetadata) {
        return
    }

    override fun traverseMethodPreorder(method: Method) {
        output += "<method>\n"
    }

    override fun traverseMethodPostorder(method: Method) {
        output += "</method>\n"
    }

    override fun traversePackagePreorder(packageObj: Package) {
        output += "<package name=\"" + packageObj.name + "\">\n"
    }

    override fun traversePackagePostorder(packageObj: Package) {
        output += "</package>\n"
    }

    override fun traverseParameterPreorder(parameter: Parameter) {
        output += "<parameter>" + parameter.name + "</parameter>\n"
    }

    override fun traverseParameterPostorder(parameter: Parameter) {
        return
    }

    override fun traverseRelationshipPreorder(relationship: Relationship) {
        output += "<relationship>" + relationship.name + "</relationship>\n"
    }

    override fun traverseRelationshipPostorder(relationship: Relationship) {
        return
    }

    override fun traverseReturnEventPreorder(returnEvent: ReturnEvent) {
        output += "<returnEvent>" + returnEvent.desc + "</returnEvent>\n"
    }

    override fun traverseReturnEventPostorder(returnEvent: ReturnEvent) {
        return
    }

    override fun traverseRoleTypePreorder(roleType: RoleType) {
        output += "<roleType name=\"" + roleType.name + "\">\n"
    }

    override fun traverseRoleTypePostorder(roleType: RoleType) {
        output += "</roleType>\n"
    }

    override fun traverseScenePreorder(scene: Scene) {
        output += "<scene name=\"" + scene.name + "\">\n"
    }

    override fun traverseScenePostorder(scene: Scene) {
        output += "</scene>\n"
    }
}