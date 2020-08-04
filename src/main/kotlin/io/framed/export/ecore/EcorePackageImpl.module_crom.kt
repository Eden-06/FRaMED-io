@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package io.framed.export.ecore

abstract external class EcorePackageImpl : EPackageImpl, EcorePackage {
    open var isCreated: Any
    open var createPackageContents: () -> Unit
    open var isInitialized: Any
    open var initializePackageContents: () -> Unit
    open var EAttributeEClass: Any
    open var EAnnotationEClass: Any
    open var EClassEClass: Any
    open var EClassifierEClass: Any
    open var EDataTypeEClass: Any
    open var EEnumEClass: Any
    open var EEnumLiteralEClass: Any
    open var EFactoryEClass: Any
    open var EModelElementEClass: Any
    open var ENamedElementEClass: Any
    open var EObjectEClass: Any
    open var EOperationEClass: Any
    open var EPackageEClass: Any
    open var EParameterEClass: Any
    open var EReferenceEClass: Any
    open var EStructuralFeatureEClass: Any
    open var ETypedElementEClass: Any
    open var EStringToStringMapEntryEClass: Any
    open var EGenericTypeEClass: Any
    open var ETypeParameterEClass: Any
    open var EBigDecimalEDataType: Any
    open var EBigIntegerEDataType: Any
    open var EBooleanEDataType: Any
    open var EBooleanObjectEDataType: Any
    open var EByteEDataType: Any
    open var EByteArrayEDataType: Any
    open var EByteObjectEDataType: Any
    open var ECharEDataType: Any
    open var ECharacterObjectEDataType: Any
    open var EDateEDataType: Any
    open var EDiagnosticChainEDataType: Any
    open var EDoubleEDataType: Any
    open var EDoubleObjectEDataType: Any
    open var EEListEDataType: Any
    open var EEnumeratorEDataType: Any
    open var EFeatureMapEDataType: Any
    open var EFeatureMapEntryEDataType: Any
    open var EFloatEDataType: Any
    open var EFloatObjectEDataType: Any
    open var EIntEDataType: Any
    open var EIntegerObjectEDataType: Any
    open var EJavaClassEDataType: Any
    open var EJavaObjectEDataType: Any
    open var ELongEDataType: Any
    open var ELongObjectEDataType: Any
    open var EMapEDataType: Any
    open var EResourceEDataType: Any
    open var EResourceSetEDataType: Any
    open var EShortEDataType: Any
    open var EShortObjectEDataType: Any
    open var EStringEDataType: Any
    open var ETreeIteratorEDataType: Any
    open var EInvocationTargetExceptionEDataType: Any
    open var getEModelElement: () -> EClass
    open var getEModelElement_EAnnotations: () -> EReference
    open var getEModelElement__GetEAnnotation__Source: () -> EOperation
    open var getENamedElement: () -> EClass
    open var getENamedElement_Name: () -> EAttribute
    open var getETypedElement: () -> EClass
    open var getETypedElement_Ordered: () -> EAttribute
    open var getETypedElement_Unique: () -> EAttribute
    open var getETypedElement_LowerBound: () -> EAttribute
    open var getETypedElement_UpperBound: () -> EAttribute
    open var getETypedElement_Many: () -> EAttribute
    open var getETypedElement_Required: () -> EAttribute
    open var getETypedElement_EType: () -> EReference
    open var getETypedElement_EGenericType: () -> EReference
    open var getEStructuralFeature: () -> EClass
    open var getEStructuralFeature_Changeable: () -> EAttribute
    open var getEStructuralFeature_Volatile: () -> EAttribute
    open var getEStructuralFeature_Transient: () -> EAttribute
    open var getEStructuralFeature_DefaultValueLiteral: () -> EAttribute
    open var getEStructuralFeature_DefaultValue: () -> EAttribute
    open var getEStructuralFeature_Unsettable: () -> EAttribute
    open var getEStructuralFeature_Derived: () -> EAttribute
    open var getEStructuralFeature_EContainingClass: () -> EReference
    open var getEStructuralFeature__GetFeatureID__: () -> EOperation
    open var getEStructuralFeature__GetContainerClass__: () -> EOperation
    open var getEAttribute: () -> EClass
    open var getEAttribute_ID: () -> EAttribute
    open var getEAttribute_EAttributeType: () -> EReference
    open var getEAnnotation_: () -> EClass
    open var getEAnnotation_Source: () -> EAttribute
    open var getEAnnotation_Details: () -> EReference
    open var getEAnnotation_EModelElement: () -> EReference
    open var getEAnnotation_Contents: () -> EReference
    open var getEAnnotation_References: () -> EReference
    open var getEClassifier_: () -> EClass
    open var getEClassifier_InstanceClassName: () -> EAttribute
    open var getEClassifier_InstanceClass: () -> EAttribute
    open var getEClassifier_DefaultValue: () -> EAttribute
    open var getEClassifier_InstanceTypeName: () -> EAttribute
    open var getEClassifier_EPackage: () -> EReference
    open var getEClassifier_ETypeParameters: () -> EReference
    open var getEClassifier__IsInstance__Object: () -> EOperation
    open var getEClassifier__GetClassifierID__: () -> EOperation
    open var getEClass: () -> EClass
    open var getEClass_Abstract: () -> EAttribute
    open var getEClass_Interface: () -> EAttribute
    open var getEClass_ESuperTypes: () -> EReference
    open var getEClass_EOperations: () -> EReference
    open var getEClass_EAllAttributes: () -> EReference
    open var getEClass_EAllReferences: () -> EReference
    open var getEClass_EReferences: () -> EReference
    open var getEClass_EAttributes: () -> EReference
    open var getEClass_EAllContainments: () -> EReference
    open var getEClass_EAllOperations: () -> EReference
    open var getEClass_EAllStructuralFeatures: () -> EReference
    open var getEClass_EAllSuperTypes: () -> EReference
    open var getEClass_EIDAttribute: () -> EReference
    open var getEClass_EStructuralFeatures: () -> EReference
    open var getEClass_EGenericSuperTypes: () -> EReference
    open var getEClass_EAllGenericSuperTypes: () -> EReference
    open var getEClass__IsSuperTypeOf__SomeClass: () -> EOperation
    open var getEClass__GetFeatureCount__: () -> EOperation
    open var getEClass__GetEStructuralFeature__FeatureID: () -> EOperation
    open var getEClass__GetFeatureID__Feature: () -> EOperation
    open var getEClass__GetEStructuralFeature__FeatureName: () -> EOperation
    open var getEClass__GetOperationCount__: () -> EOperation
    open var getEClass__GetEOperation__OperationID: () -> EOperation
    open var getEClass__GetOperationID__Operation: () -> EOperation
    open var getEClass__GetOverride__Operation: () -> EOperation
    open var getEClass__GetFeatureType__Feature: () -> EOperation
    open var getEDataType: () -> EClass
    open var getEDataType_Serializable: () -> EAttribute
    open var getEEnum: () -> EClass
    open var getEEnum_ELiterals: () -> EReference
    open var getEEnum__GetEEnumLiteral__Name: () -> EOperation
    open var getEEnum__GetEEnumLiteral__Value: () -> EOperation
    open var getEEnum__GetEEnumLiteralByLiteral__Literal: () -> EOperation
    open var getEEnumLiteral: () -> EClass
    open var getEEnumLiteral_Value: () -> EAttribute
    open var getEEnumLiteral_Instance: () -> EAttribute
    open var getEEnumLiteral_Literal: () -> EAttribute
    open var getEEnumLiteral_EEnum: () -> EReference
    open var getEFactory: () -> EClass
    open var getEFactory_EPackage: () -> EReference
    open var getEFactory__Create__EClass: () -> EOperation
    open var getEFactory__CreateFromString__EDataType__LiteralValue: () -> EOperation
    open var getEFactory__ConvertToString__EDataType__InstanceValue: () -> EOperation
    open var getEObject: () -> EClass
    open var getEObject__EClass__: () -> EOperation
    open var getEObject__EIsProxy__: () -> EOperation
    open var getEObject__EResource__: () -> EOperation
    open var getEObject__EContainer__: () -> EOperation
    open var getEObject__EContainingFeature__: () -> EOperation
    open var getEObject__EContainmentFeature__: () -> EOperation
    open var getEObject__EContents__: () -> EOperation
    open var getEObject__EAllContents__: () -> EOperation
    open var getEObject__ECrossReferences__: () -> EOperation
    open var getEObject__EGet__Feature: () -> EOperation
    open var getEObject__EGet__Feature__Resolve: () -> EOperation
    open var getEObject__ESet__Feature__NewValue: () -> EOperation
    open var getEObject__EIsSet__Feature: () -> EOperation
    open var getEObject__EUnset__Feature: () -> EOperation
    open var getEObject__EInvoke__Operation__Arguments: () -> EOperation
    open var getEOperation: () -> EClass
    open var getEOperation_EContainingClass: () -> EReference
    open var getEOperation_ETypeParameters: () -> EReference
    open var getEOperation_EParameters: () -> EReference
    open var getEOperation_EExceptions: () -> EReference
    open var getEOperation_EGenericExceptions: () -> EReference
    open var getEOperation__GetOperationID__: () -> EOperation
    open var getEOperation__IsOverrideOf__SomeOperation: () -> EOperation
    open var getEPackage: () -> EClass
    open var getEPackage_NsURI: () -> EAttribute
    open var getEPackage_NsPrefix: () -> EAttribute
    open var getEPackage_EFactoryInstance: () -> EReference
    open var getEPackage_EClassifiers: () -> EReference
    open var getEPackage_ESubpackages: () -> EReference
    open var getEPackage_ESuperPackage: () -> EReference
    open var getEPackage__GetEClassifier__Name: () -> EOperation
    open var getEParameter: () -> EClass
    open var getEParameter_EOperation: () -> EReference
    open var getEReference: () -> EClass
    open var getEReference_Containment: () -> EAttribute
    open var getEReference_Container: () -> EAttribute
    open var getEReference_ResolveProxies: () -> EAttribute
    open var getEReference_EOpposite: () -> EReference
    open var getEReference_EReferenceType: () -> EReference
    open var getEReference_EKeys: () -> EReference
    open var getEStringToStringMapEntry: () -> EClass
    open var getEStringToStringMapEntry_Key: () -> EAttribute
    open var getEStringToStringMapEntry_Value: () -> EAttribute
    open var getEGenericType: () -> EClass
    open var getEGenericType_EUpperBound: () -> EReference
    open var getEGenericType_ETypeArguments: () -> EReference
    open var getEGenericType_ERawType: () -> EReference
    open var getEGenericType_ELowerBound: () -> EReference
    open var getEGenericType_ETypeParameter: () -> EReference
    open var getEGenericType_EClassifier: () -> EReference
    open var getEGenericType__IsInstance__Object: () -> EOperation
    open var getETypeParameter: () -> EClass
    open var getETypeParameter_EBounds: () -> EReference
    open var getEBigDecimal: () -> EDataType
    open var getEBigInteger: () -> EDataType
    open var getEBoolean: () -> EDataType
    open var getEBooleanObject: () -> EDataType
    open var getEByte: () -> EDataType
    open var getEByteArray: () -> EDataType
    open var getEByteObject: () -> EDataType
    open var getEChar: () -> EDataType
    open var getECharacterObject: () -> EDataType
    open var getEDate: () -> EDataType
    open var getEDiagnosticChain: () -> EDataType
    open var getEDouble: () -> EDataType
    open var getEDoubleObject: () -> EDataType
    open var getEEList: () -> EDataType
    open var getEEnumerator: () -> EDataType
    open var getEFeatureMap: () -> EDataType
    open var getEFeatureMapEntry: () -> EDataType
    open var getEFloat: () -> EDataType
    open var getEFloatObject: () -> EDataType
    open var getEInt: () -> EDataType
    open var getEIntegerObject: () -> EDataType
    open var getEJavaClass: () -> EDataType
    open var getEJavaObject: () -> EDataType
    open var getELong: () -> EDataType
    open var getELongObject: () -> EDataType
    open var getEMap: () -> EDataType
    open var getEResource: () -> EDataType
    open var getEResourceSet: () -> EDataType
    open var getEShort: () -> EDataType
    open var getEShortObject: () -> EDataType
    open var getEString: () -> EDataType
    open var getETreeIterator: () -> EDataType
    open var getEInvocationTargetException: () -> EDataType

    companion object {
        var eNAME: String
        var eNS_URI: String
        var eNS_PREFIX: String
        fun init(): EcorePackage
        var EMODELELEMENT: Number
        var EMODELELEMENT_FEATURE_COUNT: Number
        var EMODELELEMENT_OPERATION_COUNT: Number
        var EMODELELEMENT_EANNOTATIONS: Number
        var EMODELELEMENT___GETEANNOTATION__SOURCE: Number
        var ENAMEDELEMENT: Number
        var ENAMEDELEMENT_FEATURE_COUNT: Number
        var ENAMEDELEMENT_OPERATION_COUNT: Number
        var ENAMEDELEMENT_EANNOTATIONS: Number
        var ENAMEDELEMENT_NAME: Number
        var ETYPEDELEMENT: Number
        var ETYPEDELEMENT_FEATURE_COUNT: Number
        var ETYPEDELEMENT_OPERATION_COUNT: Number
        var ETYPEDELEMENT_EANNOTATIONS: Number
        var ETYPEDELEMENT_NAME: Number
        var ETYPEDELEMENT_ORDERED: Number
        var ETYPEDELEMENT_UNIQUE: Number
        var ETYPEDELEMENT_LOWERBOUND: Number
        var ETYPEDELEMENT_UPPERBOUND: Number
        var ETYPEDELEMENT_MANY: Number
        var ETYPEDELEMENT_REQUIRED: Number
        var ETYPEDELEMENT_ETYPE: Number
        var ETYPEDELEMENT_EGENERICTYPE: Number
        var ESTRUCTURALFEATURE: Number
        var ESTRUCTURALFEATURE_FEATURE_COUNT: Number
        var ESTRUCTURALFEATURE_OPERATION_COUNT: Number
        var ESTRUCTURALFEATURE_EANNOTATIONS: Number
        var ESTRUCTURALFEATURE_NAME: Number
        var ESTRUCTURALFEATURE_ORDERED: Number
        var ESTRUCTURALFEATURE_UNIQUE: Number
        var ESTRUCTURALFEATURE_LOWERBOUND: Number
        var ESTRUCTURALFEATURE_UPPERBOUND: Number
        var ESTRUCTURALFEATURE_MANY: Number
        var ESTRUCTURALFEATURE_REQUIRED: Number
        var ESTRUCTURALFEATURE_ETYPE: Number
        var ESTRUCTURALFEATURE_EGENERICTYPE: Number
        var ESTRUCTURALFEATURE_CHANGEABLE: Number
        var ESTRUCTURALFEATURE_VOLATILE: Number
        var ESTRUCTURALFEATURE_TRANSIENT: Number
        var ESTRUCTURALFEATURE_DEFAULTVALUELITERAL: Number
        var ESTRUCTURALFEATURE_DEFAULTVALUE: Number
        var ESTRUCTURALFEATURE_UNSETTABLE: Number
        var ESTRUCTURALFEATURE_DERIVED: Number
        var ESTRUCTURALFEATURE_ECONTAININGCLASS: Number
        var ESTRUCTURALFEATURE___GETFEATUREID: Number
        var ESTRUCTURALFEATURE___GETCONTAINERCLASS: Number
        var EATTRIBUTE: Number
        var EATTRIBUTE_FEATURE_COUNT: Number
        var EATTRIBUTE_OPERATION_COUNT: Number
        var EATTRIBUTE_EANNOTATIONS: Number
        var EATTRIBUTE_NAME: Number
        var EATTRIBUTE_ORDERED: Number
        var EATTRIBUTE_UNIQUE: Number
        var EATTRIBUTE_LOWERBOUND: Number
        var EATTRIBUTE_UPPERBOUND: Number
        var EATTRIBUTE_MANY: Number
        var EATTRIBUTE_REQUIRED: Number
        var EATTRIBUTE_ETYPE: Number
        var EATTRIBUTE_EGENERICTYPE: Number
        var EATTRIBUTE_CHANGEABLE: Number
        var EATTRIBUTE_VOLATILE: Number
        var EATTRIBUTE_TRANSIENT: Number
        var EATTRIBUTE_DEFAULTVALUELITERAL: Number
        var EATTRIBUTE_DEFAULTVALUE: Number
        var EATTRIBUTE_UNSETTABLE: Number
        var EATTRIBUTE_DERIVED: Number
        var EATTRIBUTE_ECONTAININGCLASS: Number
        var EATTRIBUTE_ID: Number
        var EATTRIBUTE_EATTRIBUTETYPE: Number
        var EANNOTATION: Number
        var EANNOTATION_FEATURE_COUNT: Number
        var EANNOTATION_OPERATION_COUNT: Number
        var EANNOTATION_EANNOTATIONS: Number
        var EANNOTATION_SOURCE: Number
        var EANNOTATION_DETAILS: Number
        var EANNOTATION_EMODELELEMENT: Number
        var EANNOTATION_CONTENTS: Number
        var EANNOTATION_REFERENCES: Number
        var ECLASSIFIER: Number
        var ECLASSIFIER_FEATURE_COUNT: Number
        var ECLASSIFIER_OPERATION_COUNT: Number
        var ECLASSIFIER_EANNOTATIONS: Number
        var ECLASSIFIER_NAME: Number
        var ECLASSIFIER_INSTANCECLASSNAME: Number
        var ECLASSIFIER_INSTANCECLASS: Number
        var ECLASSIFIER_DEFAULTVALUE: Number
        var ECLASSIFIER_INSTANCETYPENAME: Number
        var ECLASSIFIER_EPACKAGE: Number
        var ECLASSIFIER_ETYPEPARAMETERS: Number
        var ECLASSIFIER___ISINSTANCE__OBJECT: Number
        var ECLASSIFIER___GETCLASSIFIERID: Number
        var ECLASS: Number
        var ECLASS_FEATURE_COUNT: Number
        var ECLASS_OPERATION_COUNT: Number
        var ECLASS_EANNOTATIONS: Number
        var ECLASS_NAME: Number
        var ECLASS_INSTANCECLASSNAME: Number
        var ECLASS_INSTANCECLASS: Number
        var ECLASS_DEFAULTVALUE: Number
        var ECLASS_INSTANCETYPENAME: Number
        var ECLASS_EPACKAGE: Number
        var ECLASS_ETYPEPARAMETERS: Number
        var ECLASS_ABSTRACT: Number
        var ECLASS_INTERFACE: Number
        var ECLASS_ESUPERTYPES: Number
        var ECLASS_EOPERATIONS: Number
        var ECLASS_EALLATTRIBUTES: Number
        var ECLASS_EALLREFERENCES: Number
        var ECLASS_EREFERENCES: Number
        var ECLASS_EATTRIBUTES: Number
        var ECLASS_EALLCONTAINMENTS: Number
        var ECLASS_EALLOPERATIONS: Number
        var ECLASS_EALLSTRUCTURALFEATURES: Number
        var ECLASS_EALLSUPERTYPES: Number
        var ECLASS_EIDATTRIBUTE: Number
        var ECLASS_ESTRUCTURALFEATURES: Number
        var ECLASS_EGENERICSUPERTYPES: Number
        var ECLASS_EALLGENERICSUPERTYPES: Number
        var ECLASS___ISSUPERTYPEOF__SOMECLASS: Number
        var ECLASS___GETFEATURECOUNT: Number
        var ECLASS___GETESTRUCTURALFEATURE__FEATUREID: Number
        var ECLASS___GETFEATUREID__FEATURE: Number
        var ECLASS___GETESTRUCTURALFEATURE__FEATURENAME: Number
        var ECLASS___GETOPERATIONCOUNT: Number
        var ECLASS___GETEOPERATION__OPERATIONID: Number
        var ECLASS___GETOPERATIONID__OPERATION: Number
        var ECLASS___GETOVERRIDE__OPERATION: Number
        var ECLASS___GETFEATURETYPE__FEATURE: Number
        var EDATATYPE: Number
        var EDATATYPE_FEATURE_COUNT: Number
        var EDATATYPE_OPERATION_COUNT: Number
        var EDATATYPE_EANNOTATIONS: Number
        var EDATATYPE_NAME: Number
        var EDATATYPE_INSTANCECLASSNAME: Number
        var EDATATYPE_INSTANCECLASS: Number
        var EDATATYPE_DEFAULTVALUE: Number
        var EDATATYPE_INSTANCETYPENAME: Number
        var EDATATYPE_EPACKAGE: Number
        var EDATATYPE_ETYPEPARAMETERS: Number
        var EDATATYPE_SERIALIZABLE: Number
        var EENUM: Number
        var EENUM_FEATURE_COUNT: Number
        var EENUM_OPERATION_COUNT: Number
        var EENUM_EANNOTATIONS: Number
        var EENUM_NAME: Number
        var EENUM_INSTANCECLASSNAME: Number
        var EENUM_INSTANCECLASS: Number
        var EENUM_DEFAULTVALUE: Number
        var EENUM_INSTANCETYPENAME: Number
        var EENUM_EPACKAGE: Number
        var EENUM_ETYPEPARAMETERS: Number
        var EENUM_SERIALIZABLE: Number
        var EENUM_ELITERALS: Number
        var EENUM___GETEENUMLITERAL__NAME: Number
        var EENUM___GETEENUMLITERAL__VALUE: Number
        var EENUM___GETEENUMLITERALBYLITERAL__LITERAL: Number
        var EENUMLITERAL: Number
        var EENUMLITERAL_FEATURE_COUNT: Number
        var EENUMLITERAL_OPERATION_COUNT: Number
        var EENUMLITERAL_EANNOTATIONS: Number
        var EENUMLITERAL_NAME: Number
        var EENUMLITERAL_VALUE: Number
        var EENUMLITERAL_INSTANCE: Number
        var EENUMLITERAL_LITERAL: Number
        var EENUMLITERAL_EENUM: Number
        var EFACTORY: Number
        var EFACTORY_FEATURE_COUNT: Number
        var EFACTORY_OPERATION_COUNT: Number
        var EFACTORY_EANNOTATIONS: Number
        var EFACTORY_EPACKAGE: Number
        var EFACTORY___CREATE__ECLASS: Number
        var EFACTORY___CREATEFROMSTRING__EDATATYPE__LITERALVALUE: Number
        var EFACTORY___CONVERTTOSTRING__EDATATYPE__INSTANCEVALUE: Number
        var EOBJECT: Number
        var EOBJECT_FEATURE_COUNT: Number
        var EOBJECT_OPERATION_COUNT: Number
        var EOBJECT___ECLASS: Number
        var EOBJECT___EISPROXY: Number
        var EOBJECT___ERESOURCE: Number
        var EOBJECT___ECONTAINER: Number
        var EOBJECT___ECONTAININGFEATURE: Number
        var EOBJECT___ECONTAINMENTFEATURE: Number
        var EOBJECT___ECONTENTS: Number
        var EOBJECT___EALLCONTENTS: Number
        var EOBJECT___ECROSSREFERENCES: Number
        var EOBJECT___EGET__FEATURE: Number
        var EOBJECT___EGET__FEATURE__RESOLVE: Number
        var EOBJECT___ESET__FEATURE__NEWVALUE: Number
        var EOBJECT___EISSET__FEATURE: Number
        var EOBJECT___EUNSET__FEATURE: Number
        var EOBJECT___EINVOKE__OPERATION__ARGUMENTS: Number
        var EOPERATION: Number
        var EOPERATION_FEATURE_COUNT: Number
        var EOPERATION_OPERATION_COUNT: Number
        var EOPERATION_EANNOTATIONS: Number
        var EOPERATION_NAME: Number
        var EOPERATION_ORDERED: Number
        var EOPERATION_UNIQUE: Number
        var EOPERATION_LOWERBOUND: Number
        var EOPERATION_UPPERBOUND: Number
        var EOPERATION_MANY: Number
        var EOPERATION_REQUIRED: Number
        var EOPERATION_ETYPE: Number
        var EOPERATION_EGENERICTYPE: Number
        var EOPERATION_ECONTAININGCLASS: Number
        var EOPERATION_ETYPEPARAMETERS: Number
        var EOPERATION_EPARAMETERS: Number
        var EOPERATION_EEXCEPTIONS: Number
        var EOPERATION_EGENERICEXCEPTIONS: Number
        var EOPERATION___GETOPERATIONID: Number
        var EOPERATION___ISOVERRIDEOF__SOMEOPERATION: Number
        var EPACKAGE: Number
        var EPACKAGE_FEATURE_COUNT: Number
        var EPACKAGE_OPERATION_COUNT: Number
        var EPACKAGE_EANNOTATIONS: Number
        var EPACKAGE_NAME: Number
        var EPACKAGE_NSURI: Number
        var EPACKAGE_NSPREFIX: Number
        var EPACKAGE_EFACTORYINSTANCE: Number
        var EPACKAGE_ECLASSIFIERS: Number
        var EPACKAGE_ESUBPACKAGES: Number
        var EPACKAGE_ESUPERPACKAGE: Number
        var EPACKAGE___GETECLASSIFIER__NAME: Number
        var EPARAMETER: Number
        var EPARAMETER_FEATURE_COUNT: Number
        var EPARAMETER_OPERATION_COUNT: Number
        var EPARAMETER_EANNOTATIONS: Number
        var EPARAMETER_NAME: Number
        var EPARAMETER_ORDERED: Number
        var EPARAMETER_UNIQUE: Number
        var EPARAMETER_LOWERBOUND: Number
        var EPARAMETER_UPPERBOUND: Number
        var EPARAMETER_MANY: Number
        var EPARAMETER_REQUIRED: Number
        var EPARAMETER_ETYPE: Number
        var EPARAMETER_EGENERICTYPE: Number
        var EPARAMETER_EOPERATION: Number
        var EREFERENCE: Number
        var EREFERENCE_FEATURE_COUNT: Number
        var EREFERENCE_OPERATION_COUNT: Number
        var EREFERENCE_EANNOTATIONS: Number
        var EREFERENCE_NAME: Number
        var EREFERENCE_ORDERED: Number
        var EREFERENCE_UNIQUE: Number
        var EREFERENCE_LOWERBOUND: Number
        var EREFERENCE_UPPERBOUND: Number
        var EREFERENCE_MANY: Number
        var EREFERENCE_REQUIRED: Number
        var EREFERENCE_ETYPE: Number
        var EREFERENCE_EGENERICTYPE: Number
        var EREFERENCE_CHANGEABLE: Number
        var EREFERENCE_VOLATILE: Number
        var EREFERENCE_TRANSIENT: Number
        var EREFERENCE_DEFAULTVALUELITERAL: Number
        var EREFERENCE_DEFAULTVALUE: Number
        var EREFERENCE_UNSETTABLE: Number
        var EREFERENCE_DERIVED: Number
        var EREFERENCE_ECONTAININGCLASS: Number
        var EREFERENCE_CONTAINMENT: Number
        var EREFERENCE_CONTAINER: Number
        var EREFERENCE_RESOLVEPROXIES: Number
        var EREFERENCE_EOPPOSITE: Number
        var EREFERENCE_EREFERENCETYPE: Number
        var EREFERENCE_EKEYS: Number
        var ESTRINGTOSTRINGMAPENTRY: Number
        var ESTRINGTOSTRINGMAPENTRY_FEATURE_COUNT: Number
        var ESTRINGTOSTRINGMAPENTRY_OPERATION_COUNT: Number
        var ESTRINGTOSTRINGMAPENTRY_KEY: Number
        var ESTRINGTOSTRINGMAPENTRY_VALUE: Number
        var EGENERICTYPE: Number
        var EGENERICTYPE_FEATURE_COUNT: Number
        var EGENERICTYPE_OPERATION_COUNT: Number
        var EGENERICTYPE_EUPPERBOUND: Number
        var EGENERICTYPE_ETYPEARGUMENTS: Number
        var EGENERICTYPE_ERAWTYPE: Number
        var EGENERICTYPE_ELOWERBOUND: Number
        var EGENERICTYPE_ETYPEPARAMETER: Number
        var EGENERICTYPE_ECLASSIFIER: Number
        var EGENERICTYPE___ISINSTANCE__OBJECT: Number
        var ETYPEPARAMETER: Number
        var ETYPEPARAMETER_FEATURE_COUNT: Number
        var ETYPEPARAMETER_OPERATION_COUNT: Number
        var ETYPEPARAMETER_EANNOTATIONS: Number
        var ETYPEPARAMETER_NAME: Number
        var ETYPEPARAMETER_EBOUNDS: Number
        var EBIGDECIMAL: Number
        var EBIGINTEGER: Number
        var EBOOLEAN: Number
        var EBOOLEANOBJECT: Number
        var EBYTE: Number
        var EBYTEARRAY: Number
        var EBYTEOBJECT: Number
        var ECHAR: Number
        var ECHARACTEROBJECT: Number
        var EDATE: Number
        var EDIAGNOSTICCHAIN: Number
        var EDOUBLE: Number
        var EDOUBLEOBJECT: Number
        var EELIST: Number
        var EENUMERATOR: Number
        var EFEATUREMAP: Number
        var EFEATUREMAPENTRY: Number
        var EFLOAT: Number
        var EFLOATOBJECT: Number
        var EINT: Number
        var EINTEGEROBJECT: Number
        var EJAVACLASS: Number
        var EJAVAOBJECT: Number
        var ELONG: Number
        var ELONGOBJECT: Number
        var EMAP: Number
        var ERESOURCE: Number
        var ERESOURCESET: Number
        var ESHORT: Number
        var ESHORTOBJECT: Number
        var ESTRING: Number
        var ETREEITERATOR: Number
        var EINVOCATIONTARGETEXCEPTION: Number
        var eINSTANCE: EcorePackage
    }
}