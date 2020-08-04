@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package io.framed.exporter.crom.crom

import io.framed.exporter.crom.ecore.EAttribute
import io.framed.exporter.crom.ecore.EClass
import io.framed.exporter.crom.ecore.EEnum
import io.framed.exporter.crom.ecore.EPackage
import io.framed.exporter.crom.ecore.EReference

external interface Crom_l1_composedPackage : EPackage {
    fun getNamedElement(): EClass
    fun getNamedElement_Name(): EAttribute
    fun getModelElement(): EClass
    fun getModel(): EClass
    fun getModel_Elements(): EReference
    fun getModel_Relations(): EReference
    fun getRelationTarget(): EClass
    fun getRelationTarget_Incoming(): EReference
    fun getRelationTarget_Outgoing(): EReference
    fun getType(): EClass
    fun getType_Attributes(): EReference
    fun getType_Operations(): EReference
    fun getRigidType(): EClass
    fun getGroup(): EClass
    fun getRelation(): EClass
    fun getTypedElement(): EClass
    fun getTypedElement_Type(): EReference
    fun getParameter(): EClass
    fun getOperation(): EClass
    fun getOperation_Params(): EReference
    fun getOperation_Owner(): EReference
    fun getOperation_Operation(): EAttribute
    fun getAttribute(): EClass
    fun getAttribute_Owner(): EReference
    fun getDataType(): EClass
    fun getDataType_Tr_extends(): EReference
    fun getDataType_Serializable(): EAttribute
    fun getNaturalType(): EClass
    fun getNaturalType_Tr_extends(): EReference
    fun getCompartmentType(): EClass
    fun getCompartmentType_Parts(): EReference
    fun getCompartmentType_Relationships(): EReference
    fun getCompartmentType_Constraints(): EReference
    fun getCompartmentType_Tr_extends(): EReference
    fun getCompartmentType_Contains(): EReference
    fun getCompartmentType_Fulfillments(): EReference
    fun getAntiRigidType(): EClass
    fun getRoleGroupElement(): EClass
    fun getAbstractRole(): EClass
    fun getRoleType(): EClass
    fun getRoleType_Tr_extends(): EReference
    fun getRelationship(): EClass
    fun getRelationship_First(): EReference
    fun getRelationship_Second(): EReference
    fun getRelationship_Tr_constraints(): EReference
    fun getRelationship_Direction(): EAttribute
    fun getFulfillment(): EClass
    fun getFulfillment_Filled(): EReference
    fun getFulfillment_Filler(): EReference
    fun getInheritance(): EClass
    fun getConstraint(): EClass
    fun getRoleConstraint(): EClass
    fun getRoleConstraint_First(): EReference
    fun getRoleConstraint_Second(): EReference
    fun getRelationshipConstraint(): EClass
    fun getIntraRelationshipConstraint(): EClass
    fun getIntraRelationshipConstraint_Relation(): EReference
    fun getInterRelationshipConstraint(): EClass
    fun getInterRelationshipConstraint_First(): EReference
    fun getInterRelationshipConstraint_Second(): EReference
    fun getComplexConstraint(): EClass
    fun getComplexConstraint_Targets(): EReference
    fun getComplexConstraint_Expression(): EAttribute
    fun getDataInheritance(): EClass
    fun getDataInheritance_Super(): EReference
    fun getDataInheritance_Sub(): EReference
    fun getNaturalInheritance(): EClass
    fun getNaturalInheritance_Super(): EReference
    fun getNaturalInheritance_Sub(): EReference
    fun getCompartmentInheritance(): EClass
    fun getCompartmentInheritance_Super(): EReference
    fun getCompartmentInheritance_Sub(): EReference
    fun getRoleInheritance(): EClass
    fun getRoleInheritance_Super(): EReference
    fun getRoleInheritance_Sub(): EReference
    fun getPlace(): EClass
    fun getPlace_Holder(): EReference
    fun getPlace_Lower(): EAttribute
    fun getPlace_Upper(): EAttribute
    fun getRelationshipImplication(): EClass
    fun getRelationshipExclusion(): EClass
    fun getIrreflexive(): EClass
    fun getCyclic(): EClass
    fun getTotal(): EClass
    fun getAcyclic(): EClass
    fun getReflexive(): EClass
    fun getRoleGroup(): EClass
    fun getRoleGroup_Elements(): EReference
    fun getRoleGroup_Lower(): EAttribute
    fun getRoleGroup_Upper(): EAttribute
    fun getRoleImplication(): EClass
    fun getRoleEquivalence(): EClass
    fun getRoleProhibition(): EClass
    fun getPart(): EClass
    fun getPart_Whole(): EReference
    fun getPart_Role(): EReference
    fun getPart_Lower(): EAttribute
    fun getPart_Upper(): EAttribute
    fun getParthoodConstraint(): EClass
    fun getParthoodConstraint_Kind(): EAttribute
    fun getAbstractRoleRef(): EClass
    fun getAbstractRoleRef_Ref(): EReference
    fun getDirection(): EEnum
    fun getParthood(): EEnum
}