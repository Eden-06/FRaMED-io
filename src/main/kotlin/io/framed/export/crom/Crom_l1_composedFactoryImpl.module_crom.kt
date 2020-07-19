@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

import kotlin.js.*
import kotlin.js.Json
import org.khronos.webgl.*
import org.w3c.dom.*
import org.w3c.dom.events.*
import org.w3c.dom.parsing.*
import org.w3c.dom.svg.*
import org.w3c.dom.url.*
import org.w3c.fetch.*
import org.w3c.files.*
import org.w3c.notifications.*
import org.w3c.performance.*
import org.w3c.workers.*
import org.w3c.xhr.*

open external class Crom_l1_composedFactoryImpl : EFactoryImpl, Crom_l1_composedFactory {
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
    open fun create(eClass: EClass): EObject
    open fun createFromString(eDataType: EDataType, initialValue: String): Any
    open fun convertToString(eDataType: EDataType, instanceValue: Any): String
    open fun createDirectionFromString(eDataType: EDataType, initialValue: String): Direction
    open fun convertDirectionToString(eDataType: EDataType, instanceValue: Any): String
    open fun createParthoodFromString(eDataType: EDataType, initialValue: String): Parthood
    open fun convertParthoodToString(eDataType: EDataType, instanceValue: Any): String

    companion object {
        var eINSTANCE: Any
        fun init(): Crom_l1_composedFactory
    }
}