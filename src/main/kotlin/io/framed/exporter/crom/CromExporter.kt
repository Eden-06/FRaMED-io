package io.framed.exporter.crom

import io.framed.Project
import io.framed.exporter.crom.crom.ModelElement
import io.framed.exporter.visitor.ProjectTreeVisitor
import io.framed.model.*

/**
 * Implementation of a ProjectTreeVisitor that exports the project to CROM (Compartment Role Object Model).
 */
class CromExporter : ProjectTreeVisitor<String>("") {

    /**
     * Mapping of Framed.io element ID to CROM ModelElement.
     * The lookup table is e.g. used for finding CROM ModelElements matching Framed.io IDs in relationships.
     */
    private var elementLookupTable = HashMap<Long, ModelElement>()

    override fun traverseProjectPreorder(project: Project) {
    }

    override fun traverseProjectPostorder(project: Project) {
    }

    override fun traverseAggregationPreorder(aggregation: Aggregation) {
        result += "<aggregation>" + aggregation.name + "</aggregation>\n"
    }

    override fun traverseAggregationPostorder(aggregation: Aggregation) {
        return
    }

    override fun traverseAttributePreorder(attribute: Attribute) {
        result += "<attribute>" + attribute.name + "</attribute>\n"
    }

    override fun traverseAttributePostorder(attribute: Attribute) {
        return
    }

    override fun traverseCompartmentPreorder(compartment: Compartment) {
        result += "<compartment name=\"" + compartment.name + "\">\n"
    }

    override fun traverseCompartmentPostorder(compartment: Compartment) {
        result += "</compartment>\n"
    }

    override fun traverseClassPreorder(clazz: Class) {
        result += "<class name=\"" + clazz.name + "\">\n"
    }

    override fun traverseClassPostorder(clazz: Class) {
        result += "</class>\n"
    }

    override fun traverseCompositionPreorder(composition: Composition) {
        result += "<composition>" + composition.name + "</composition>\n"
    }

    override fun traverseCompositionPostorder(composition: Composition) {
        return
    }

    override fun traverseConnectionsPreorder(connections: Connections) {
        result += "<connections>\n"
    }

    override fun traverseConnectionsPostorder(connections: Connections) {
        result += "</connections>\n"
    }

    override fun traverseCreateRelationshipPreorder(createRelationship: CreateRelationship) {
        result += "<createRelationship>" + createRelationship.name + "</createRelationship>\n"
    }

    override fun traverseCreateRelationshipPostorder(createRelationship: CreateRelationship) {
        return
    }

    override fun traverseDestroyRelationshipPreorder(destroyRelationship: DestroyRelationship) {
        result += "<destroyRelationship>" + destroyRelationship.name + "</destroyRelationship>\n"
    }

    override fun traverseDestroyRelationshipPostorder(destroyRelationship: DestroyRelationship) {
        return
    }

    override fun traverseEventPreorder(event: Event) {
        result += "<event>" + event.desc + "</event>"
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
        result += "<fulfillment>" + fulfillment.name + "</fulfillment>\n"
    }

    override fun traverseFulfillmentPostorder(fulfillment: Fulfillment) {
        return
    }

    override fun traverseInheritancePreorder(inheritance: Inheritance) {
        result += "<inheritance>" + inheritance.name + "</inheritance>\n"
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
        result += "<method>\n"
    }

    override fun traverseMethodPostorder(method: Method) {
        result += "</method>\n"
    }

    override fun traversePackagePreorder(packageObj: Package) {
        result += "<package name=\"" + packageObj.name + "\">\n"
    }

    override fun traversePackagePostorder(packageObj: Package) {
        result += "</package>\n"
    }

    override fun traverseParameterPreorder(parameter: Parameter) {
        result += "<parameter>" + parameter.name + "</parameter>\n"
    }

    override fun traverseParameterPostorder(parameter: Parameter) {
        return
    }

    override fun traverseRelationshipPreorder(relationship: Relationship) {
        result += "<relationship>" + relationship.name + "</relationship>\n"
    }

    override fun traverseRelationshipPostorder(relationship: Relationship) {
        return
    }

    override fun traverseReturnEventPreorder(returnEvent: ReturnEvent) {
        result += "<returnEvent>" + returnEvent.desc + "</returnEvent>\n"
    }

    override fun traverseReturnEventPostorder(returnEvent: ReturnEvent) {
        return
    }

    override fun traverseRoleTypePreorder(roleType: RoleType) {
        result += "<roleType name=\"" + roleType.name + "\">\n"
    }

    override fun traverseRoleTypePostorder(roleType: RoleType) {
        result += "</roleType>\n"
    }

    override fun traverseScenePreorder(scene: Scene) {
        result += "<scene name=\"" + scene.name + "\">\n"
    }

    override fun traverseScenePostorder(scene: Scene) {
        result += "</scene>\n"
    }
}