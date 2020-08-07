package io.framed.exporter

import io.framed.Project
import io.framed.exporter.crom.Crom_l1_composedFactoryImpl
import io.framed.exporter.crom.ModelElement
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

    /**
     * Factory, that creates CROM elements
     */
    private val factory = Crom_l1_composedFactoryImpl.Crom_l1_composedFactoryImpl()

    override fun traverseProjectBeforeChildren(project: Project) {
    }

    override fun traverseProjectAfterChildren(project: Project) {
    }

    override fun traverseAggregation(aggregation: Aggregation) {
        result += "<aggregation>" + aggregation.name + "</aggregation>\n"
    }

    override fun traverseAttribute(attribute: Attribute) {
        result += "<attribute>" + attribute.name + "</attribute>\n"
    }

    override fun traverseCompartmentBeforeChildren(compartment: Compartment) {
        result += "<compartment name=\"" + compartment.name + "\">\n"
    }

    override fun traverseCompartmentAfterChildren(compartment: Compartment) {
        result += "</compartment>\n"
    }

    override fun traverseClassBeforeChildren(clazz: Class) {
        result += "<class name=\"" + clazz.name + "\">\n"
    }

    override fun traverseClassAfterChildren(clazz: Class) {
        result += "</class>\n"
    }

    override fun traverseComposition(composition: Composition) {
        result += "<composition>" + composition.name + "</composition>\n"
    }

    override fun traverseConnectionsBeforeChildren(connections: Connections) {
        result += "<connections>\n"
    }

    override fun traverseConnectionsAfterChildren(connections: Connections) {
        result += "</connections>\n"
    }

    override fun traverseCreateRelationship(createRelationship: CreateRelationship) {
        result += "<createRelationship>" + createRelationship.name + "</createRelationship>\n"
    }

    override fun traverseDestroyRelationship(destroyRelationship: DestroyRelationship) {
        result += "<destroyRelationship>" + destroyRelationship.name + "</destroyRelationship>\n"
    }

    override fun traverseEvent(event: Event) {
        result += "<event>" + event.desc + "</event>"
    }

    override fun traverseEventType(eventType: EventType) {
        return
    }

    override fun traverseFulfillment(fulfillment: Fulfillment) {
        result += "<fulfillment>" + fulfillment.name + "</fulfillment>\n"
    }

    override fun traverseInheritance(inheritance: Inheritance) {
        result += "<inheritance>" + inheritance.name + "</inheritance>\n"
    }

    override fun traverseMetadata(metadata: Metadata) {
        return
    }

    override fun traverseModelElementMetadata(modelElementMetadata: ModelElementMetadata) {
        return
    }

    override fun traverseMethodBeforeChildren(method: Method) {
        result += "<method>\n"
    }

    override fun traverseMethodAfterChildren(method: Method) {
        result += "</method>\n"
    }

    override fun traversePackageBeforeChildren(packageObj: Package) {
        result += "<package name=\"" + packageObj.name + "\">\n"
    }

    override fun traversePackageAfterChildren(packageObj: Package) {
        result += "</package>\n"
    }

    override fun traverseParameter(parameter: Parameter) {
        result += "<parameter>" + parameter.name + "</parameter>\n"
    }

    override fun traverseRelationship(relationship: Relationship) {
        result += "<relationship>" + relationship.name + "</relationship>\n"
    }

    override fun traverseReturnEvent(returnEvent: ReturnEvent) {
        result += "<returnEvent>" + returnEvent.desc + "</returnEvent>\n"
    }

    override fun traverseRoleTypeBeforeChildren(roleType: RoleType) {
        result += "<roleType name=\"" + roleType.name + "\">\n"
    }

    override fun traverseRoleTypeAfterChildren(roleType: RoleType) {
        result += "</roleType>\n"
    }

    override fun traverseSceneBeforeChildren(scene: Scene) {
        result += "<scene name=\"" + scene.name + "\">\n"
    }

    override fun traverseSceneAfterChildren(scene: Scene) {
        result += "</scene>\n"
    }
}