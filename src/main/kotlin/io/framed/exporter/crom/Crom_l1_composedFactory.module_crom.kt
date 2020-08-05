@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package io.framed.exporter.crom

import io.framed.exporter.ecore.EFactory

external interface Crom_l1_composedFactory : EFactory {
    fun createModel(): Model
    fun createRigidType(): RigidType
    fun createGroup(): Group
    fun createRelation(): Relation
    fun createParameter(): Parameter
    fun createOperation(): Operation
    fun createAttribute(): Attribute
    fun createType(): Type
    fun createDataType(): DataType
    fun createNaturalType(): NaturalType
    fun createCompartmentType(): CompartmentType
    fun createAntiRigidType(): AntiRigidType
    fun createRoleType(): RoleType
    fun createRelationship(): Relationship
    fun createFulfillment(): Fulfillment
    fun createInheritance(): Inheritance
    fun createConstraint(): Constraint
    fun createRoleConstraint(): RoleConstraint
    fun createRelationshipConstraint(): RelationshipConstraint
    fun createIntraRelationshipConstraint(): IntraRelationshipConstraint
    fun createInterRelationshipConstraint(): InterRelationshipConstraint
    fun createComplexConstraint(): ComplexConstraint
    fun createDataInheritance(): DataInheritance
    fun createNaturalInheritance(): NaturalInheritance
    fun createCompartmentInheritance(): CompartmentInheritance
    fun createRoleInheritance(): RoleInheritance
    fun createPlace(): Place
    fun createRelationshipImplication(): RelationshipImplication
    fun createRelationshipExclusion(): RelationshipExclusion
    fun createRelationTarget(): RelationTarget
    fun createIrreflexive(): Irreflexive
    fun createCyclic(): Cyclic
    fun createTotal(): Total
    fun createAcyclic(): Acyclic
    fun createReflexive(): Reflexive
    fun createRoleGroup(): RoleGroup
    fun createRoleImplication(): RoleImplication
    fun createRoleEquivalence(): RoleEquivalence
    fun createRoleProhibition(): RoleProhibition
    fun createPart(): Part
    fun createTypedElement(): TypedElement
    fun createParthoodConstraint(): ParthoodConstraint
    fun createAbstractRoleRef(): AbstractRoleRef
}