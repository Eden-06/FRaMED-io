@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package io.framed.exporter.ecore

external interface EcorePackage : EPackage {
    fun getEModelElement(): EClass
    fun getEModelElement_EAnnotations(): EReference
    fun getENamedElement(): EClass
    fun getENamedElement_Name(): EAttribute
    fun getETypedElement(): EClass
    fun getETypedElement_EType(): EReference
    fun getETypedElement_EGenericType(): EReference
    fun getETypedElement_Ordered(): EAttribute
    fun getETypedElement_Unique(): EAttribute
    fun getETypedElement_LowerBound(): EAttribute
    fun getETypedElement_UpperBound(): EAttribute
    fun getETypedElement_Many(): EAttribute
    fun getETypedElement_Required(): EAttribute
    fun getEStructuralFeature(): EClass
    fun getEStructuralFeature_EContainingClass(): EReference
    fun getEStructuralFeature_Changeable(): EAttribute
    fun getEStructuralFeature_Volatile(): EAttribute
    fun getEStructuralFeature_Transient(): EAttribute
    fun getEStructuralFeature_DefaultValueLiteral(): EAttribute
    fun getEStructuralFeature_DefaultValue(): EAttribute
    fun getEStructuralFeature_Unsettable(): EAttribute
    fun getEStructuralFeature_Derived(): EAttribute
    fun getEAttribute(): EClass
    fun getEAttribute_EAttributeType(): EReference
    fun getEAttribute_ID(): EAttribute
    fun getEAnnotation_(): EClass
    fun getEAnnotation_Details(): EReference
    fun getEAnnotation_EModelElement(): EReference
    fun getEAnnotation_Contents(): EReference
    fun getEAnnotation_References(): EReference
    fun getEAnnotation_Source(): EAttribute
    fun getEClassifier_(): EClass
    fun getEClassifier_EPackage(): EReference
    fun getEClassifier_ETypeParameters(): EReference
    fun getEClassifier_InstanceClassName(): EAttribute
    fun getEClassifier_InstanceClass(): EAttribute
    fun getEClassifier_DefaultValue(): EAttribute
    fun getEClassifier_InstanceTypeName(): EAttribute
    fun getEClass(): EClass
    fun getEClass_ESuperTypes(): EReference
    fun getEClass_EOperations(): EReference
    fun getEClass_EAllAttributes(): EReference
    fun getEClass_EAllReferences(): EReference
    fun getEClass_EReferences(): EReference
    fun getEClass_EAttributes(): EReference
    fun getEClass_EAllContainments(): EReference
    fun getEClass_EAllOperations(): EReference
    fun getEClass_EAllStructuralFeatures(): EReference
    fun getEClass_EAllSuperTypes(): EReference
    fun getEClass_EIDAttribute(): EReference
    fun getEClass_EStructuralFeatures(): EReference
    fun getEClass_EGenericSuperTypes(): EReference
    fun getEClass_EAllGenericSuperTypes(): EReference
    fun getEClass_Abstract(): EAttribute
    fun getEClass_Interface(): EAttribute
    fun getEDataType(): EClass
    fun getEDataType_Serializable(): EAttribute
    fun getEEnum(): EClass
    fun getEEnum_ELiterals(): EReference
    fun getEEnumLiteral(): EClass
    fun getEEnumLiteral_EEnum(): EReference
    fun getEEnumLiteral_Value(): EAttribute
    fun getEEnumLiteral_Instance(): EAttribute
    fun getEEnumLiteral_Literal(): EAttribute
    fun getEFactory(): EClass
    fun getEFactory_EPackage(): EReference
    fun getEObject(): EClass
    fun getEOperation(): EClass
    fun getEOperation_EContainingClass(): EReference
    fun getEOperation_ETypeParameters(): EReference
    fun getEOperation_EParameters(): EReference
    fun getEOperation_EExceptions(): EReference
    fun getEOperation_EGenericExceptions(): EReference
    fun getEPackage(): EClass
    fun getEPackage_EFactoryInstance(): EReference
    fun getEPackage_EClassifiers(): EReference
    fun getEPackage_ESubpackages(): EReference
    fun getEPackage_ESuperPackage(): EReference
    fun getEPackage_NsURI(): EAttribute
    fun getEPackage_NsPrefix(): EAttribute
    fun getEParameter(): EClass
    fun getEParameter_EOperation(): EReference
    fun getEReference(): EClass
    fun getEReference_EOpposite(): EReference
    fun getEReference_EReferenceType(): EReference
    fun getEReference_EKeys(): EReference
    fun getEReference_Containment(): EAttribute
    fun getEReference_Container(): EAttribute
    fun getEReference_ResolveProxies(): EAttribute
    fun getEStringToStringMapEntry(): EClass
    fun getEStringToStringMapEntry_Key(): EAttribute
    fun getEStringToStringMapEntry_Value(): EAttribute
    fun getEGenericType(): EClass
    fun getEGenericType_EUpperBound(): EReference
    fun getEGenericType_ETypeArguments(): EReference
    fun getEGenericType_ERawType(): EReference
    fun getEGenericType_ELowerBound(): EReference
    fun getEGenericType_ETypeParameter(): EReference
    fun getEGenericType_EClassifier(): EReference
    fun getETypeParameter(): EClass
    fun getETypeParameter_EBounds(): EReference
    fun getEBigDecimal(): EDataType
    fun getEBigInteger(): EDataType
    fun getEBoolean(): EDataType
    fun getEBooleanObject(): EDataType
    fun getEByte(): EDataType
    fun getEByteArray(): EDataType
    fun getEByteObject(): EDataType
    fun getEChar(): EDataType
    fun getECharacterObject(): EDataType
    fun getEDate(): EDataType
    fun getEDiagnosticChain(): EDataType
    fun getEDouble(): EDataType
    fun getEDoubleObject(): EDataType
    fun getEEList(): EDataType
    fun getEEnumerator(): EDataType
    fun getEFeatureMap(): EDataType
    fun getEFeatureMapEntry(): EDataType
    fun getEFloat(): EDataType
    fun getEFloatObject(): EDataType
    fun getEInt(): EDataType
    fun getEIntegerObject(): EDataType
    fun getEJavaClass(): EDataType
    fun getEJavaObject(): EDataType
    fun getELong(): EDataType
    fun getELongObject(): EDataType
    fun getEMap(): EDataType
    fun getEResource(): EDataType
    fun getEResourceSet(): EDataType
    fun getEShort(): EDataType
    fun getEShortObject(): EDataType
    fun getEString(): EDataType
    fun getETreeIterator(): EDataType
    fun getEInvocationTargetException(): EDataType
}