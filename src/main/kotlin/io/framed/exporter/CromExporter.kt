package io.framed.exporter

import io.framed.Project
import io.framed.exporter.crom.*
import io.framed.exporter.ecore.InternalEObject
import io.framed.exporter.visitor.ProjectTreeVisitor
import io.framed.model.*
import io.framed.model.Attribute
import io.framed.model.Fulfillment
import io.framed.model.Inheritance
import io.framed.model.Parameter
import io.framed.model.Relationship
import io.framed.model.RoleType

/**
 * Implementation of a ProjectTreeVisitor that exports the project to CROM (Compartment Role Object Model).
 */
@Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")
class CromExporter private constructor() : ProjectTreeVisitor<Model>(
    Crom_l1_composedFactoryImpl.Crom_l1_composedFactoryImpl().createModel()
) {


    /**
     * Mapping of Framed.io element ID to CROM ModelElement.
     * The lookup table is e.g. used for finding CROM ModelElements matching Framed.io IDs in relationships.
     */
    private var elementLookupTable: MutableMap<Long, InternalEObject> = HashMap()

    /**
     * Mapping of type name to CROM type element.
     * The lookup table is e.g. used for finding CROM Types from FRaMED.io type names.
     */
    private var typeLookupTable: MutableMap<String, RigidType> = HashMap()

    /**
     * Mapping of type name to CROM type element.
     * The lookup table is e.g. used for finding CROM Types from FRaMED.io type names.
     */
    private var cromElementParentTable: MutableMap<InternalEObject, InternalEObject> = HashMap()

    /**
     * Factory, that creates CROM elements
     */
    private val factory = Crom_l1_composedFactoryImpl.Crom_l1_composedFactoryImpl()

    companion object {
        fun exportToCrom(project: Project): Model {
            val exporter = CromExporter()
            exporter.visit(project)
            return exporter.getResultAndReset()
        }
    }

    override fun traverseProjectBeforeChildren(project: Project) {
        // ignore
        return
    }

    override fun traverseProjectAfterChildren(project: Project) {
        // ignore
        return
    }

    override fun traverseAggregation(aggregation: Aggregation) {
        val cromParent = cromElementParentTable[elementLookupTable[aggregation.sourceId]] as CompartmentType?

        // That's a hack. Better solution: create one elementLookupTables each type (RoleTypes, NaturalTypes, etc.)
        // and save all model elements in one specific table there.
        // This would at least simulate type safety and would get rid of those "as RoleType?" or similar.
        if (cromParent == null) {
            console.warn(
                "It seems that your project contains aggregations between non-RoleTypes. " +
                        "Those are not a feature of CROM and therefore will be omitted during export."
            )
            return
        }

        val cromRelationship = factory.createRelationship()

        cromRelationship.direction =
            factory.Crom_l1_composedFactoryImpl.prototype.createDirectionFromString(null, "FirstToSecond")


        cromRelationship.first = createPlaceForRelationshipEnd(aggregation.sourceId, aggregation.sourceCardinality)
        cromRelationship.second = createPlaceForRelationshipEnd(aggregation.targetId, aggregation.targetCardinality)

        val constraint = factory.createParthoodConstraint()
        constraint.relation = cromRelationship
        constraint.kind = factory.Crom_l1_composedFactoryImpl.prototype.createParthoodFromString(null, "SharablePart")

        elementLookupTable[aggregation.id] = cromRelationship

        cromParent.relationships.add(cromRelationship)
        cromParent.constraints.add(constraint)
    }

    override fun traverseAttribute(attribute: Attribute) {
        val cromAttribute: io.framed.exporter.crom.Attribute = factory.createAttribute()
        cromAttribute.name = attribute.name

        cromAttribute.type = getType(attribute.type)

        val parent = getParent() ?: error("Parent of Attribute ${attribute.id} is not present.")
        val cromParent = elementLookupTable[parent.id] as Type?
            ?: error("Element with id " + parent.id + " cannot be found.")

        js("console.log(cromParent.name)")
        cromAttribute.owner = cromParent
        cromParent.attributes.add(cromAttribute)
    }

    override fun traverseCompartmentBeforeChildren(compartment: Compartment) {
        createAndDeclareCompartmentType(compartment.id, compartment.name)
    }

    private fun createAndDeclareCompartmentType(id: Long, name: String) {
        val cromCompartment = factory.createCompartmentType()
        cromCompartment.name = name

        val parent = getParent()
        if (parent == null) {
            result.elements.add(cromCompartment)
        } else {
            // If the parent has a "contains" attribute, its a CompartmentType, otherwise a Model
            val cromParentCompartment = elementLookupTable[parent.id] as CompartmentType?
            if (cromParentCompartment?.contains != undefined) {
                cromParentCompartment.contains.add(cromCompartment)
            } else {
                val cromParentModel = elementLookupTable[parent.id] as Model?
                cromParentModel?.elements?.add(cromCompartment)
            }
        }

        elementLookupTable[id] = cromCompartment
        declareTypeIfNotPresent(cromCompartment)
    }

    override fun traverseCompartmentAfterChildren(compartment: Compartment) {
        // nothing to do
        return
    }

    override fun traverseClassBeforeChildren(clazz: Class) {
        val naturalType = factory.createNaturalType()
        naturalType.name = clazz.name

        val parent = getParent()
        if (parent != null) {
            val cromParent =
                elementLookupTable[parent.id] as Model? ?: error("Element with id " + parent.id + " cannot be found.")
            cromParent.elements.add(naturalType)
        } else {
            result.elements.add(naturalType)
        }

        elementLookupTable[clazz.id] = naturalType
        declareTypeIfNotPresent(naturalType)
    }

    override fun traverseClassAfterChildren(clazz: Class) {
        // nothing to do
        return
    }

    override fun traverseComposition(composition: Composition) {
        val cromParent = cromElementParentTable[elementLookupTable[composition.sourceId]] as CompartmentType?

        // That's a hack. Better solution: create multiple elementLookupTables for RoleTypes, NaturalTypes, etc. and save them there.
        // This would at least simulate type safety and would get rid of those "as RoleType?" or similar.
        if (cromParent == null) {
            console.warn(
                "It seems that your project contains compositions between non-RoleTypes. " +
                        "Those are not a feature of CROM and therefore will be omitted during export."
            )
            return
        }

        val cromRelationship = factory.createRelationship()

        cromRelationship.direction =
            factory.Crom_l1_composedFactoryImpl.prototype.createDirectionFromString(null, "FirstToSecond")


        cromRelationship.first = createPlaceForRelationshipEnd(composition.sourceId, composition.sourceCardinality)
        cromRelationship.second = createPlaceForRelationshipEnd(composition.targetId, composition.targetCardinality)

        val constraint = factory.createParthoodConstraint()
        constraint.relation = cromRelationship
        constraint.kind = factory.Crom_l1_composedFactoryImpl.prototype.createParthoodFromString(null, "ExclusivePart")

        elementLookupTable[composition.id] = cromRelationship

        cromParent.relationships.add(cromRelationship)
        cromParent.constraints.add(constraint)
    }

    override fun traverseConnectionsBeforeChildren(connections: Connections) {
        // ignore
        return
    }

    override fun traverseConnectionsAfterChildren(connections: Connections) {
        // ignore
        return
    }

    override fun traverseCreateRelationship(createRelationship: CreateRelationship) {
        // ignore
        return
    }

    override fun traverseDestroyRelationship(destroyRelationship: DestroyRelationship) {
        // ignore
        return
    }

    override fun traverseEvent(event: Event) {
        // ignore
        return
    }

    override fun traverseEventType(eventType: EventType) {
        // ignore
        return
    }

    override fun traverseFulfillment(fulfillment: Fulfillment) {
        val cromFulfillment = factory.createFulfillment()

        cromFulfillment.filled = elementLookupTable[fulfillment.sourceId] as io.framed.exporter.crom.RoleType?
            ?: error("Element with id ${fulfillment.id} cannot be found in lookup table.")

        cromFulfillment.filler = elementLookupTable[fulfillment.targetId] as CompartmentType?
            ?: error("Element with id ${fulfillment.id} cannot be found in lookup table.")


        elementLookupTable[fulfillment.id] = cromFulfillment

        result.relations.add(cromFulfillment)
    }

    override fun traverseInheritance(inheritance: Inheritance) {
    }

    override fun traverseMetadata(metadata: Metadata) {
        // ignore
        return
    }

    override fun traverseModelElementMetadata(modelElementMetadata: ModelElementMetadata) {
        // ignore
        return
    }

    override fun traverseMethodBeforeChildren(method: Method) {
        val cromMethod = factory.createOperation()
        cromMethod.name = method.name
        cromMethod.type = getType(method.type)


        val parent = getParent() ?: error("Parent of method ${method.id} is not present.")
        val cromParent = elementLookupTable[parent.id] as Type?
            ?: error("Element with id ${parent.id} cannot be found in lookup table.")
        cromParent.operations.add(cromMethod)

        elementLookupTable[method.id] = cromMethod
    }

    override fun traverseMethodAfterChildren(method: Method) {
        // nothing to do
        return
    }

    override fun traversePackageBeforeChildren(packageObj: Package) {
        val cromGroup = factory.createGroup()
        cromGroup.name = packageObj.name

        val parent = getParent()
        if (parent == null) {
            result.elements.add(cromGroup)
        } else {
            val cromParent = elementLookupTable[parent.id] as Model?
                ?: error("Element with id ${parent.id} cannot be found in lookup table.")
            cromParent.elements.add(cromGroup)
        }

        elementLookupTable[packageObj.id] = cromGroup
    }

    override fun traversePackageAfterChildren(packageObj: Package) {
        // nothing to do
        return
    }

    override fun traverseParameter(parameter: Parameter) {
        val cromParameter = factory.createParameter()
        cromParameter.name = parameter.name
        cromParameter.type = getType(parameter.type)

        val parent = getParent() ?: error("Parent of parameter ${parameter.id} is not present.")
        val cromParent = elementLookupTable[parent.id] as Operation?
            ?: error("Element with id ${parent.id} cannot be found in lookup table.")

        cromParent.params.add(cromParameter)

        elementLookupTable[parameter.id] = cromParameter
    }

    override fun traverseRelationship(relationship: Relationship) {
        val cromRelationship = factory.createRelationship()

        cromRelationship.direction =
            factory.Crom_l1_composedFactoryImpl.prototype.createDirectionFromString(null, "FirstToSecond")


        cromRelationship.first = createPlaceForRelationshipEnd(relationship.sourceId, relationship.sourceCardinality)
        cromRelationship.second = createPlaceForRelationshipEnd(relationship.targetId, relationship.targetCardinality)

        elementLookupTable[relationship.id] = cromRelationship


        val cromParent = cromElementParentTable[elementLookupTable[relationship.sourceId]] as CompartmentType? ?: error(
            "Parent for relationship from ${relationship.sourceId} to ${relationship.targetId} not found."
        )
        cromParent.relationships.add(cromRelationship)
    }

    override fun traverseReturnEvent(returnEvent: ReturnEvent) {
        // ignore
    }

    override fun traverseRoleTypeBeforeChildren(roleType: RoleType) {
        val cromRoleType = factory.createRoleType()
        cromRoleType.name = roleType.name
        val parent =
            getParent() ?: error("Parent of roletype ${roleType.id} is not present.")


        val part = factory.createPart()
        val cromParent = elementLookupTable[parent.id]
            ?: error("Parent of roletype ${roleType.id} should have been initialized, but is not.")
        part.role = cromRoleType

        if (roleType.occurrenceConstraint != "") {
            val lower = getLowerFromCardinalityString(roleType.occurrenceConstraint)
            val upper = getUpperFromCardinalityString(roleType.occurrenceConstraint)

            if (lower != "*") part.lower = lower.toInt()
            if (upper != "*") part.upper = upper.toInt()
        }

        part.whole = cromParent

        cromElementParentTable[cromRoleType] = cromParent
        elementLookupTable[roleType.id] = cromRoleType
    }

    override fun traverseRoleTypeAfterChildren(roleType: RoleType) {
        // nothing to do
        return
    }

    override fun traverseSceneBeforeChildren(scene: Scene) {
        createAndDeclareCompartmentType(scene.id, scene.name)
    }

    override fun traverseSceneAfterChildren(scene: Scene) {
        // nothing to do
        return
    }

    private fun declareTypeIfNotPresent(type: RigidType): Boolean {
        if (typeLookupTable[type.name] == null) {
            typeLookupTable[type.name] = type

            return true
        }
        return false
    }

    /**
     * Returns the type from the table. If the type is not present, it creates a new DataType and attaches it to the root model.
     */
    private fun getType(name: String): RigidType {
        val type = typeLookupTable[name]
        if (type != null) {
            return type
        } else {
            val dataType = factory.createDataType()
            dataType.name = name
            typeLookupTable[name] = dataType
            result.elements.add(dataType)
            return dataType
        }
    }

    private fun createPlaceForRelationshipEnd(id: Long, cardinality: String): Place {
        val second = factory.createPlace()
        second.holder = elementLookupTable[id] as io.framed.exporter.crom.RoleType?
            ?: error("Element with id $id cannot be found in lookup table.")
        val lower = getLowerFromCardinalityString(cardinality)
        val upper = getUpperFromCardinalityString(cardinality)

        if (lower != "*") second.lower = lower.toInt()
        if (upper != "*") second.upper = upper.toInt()
        return second
    }

    /**
     * Gets the lower cardinality as a number from a cardinalities String or *
     */
    private fun getLowerFromCardinalityString(cardinalities: String): String {
        val cardinalitiesArr = cardinalities.trim().split("..")

        if (cardinalitiesArr.size > 2) error("Only 2 cardinalities at maximum are allowed.")
        if (cardinalitiesArr.isEmpty()) error("Cardinalities are required and not set.")

        try {
            cardinalitiesArr[0].toInt()
            return cardinalitiesArr[0]
        } catch (e: NumberFormatException) {
            if (cardinalitiesArr[0] == "*") {
                return "*"
            }
            error("$cardinalitiesArr contains invalid cardinalities.")
        }
    }

    /**
     * Gets the upper cardinality from a cardinalities String or -1 for *-Cardinality
     */
    private fun getUpperFromCardinalityString(cardinalities: String): String {
        val cardinalitiesArr = cardinalities.trim().split("..")

        if (cardinalitiesArr.size > 2) error("Only 2 cardinalities at maximum are allowed.")
        if (cardinalitiesArr.isEmpty()) error("Cardinalities are required and not set.")

        try {
            cardinalitiesArr[cardinalitiesArr.lastIndex].toInt()
            return cardinalitiesArr[cardinalitiesArr.lastIndex]
        } catch (e: NumberFormatException) {
            if (cardinalitiesArr[cardinalitiesArr.lastIndex] == "*") {
                return "*"
            }
            error("$cardinalitiesArr contains invalid cardinalities.")
        }
    }
}