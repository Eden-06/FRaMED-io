@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package io.framed.exporter.crom.crom

import io.framed.exporter.crom.ecore.EAttribute
import io.framed.exporter.crom.ecore.EClass
import io.framed.exporter.crom.ecore.EEnum
import io.framed.exporter.crom.ecore.EOperation
import io.framed.exporter.crom.ecore.EPackageImpl
import io.framed.exporter.crom.ecore.EReference

abstract external class Crom_l1_composedPackageImpl : EPackageImpl, Crom_l1_composedPackage {
    open var isCreated: Any
    open var createPackageContents: () -> Unit
    open var isInitialized: Any
    open var initializePackageContents: () -> Unit
    open var NamedElementEClass: Any
    open var ModelElementEClass: Any
    open var ModelEClass: Any
    open var RigidTypeEClass: Any
    open var GroupEClass: Any
    open var RelationEClass: Any
    open var ParameterEClass: Any
    open var OperationEClass: Any
    open var AttributeEClass: Any
    open var TypeEClass: Any
    open var DataTypeEClass: Any
    open var NaturalTypeEClass: Any
    open var CompartmentTypeEClass: Any
    open var AntiRigidTypeEClass: Any
    open var RoleTypeEClass: Any
    open var RelationshipEClass: Any
    open var FulfillmentEClass: Any
    open var InheritanceEClass: Any
    open var ConstraintEClass: Any
    open var RoleConstraintEClass: Any
    open var RelationshipConstraintEClass: Any
    open var IntraRelationshipConstraintEClass: Any
    open var InterRelationshipConstraintEClass: Any
    open var ComplexConstraintEClass: Any
    open var DataInheritanceEClass: Any
    open var NaturalInheritanceEClass: Any
    open var CompartmentInheritanceEClass: Any
    open var RoleInheritanceEClass: Any
    open var PlaceEClass: Any
    open var RelationshipImplicationEClass: Any
    open var RelationshipExclusionEClass: Any
    open var RelationTargetEClass: Any
    open var IrreflexiveEClass: Any
    open var CyclicEClass: Any
    open var TotalEClass: Any
    open var AcyclicEClass: Any
    open var ReflexiveEClass: Any
    open var AbstractRoleEClass: Any
    open var RoleGroupEClass: Any
    open var RoleImplicationEClass: Any
    open var RoleEquivalenceEClass: Any
    open var RoleProhibitionEClass: Any
    open var PartEClass: Any
    open var TypedElementEClass: Any
    open var ParthoodConstraintEClass: Any
    open var RoleGroupElementEClass: Any
    open var AbstractRoleRefEClass: Any
    open var DirectionEEnum: Any
    open var ParthoodEEnum: Any
    open var getNamedElement: () -> EClass
    open var getNamedElement_Name: () -> EAttribute
    open var getModelElement: () -> EClass
    open var getModel: () -> EClass
    open var getModel_Elements: () -> EReference
    open var getModel_Relations: () -> EReference
    open var getRelationTarget: () -> EClass
    open var getRelationTarget_Incoming: () -> EReference
    open var getRelationTarget_Outgoing: () -> EReference
    open var getType: () -> EClass
    open var getType_Attributes: () -> EReference
    open var getType_Operations: () -> EReference
    open var getRigidType: () -> EClass
    open var getGroup: () -> EClass
    open var getRelation: () -> EClass
    open var getRelation__GetSource__: () -> EOperation
    open var getRelation__GetTarget__: () -> EOperation
    open var getTypedElement: () -> EClass
    open var getTypedElement_Type: () -> EReference
    open var getParameter: () -> EClass
    open var getOperation: () -> EClass
    open var getOperation_Params: () -> EReference
    open var getOperation_Operation: () -> EAttribute
    open var getOperation_Owner: () -> EReference
    open var getAttribute: () -> EClass
    open var getAttribute_Owner: () -> EReference
    open var getDataType: () -> EClass
    open var getDataType_Serializable: () -> EAttribute
    open var getDataType_Tr_extends: () -> EReference
    open var getNaturalType: () -> EClass
    open var getNaturalType_Tr_extends: () -> EReference
    open var getCompartmentType: () -> EClass
    open var getCompartmentType_Parts: () -> EReference
    open var getCompartmentType_Relationships: () -> EReference
    open var getCompartmentType_Constraints: () -> EReference
    open var getCompartmentType_Tr_extends: () -> EReference
    open var getCompartmentType_Contains: () -> EReference
    open var getCompartmentType_Fulfillments: () -> EReference
    open var getAntiRigidType: () -> EClass
    open var getRoleGroupElement: () -> EClass
    open var getAbstractRole: () -> EClass
    open var getRoleType: () -> EClass
    open var getRoleType_Tr_extends: () -> EReference
    open var getRelationship: () -> EClass
    open var getRelationship_First: () -> EReference
    open var getRelationship_Second: () -> EReference
    open var getRelationship_Direction: () -> EAttribute
    open var getRelationship_Tr_constraints: () -> EReference
    open var getFulfillment: () -> EClass
    open var getFulfillment_Filled: () -> EReference
    open var getFulfillment_Filler: () -> EReference
    open var getInheritance: () -> EClass
    open var getConstraint: () -> EClass
    open var getRoleConstraint: () -> EClass
    open var getRoleConstraint_First: () -> EReference
    open var getRoleConstraint_Second: () -> EReference
    open var getRelationshipConstraint: () -> EClass
    open var getIntraRelationshipConstraint: () -> EClass
    open var getIntraRelationshipConstraint_Relation: () -> EReference
    open var getInterRelationshipConstraint: () -> EClass
    open var getInterRelationshipConstraint_First: () -> EReference
    open var getInterRelationshipConstraint_Second: () -> EReference
    open var getComplexConstraint: () -> EClass
    open var getComplexConstraint_Targets: () -> EReference
    open var getComplexConstraint_Expression: () -> EAttribute
    open var getDataInheritance: () -> EClass
    open var getDataInheritance_Super: () -> EReference
    open var getDataInheritance_Sub: () -> EReference
    open var getNaturalInheritance: () -> EClass
    open var getNaturalInheritance_Super: () -> EReference
    open var getNaturalInheritance_Sub: () -> EReference
    open var getCompartmentInheritance: () -> EClass
    open var getCompartmentInheritance_Super: () -> EReference
    open var getCompartmentInheritance_Sub: () -> EReference
    open var getRoleInheritance: () -> EClass
    open var getRoleInheritance_Super: () -> EReference
    open var getRoleInheritance_Sub: () -> EReference
    open var getPlace: () -> EClass
    open var getPlace_Lower: () -> EAttribute
    open var getPlace_Upper: () -> EAttribute
    open var getPlace_Holder: () -> EReference
    open var getRelationshipImplication: () -> EClass
    open var getRelationshipExclusion: () -> EClass
    open var getIrreflexive: () -> EClass
    open var getCyclic: () -> EClass
    open var getTotal: () -> EClass
    open var getAcyclic: () -> EClass
    open var getReflexive: () -> EClass
    open var getRoleGroup: () -> EClass
    open var getRoleGroup_Lower: () -> EAttribute
    open var getRoleGroup_Upper: () -> EAttribute
    open var getRoleGroup_Elements: () -> EReference
    open var getRoleImplication: () -> EClass
    open var getRoleEquivalence: () -> EClass
    open var getRoleProhibition: () -> EClass
    open var getPart: () -> EClass
    open var getPart_Whole: () -> EReference
    open var getPart_Lower: () -> EAttribute
    open var getPart_Upper: () -> EAttribute
    open var getPart_Role: () -> EReference
    open var getParthoodConstraint: () -> EClass
    open var getParthoodConstraint_Kind: () -> EAttribute
    open var getAbstractRoleRef: () -> EClass
    open var getAbstractRoleRef_Ref: () -> EReference
    open var getDirection: () -> EEnum
    open var getParthood: () -> EEnum

    companion object {
        var eNAME: String
        var eNS_URI: String
        var eNS_PREFIX: String
        fun init(): Crom_l1_composedPackage
        var NAMEDELEMENT: Number
        var NAMEDELEMENT_FEATURE_COUNT: Number
        var NAMEDELEMENT_OPERATION_COUNT: Number
        var NAMED_ELEMENT__NAME: Number
        var MODELELEMENT: Number
        var MODELELEMENT_FEATURE_COUNT: Number
        var MODELELEMENT_OPERATION_COUNT: Number
        var MODEL_ELEMENT__NAME: Number
        var MODEL: Number
        var MODEL_FEATURE_COUNT: Number
        var MODEL_OPERATION_COUNT: Number
        var MODEL__ELEMENTS: Number
        var MODEL__RELATIONS: Number
        var RELATIONTARGET: Number
        var RELATIONTARGET_FEATURE_COUNT: Number
        var RELATIONTARGET_OPERATION_COUNT: Number
        var RELATION_TARGET__NAME: Number
        var RELATION_TARGET__INCOMING: Number
        var RELATION_TARGET__OUTGOING: Number
        var TYPE: Number
        var TYPE_FEATURE_COUNT: Number
        var TYPE_OPERATION_COUNT: Number
        var TYPE__NAME: Number
        var TYPE__INCOMING: Number
        var TYPE__OUTGOING: Number
        var TYPE__ATTRIBUTES: Number
        var TYPE__OPERATIONS: Number
        var RIGIDTYPE: Number
        var RIGIDTYPE_FEATURE_COUNT: Number
        var RIGIDTYPE_OPERATION_COUNT: Number
        var RIGID_TYPE__NAME: Number
        var RIGID_TYPE__INCOMING: Number
        var RIGID_TYPE__OUTGOING: Number
        var RIGID_TYPE__ATTRIBUTES: Number
        var RIGID_TYPE__OPERATIONS: Number
        var GROUP: Number
        var GROUP_FEATURE_COUNT: Number
        var GROUP_OPERATION_COUNT: Number
        var GROUP__NAME: Number
        var GROUP__ELEMENTS: Number
        var GROUP__RELATIONS: Number
        var RELATION: Number
        var RELATION_FEATURE_COUNT: Number
        var RELATION_OPERATION_COUNT: Number
        var RELATION___GETSOURCE: Number
        var RELATION___GETTARGET: Number
        var TYPEDELEMENT: Number
        var TYPEDELEMENT_FEATURE_COUNT: Number
        var TYPEDELEMENT_OPERATION_COUNT: Number
        var TYPED_ELEMENT__NAME: Number
        var TYPED_ELEMENT__TYPE: Number
        var PARAMETER: Number
        var PARAMETER_FEATURE_COUNT: Number
        var PARAMETER_OPERATION_COUNT: Number
        var PARAMETER__NAME: Number
        var PARAMETER__TYPE: Number
        var OPERATION: Number
        var OPERATION_FEATURE_COUNT: Number
        var OPERATION_OPERATION_COUNT: Number
        var OPERATION__NAME: Number
        var OPERATION__TYPE: Number
        var OPERATION__PARAMS: Number
        var OPERATION__OPERATION: Number
        var OPERATION__OWNER: Number
        var ATTRIBUTE: Number
        var ATTRIBUTE_FEATURE_COUNT: Number
        var ATTRIBUTE_OPERATION_COUNT: Number
        var ATTRIBUTE__NAME: Number
        var ATTRIBUTE__TYPE: Number
        var ATTRIBUTE__OWNER: Number
        var DATATYPE: Number
        var DATATYPE_FEATURE_COUNT: Number
        var DATATYPE_OPERATION_COUNT: Number
        var DATA_TYPE__NAME: Number
        var DATA_TYPE__INCOMING: Number
        var DATA_TYPE__OUTGOING: Number
        var DATA_TYPE__ATTRIBUTES: Number
        var DATA_TYPE__OPERATIONS: Number
        var DATA_TYPE__SERIALIZABLE: Number
        var DATA_TYPE__TR_EXTENDS: Number
        var NATURALTYPE: Number
        var NATURALTYPE_FEATURE_COUNT: Number
        var NATURALTYPE_OPERATION_COUNT: Number
        var NATURAL_TYPE__NAME: Number
        var NATURAL_TYPE__INCOMING: Number
        var NATURAL_TYPE__OUTGOING: Number
        var NATURAL_TYPE__ATTRIBUTES: Number
        var NATURAL_TYPE__OPERATIONS: Number
        var NATURAL_TYPE__TR_EXTENDS: Number
        var COMPARTMENTTYPE: Number
        var COMPARTMENTTYPE_FEATURE_COUNT: Number
        var COMPARTMENTTYPE_OPERATION_COUNT: Number
        var COMPARTMENT_TYPE__NAME: Number
        var COMPARTMENT_TYPE__INCOMING: Number
        var COMPARTMENT_TYPE__OUTGOING: Number
        var COMPARTMENT_TYPE__ATTRIBUTES: Number
        var COMPARTMENT_TYPE__OPERATIONS: Number
        var COMPARTMENT_TYPE__PARTS: Number
        var COMPARTMENT_TYPE__RELATIONSHIPS: Number
        var COMPARTMENT_TYPE__CONSTRAINTS: Number
        var COMPARTMENT_TYPE__TR_EXTENDS: Number
        var COMPARTMENT_TYPE__CONTAINS: Number
        var COMPARTMENT_TYPE__FULFILLMENTS: Number
        var ANTIRIGIDTYPE: Number
        var ANTIRIGIDTYPE_FEATURE_COUNT: Number
        var ANTIRIGIDTYPE_OPERATION_COUNT: Number
        var ANTI_RIGID_TYPE__NAME: Number
        var ANTI_RIGID_TYPE__INCOMING: Number
        var ANTI_RIGID_TYPE__OUTGOING: Number
        var ANTI_RIGID_TYPE__ATTRIBUTES: Number
        var ANTI_RIGID_TYPE__OPERATIONS: Number
        var ROLEGROUPELEMENT: Number
        var ROLEGROUPELEMENT_FEATURE_COUNT: Number
        var ROLEGROUPELEMENT_OPERATION_COUNT: Number
        var ABSTRACTROLE: Number
        var ABSTRACTROLE_FEATURE_COUNT: Number
        var ABSTRACTROLE_OPERATION_COUNT: Number
        var ROLETYPE: Number
        var ROLETYPE_FEATURE_COUNT: Number
        var ROLETYPE_OPERATION_COUNT: Number
        var ROLE_TYPE__NAME: Number
        var ROLE_TYPE__INCOMING: Number
        var ROLE_TYPE__OUTGOING: Number
        var ROLE_TYPE__ATTRIBUTES: Number
        var ROLE_TYPE__OPERATIONS: Number
        var ROLE_TYPE__TR_EXTENDS: Number
        var RELATIONSHIP: Number
        var RELATIONSHIP_FEATURE_COUNT: Number
        var RELATIONSHIP_OPERATION_COUNT: Number
        var RELATIONSHIP__NAME: Number
        var RELATIONSHIP__FIRST: Number
        var RELATIONSHIP__SECOND: Number
        var RELATIONSHIP__DIRECTION: Number
        var RELATIONSHIP__TR_CONSTRAINTS: Number
        var FULFILLMENT: Number
        var FULFILLMENT_FEATURE_COUNT: Number
        var FULFILLMENT_OPERATION_COUNT: Number
        var FULFILLMENT__FILLED: Number
        var FULFILLMENT__FILLER: Number
        var INHERITANCE: Number
        var INHERITANCE_FEATURE_COUNT: Number
        var INHERITANCE_OPERATION_COUNT: Number
        var CONSTRAINT: Number
        var CONSTRAINT_FEATURE_COUNT: Number
        var CONSTRAINT_OPERATION_COUNT: Number
        var ROLECONSTRAINT: Number
        var ROLECONSTRAINT_FEATURE_COUNT: Number
        var ROLECONSTRAINT_OPERATION_COUNT: Number
        var ROLE_CONSTRAINT__FIRST: Number
        var ROLE_CONSTRAINT__SECOND: Number
        var RELATIONSHIPCONSTRAINT: Number
        var RELATIONSHIPCONSTRAINT_FEATURE_COUNT: Number
        var RELATIONSHIPCONSTRAINT_OPERATION_COUNT: Number
        var INTRARELATIONSHIPCONSTRAINT: Number
        var INTRARELATIONSHIPCONSTRAINT_FEATURE_COUNT: Number
        var INTRARELATIONSHIPCONSTRAINT_OPERATION_COUNT: Number
        var INTRA_RELATIONSHIP_CONSTRAINT__RELATION: Number
        var INTERRELATIONSHIPCONSTRAINT: Number
        var INTERRELATIONSHIPCONSTRAINT_FEATURE_COUNT: Number
        var INTERRELATIONSHIPCONSTRAINT_OPERATION_COUNT: Number
        var INTER_RELATIONSHIP_CONSTRAINT__FIRST: Number
        var INTER_RELATIONSHIP_CONSTRAINT__SECOND: Number
        var COMPLEXCONSTRAINT: Number
        var COMPLEXCONSTRAINT_FEATURE_COUNT: Number
        var COMPLEXCONSTRAINT_OPERATION_COUNT: Number
        var COMPLEX_CONSTRAINT__TARGETS: Number
        var COMPLEX_CONSTRAINT__EXPRESSION: Number
        var DATAINHERITANCE: Number
        var DATAINHERITANCE_FEATURE_COUNT: Number
        var DATAINHERITANCE_OPERATION_COUNT: Number
        var DATA_INHERITANCE__SUPER: Number
        var DATA_INHERITANCE__SUB: Number
        var NATURALINHERITANCE: Number
        var NATURALINHERITANCE_FEATURE_COUNT: Number
        var NATURALINHERITANCE_OPERATION_COUNT: Number
        var NATURAL_INHERITANCE__SUPER: Number
        var NATURAL_INHERITANCE__SUB: Number
        var COMPARTMENTINHERITANCE: Number
        var COMPARTMENTINHERITANCE_FEATURE_COUNT: Number
        var COMPARTMENTINHERITANCE_OPERATION_COUNT: Number
        var COMPARTMENT_INHERITANCE__SUPER: Number
        var COMPARTMENT_INHERITANCE__SUB: Number
        var ROLEINHERITANCE: Number
        var ROLEINHERITANCE_FEATURE_COUNT: Number
        var ROLEINHERITANCE_OPERATION_COUNT: Number
        var ROLE_INHERITANCE__SUPER: Number
        var ROLE_INHERITANCE__SUB: Number
        var PLACE: Number
        var PLACE_FEATURE_COUNT: Number
        var PLACE_OPERATION_COUNT: Number
        var PLACE__LOWER: Number
        var PLACE__UPPER: Number
        var PLACE__HOLDER: Number
        var RELATIONSHIPIMPLICATION: Number
        var RELATIONSHIPIMPLICATION_FEATURE_COUNT: Number
        var RELATIONSHIPIMPLICATION_OPERATION_COUNT: Number
        var RELATIONSHIP_IMPLICATION__FIRST: Number
        var RELATIONSHIP_IMPLICATION__SECOND: Number
        var RELATIONSHIPEXCLUSION: Number
        var RELATIONSHIPEXCLUSION_FEATURE_COUNT: Number
        var RELATIONSHIPEXCLUSION_OPERATION_COUNT: Number
        var RELATIONSHIP_EXCLUSION__FIRST: Number
        var RELATIONSHIP_EXCLUSION__SECOND: Number
        var IRREFLEXIVE: Number
        var IRREFLEXIVE_FEATURE_COUNT: Number
        var IRREFLEXIVE_OPERATION_COUNT: Number
        var IRREFLEXIVE__RELATION: Number
        var CYCLIC: Number
        var CYCLIC_FEATURE_COUNT: Number
        var CYCLIC_OPERATION_COUNT: Number
        var CYCLIC__RELATION: Number
        var TOTAL: Number
        var TOTAL_FEATURE_COUNT: Number
        var TOTAL_OPERATION_COUNT: Number
        var TOTAL__RELATION: Number
        var ACYCLIC: Number
        var ACYCLIC_FEATURE_COUNT: Number
        var ACYCLIC_OPERATION_COUNT: Number
        var ACYCLIC__RELATION: Number
        var REFLEXIVE: Number
        var REFLEXIVE_FEATURE_COUNT: Number
        var REFLEXIVE_OPERATION_COUNT: Number
        var REFLEXIVE__RELATION: Number
        var ROLEGROUP: Number
        var ROLEGROUP_FEATURE_COUNT: Number
        var ROLEGROUP_OPERATION_COUNT: Number
        var ROLE_GROUP__NAME: Number
        var ROLE_GROUP__INCOMING: Number
        var ROLE_GROUP__OUTGOING: Number
        var ROLE_GROUP__LOWER: Number
        var ROLE_GROUP__UPPER: Number
        var ROLE_GROUP__ELEMENTS: Number
        var ROLEIMPLICATION: Number
        var ROLEIMPLICATION_FEATURE_COUNT: Number
        var ROLEIMPLICATION_OPERATION_COUNT: Number
        var ROLE_IMPLICATION__FIRST: Number
        var ROLE_IMPLICATION__SECOND: Number
        var ROLEEQUIVALENCE: Number
        var ROLEEQUIVALENCE_FEATURE_COUNT: Number
        var ROLEEQUIVALENCE_OPERATION_COUNT: Number
        var ROLE_EQUIVALENCE__FIRST: Number
        var ROLE_EQUIVALENCE__SECOND: Number
        var ROLEPROHIBITION: Number
        var ROLEPROHIBITION_FEATURE_COUNT: Number
        var ROLEPROHIBITION_OPERATION_COUNT: Number
        var ROLE_PROHIBITION__FIRST: Number
        var ROLE_PROHIBITION__SECOND: Number
        var PART: Number
        var PART_FEATURE_COUNT: Number
        var PART_OPERATION_COUNT: Number
        var PART__WHOLE: Number
        var PART__LOWER: Number
        var PART__UPPER: Number
        var PART__ROLE: Number
        var PARTHOODCONSTRAINT: Number
        var PARTHOODCONSTRAINT_FEATURE_COUNT: Number
        var PARTHOODCONSTRAINT_OPERATION_COUNT: Number
        var PARTHOOD_CONSTRAINT__RELATION: Number
        var PARTHOOD_CONSTRAINT__KIND: Number
        var ABSTRACTROLEREF: Number
        var ABSTRACTROLEREF_FEATURE_COUNT: Number
        var ABSTRACTROLEREF_OPERATION_COUNT: Number
        var ABSTRACT_ROLE_REF__REF: Number
        var DIRECTION: Number
        var PARTHOOD: Number
        var eINSTANCE: Any
    }
}