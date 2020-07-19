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

external open class Crom_l1_composedSwitch<T> : Switch<T> {
    open fun isSwitchFor(ePackage: EPackage): Boolean
    open fun doSwitch(classifierID: Number, theEObject: EObject): T
    open fun caseNamedElement(obj: NamedElement): T
    open fun caseModelElement(obj: ModelElement): T
    open fun caseModel(obj: Model): T
    open fun caseRigidType(obj: RigidType): T
    open fun caseGroup(obj: Group): T
    open fun caseRelation(obj: Relation): T
    open fun caseParameter(obj: Parameter): T
    open fun caseOperation(obj: Operation): T
    open fun caseAttribute(obj: Attribute): T
    open fun caseType(obj: Type): T
    open fun caseDataType(obj: DataType): T
    open fun caseNaturalType(obj: NaturalType): T
    open fun caseCompartmentType(obj: CompartmentType): T
    open fun caseAntiRigidType(obj: AntiRigidType): T
    open fun caseRoleType(obj: RoleType): T
    open fun caseRelationship(obj: Relationship): T
    open fun caseFulfillment(obj: Fulfillment): T
    open fun caseInheritance(obj: Inheritance): T
    open fun caseConstraint(obj: Constraint): T
    open fun caseRoleConstraint(obj: RoleConstraint): T
    open fun caseRelationshipConstraint(obj: RelationshipConstraint): T
    open fun caseIntraRelationshipConstraint(obj: IntraRelationshipConstraint): T
    open fun caseInterRelationshipConstraint(obj: InterRelationshipConstraint): T
    open fun caseComplexConstraint(obj: ComplexConstraint): T
    open fun caseDataInheritance(obj: DataInheritance): T
    open fun caseNaturalInheritance(obj: NaturalInheritance): T
    open fun caseCompartmentInheritance(obj: CompartmentInheritance): T
    open fun caseRoleInheritance(obj: RoleInheritance): T
    open fun casePlace(obj: Place): T
    open fun caseRelationshipImplication(obj: RelationshipImplication): T
    open fun caseRelationshipExclusion(obj: RelationshipExclusion): T
    open fun caseRelationTarget(obj: RelationTarget): T
    open fun caseIrreflexive(obj: Irreflexive): T
    open fun caseCyclic(obj: Cyclic): T
    open fun caseTotal(obj: Total): T
    open fun caseAcyclic(obj: Acyclic): T
    open fun caseReflexive(obj: Reflexive): T
    open fun caseAbstractRole(obj: AbstractRole): T
    open fun caseRoleGroup(obj: RoleGroup): T
    open fun caseRoleImplication(obj: RoleImplication): T
    open fun caseRoleEquivalence(obj: RoleEquivalence): T
    open fun caseRoleProhibition(obj: RoleProhibition): T
    open fun casePart(obj: Part): T
    open fun caseTypedElement(obj: TypedElement): T
    open fun caseParthoodConstraint(obj: ParthoodConstraint): T
    open fun caseRoleGroupElement(obj: RoleGroupElement): T
    open fun caseAbstractRoleRef(obj: AbstractRoleRef): T

    companion object {
        var modelPackage: Any
    }
}