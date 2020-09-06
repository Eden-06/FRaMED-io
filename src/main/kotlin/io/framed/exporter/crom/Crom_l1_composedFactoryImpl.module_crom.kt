@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package io.framed.exporter.crom

import io.framed.exporter.ecore.EDataType
import io.framed.exporter.ecore.EFactoryImpl

@JsModule("crom")
@JsNonModule
@JsName("crom")
abstract external class Crom_l1_composedFactoryImpl : EFactoryImpl, Crom_l1_composedFactory {

    open var createModel: () -> Model
    open var createRigidType: () -> RigidType
    open var createGroup: () -> Group
    open var createRelation: () -> Relation
    open var createParameter: () -> Parameter
    open var createOperation: () -> Operation
    open var createAttribute: () -> Attribute
    open var createType: () -> Type
    open var createDataType: () -> DataType
    open var createNaturalType: () -> NaturalType
    open var createCompartmentType: () -> CompartmentType
    open var createAntiRigidType: () -> AntiRigidType
    open var createRoleType: () -> RoleType
    open var createRelationship: () -> Relationship
    open var createFulfillment: () -> Fulfillment
    open var createInheritance: () -> Inheritance
    open var createConstraint: () -> Constraint
    open var createRoleConstraint: () -> RoleConstraint
    open var createRelationshipConstraint: () -> RelationshipConstraint
    open var createIntraRelationshipConstraint: () -> IntraRelationshipConstraint
    open var createInterRelationshipConstraint: () -> InterRelationshipConstraint
    open var createComplexConstraint: () -> ComplexConstraint
    open var createDataInheritance: () -> DataInheritance
    open var createNaturalInheritance: () -> NaturalInheritance
    open var createCompartmentInheritance: () -> CompartmentInheritance
    open var createRoleInheritance: () -> RoleInheritance
    open var createPlace: () -> Place
    open var createRelationshipImplication: () -> RelationshipImplication
    open var createRelationshipExclusion: () -> RelationshipExclusion
    open var createRelationTarget: () -> RelationTarget
    open var createIrreflexive: () -> Irreflexive
    open var createCyclic: () -> Cyclic
    open var createTotal: () -> Total
    open var createAcyclic: () -> Acyclic
    open var createReflexive: () -> Reflexive
    open var createRoleGroup: () -> RoleGroup
    open var createRoleImplication: () -> RoleImplication
    open var createRoleEquivalence: () -> RoleEquivalence
    open var createRoleProhibition: () -> RoleProhibition
    open var createPart: () -> Part
    open var createTypedElement: () -> TypedElement
    open var createParthoodConstraint: () -> ParthoodConstraint
    open var createAbstractRoleRef: () -> AbstractRoleRef
    open fun createDirectionFromString(eDataType: EDataType?, initialValue: String): Direction
    open fun convertDirectionToString(eDataType: EDataType?, instanceValue: Any): String
    open fun createParthoodFromString(eDataType: EDataType?, initialValue: String): Parthood
    open fun convertParthoodToString(eDataType: EDataType?, instanceValue: Any): String
    open var Crom_l1_composedFactoryImpl : InternalFactoryImpl

    companion object {
        open fun Crom_l1_composedFactoryImpl(): Crom_l1_composedFactoryImpl
    }
}

abstract external class InternalFactoryImpl {

    open var prototype: EnumConverter
}

abstract external class EnumConverter {

    open fun convertDirectionToString(eDataType: EDataType?, instanceValue: Direction): String
    open fun convertParthoodToString(eDataType: EDataType?, instanceValue: Parthood): String
    open fun createDirectionFromString(eDataType: EDataType?, instanceValue: String): Direction
    open fun createParthoodFromString(eDataType: EDataType?, instanceValue: String): Parthood
}