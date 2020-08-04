@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package io.framed.exporter.crom.ecore

abstract external class EPackageImpl : EPackageBase {
    open var ecoreFactory: EcoreFactory
    open var ecorePackage: EcorePackage
    override fun getEClassifier(name: String): EClassifier
    open var createEClass: (id: Number) -> EClass
    open var createEEnum: (id: Number) -> EEnum
    open var createEDataType: (id: Number) -> EDataType
    open var createEAttribute: (owner: EClass, id: Number) -> Unit
    open var createEReference: (owner: EClass, id: Number) -> Unit
    open var createEOperation: (owner: EClass, id: Number) -> Unit
    open var initEClass_: (c: EClass, instanceClass: Function<*>, name: String, isAbstract: Boolean, isInterface: Boolean, isGenerated: Boolean) -> EClass
    open var initEClass: (c: EClass, instanceClass: Function<*>, name: String, isAbstract: Boolean, isInterface: Boolean, isGenerated: Boolean, instanceTypeName: String) -> EClass
    open fun initEEnum(e: EEnum, instanceClass: Function<*>, name: String): EEnum
    open var initEDataType_: (d: EDataType, instanceClass: Function<*>, name: String, isSerializable: Boolean) -> EDataType
    open var initEDataType__: (d: EDataType, instanceClass: Function<*>, name: String, isSerializable: Boolean, isGenerated: Boolean) -> EDataType
    open var initEDataType: (d: EDataType, instanceClass: Function<*>, name: String, isSerializable: Boolean, isGenerated: Boolean, instanceTypeName: String) -> EDataType
    open var initEClassifier_: (o: EClassifier, metaObject: EClass, instanceClass: Function<*>, name: String) -> Unit
    open var initEClassifier: (o: EClassifier, metaObject: EClass, instanceClass: Function<*>, name: String, isGenerated: Boolean) -> Unit
    open var setGeneratedClassName: (eClassifier: EClassifier) -> Unit
    open var setInstanceTypeName: (eClassifier: EClassifier, instanceTypeName: String) -> Unit
    open var initEAttribute13: (a: EAttribute, type: EClassifier, name: String, defaultValue: String, lowerBound: Number, upperBound: Number, isTransient: Boolean, isVolatile: Boolean, isChangeable: Boolean, isUnsettable: Boolean, isID: Boolean, isUnique: Boolean, isDerived: Boolean) -> EAttribute
    open var initEAttribute14: (a: EAttribute, type: EClassifier, name: String, defaultValue: String, lowerBound: Number, upperBound: Number, isTransient: Boolean, isVolatile: Boolean, isChangeable: Boolean, isUnsettable: Boolean, isID: Boolean, isUnique: Boolean, isDerived: Boolean, isOrdered: Boolean) -> EAttribute
    open var initEAttribute_EClassifier: (a: EAttribute, type: EClassifier, name: String, defaultValue: String, lowerBound: Number, upperBound: Number, containerClass: Function<*>, isTransient: Boolean, isVolatile: Boolean, isChangeable: Boolean, isUnsettable: Boolean, isID: Boolean, isUnique: Boolean, isDerived: Boolean, isOrdered: Boolean) -> EAttribute
    open var initEAttribute_EGenericType: (a: EAttribute, type: EGenericType, name: String, defaultValue: String, lowerBound: Number, upperBound: Number, containerClass: Function<*>, isTransient: Boolean, isVolatile: Boolean, isChangeable: Boolean, isUnsettable: Boolean, isID: Boolean, isUnique: Boolean, isDerived: Boolean, isOrdered: Boolean) -> EAttribute
    open var initEStructuralFeature_EClassifier: Any
    open var initEStructuralFeature_EGenericType: Any
    open var IS_RESOLVABLE: Boolean
    open var initEReference: (r: EReference, type: EClassifier, otherEnd: EReference, name: String, defaultValue: String, lowerBound: Number, upperBound: Number, containerClass: Function<*>, isTransient: Boolean, isVolatile: Boolean, isChangeable: Boolean, isContainment: Boolean, isResolveProxies: Boolean, isUnsettable: Boolean, isUnique: Boolean, isDerived: Boolean, isOrdered: Boolean) -> EReference
    open var initEReference_EGenericType: (r: EReference, type: EGenericType, otherEnd: EReference, name: String, defaultValue: String, lowerBound: Number, upperBound: Number, containerClass: Function<*>, isTransient: Boolean, isVolatile: Boolean, isChangeable: Boolean, isContainment: Boolean, isResolveProxies: Boolean, isUnsettable: Boolean, isUnique: Boolean, isDerived: Boolean, isOrdered: Boolean) -> EReference
    open var addEOperation_1: (owner: EClass, type: EClassifier, name: String) -> EOperation
    open var addEOperation_2: (owner: EClass, type: EClassifier, name: String, lowerBound: Number, upperBound: Number) -> EOperation
    open var addEOperation_3: (owner: EClass, type: EClassifier, name: String, lowerBound: Number, upperBound: Number, isUnique: Boolean, isOrdered: Boolean) -> EOperation
    open var initEOperation_1: (eOperation: EOperation, type: EClassifier, name: String) -> EOperation
    open var initEOperation_2: (eOperation: EOperation, type: EClassifier, name: String, lowerBound: Number, upperBound: Number) -> EOperation
    open var initEOperation_3: (eOperation: EOperation, type: EClassifier, name: String, lowerBound: Number, upperBound: Number, isUnique: Boolean, isOrdered: Boolean) -> EOperation
    open var initEOperation_4: (eOperation: EOperation, eGenericType: EGenericType) -> Unit

    companion object {
        var IS_ABSTRACT: Boolean
        var IS_INTERFACE: Boolean
        var IS_GENERATED_INSTANCE_CLASS: Boolean
        var IS_SERIALIZABLE: Boolean
        var IS_DERIVED: Boolean
        var IS_TRANSIENT: Boolean
        var IS_VOLATILE: Boolean
        var IS_CHANGEABLE: Boolean
        var IS_UNSETTABLE: Boolean
        var IS_UNIQUE: Boolean
        var IS_ID: Boolean
        var IS_ORDERED: Boolean
        var IS_COMPOSITE: Boolean
        var IS_RESOLVE_PROXIES: Boolean
    }
}