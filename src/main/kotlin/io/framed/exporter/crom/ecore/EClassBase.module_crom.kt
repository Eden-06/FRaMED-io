@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package io.framed.exporter.crom.ecore

abstract external class EClassBase : EClassifierImpl, EClass {
    open var _abstract: Any
    open var _interface: Any
    open var _eSuperTypes: Any
    open var _eOperations: Any
    open var _eStructuralFeatures: Any
    open var _eGenericSuperTypes: Any
    override fun isSuperTypeOf(someClass: EClass): Boolean
    override fun getOverride(operation: EOperation): EOperation
    override fun getFeatureType(feature: EStructuralFeature): EGenericType
    override fun getFeatureID(feature: EStructuralFeature): Number
    override fun getFeatureCount(): Number
    override fun getOperationID(operation: EOperation): Number
    override fun getOperationCount(): Number
    override fun getEStructuralFeature(vararg args: Any): Any
    override fun getEStructuralFeature_number(featureID: Number): EStructuralFeature
    override fun getEStructuralFeature_string(featureName: String): EStructuralFeature
    override fun getEOperation(operationID: Number): EOperation
    override fun eStaticClass(): EClass
    override fun eInverseAdd(otherEnd: InternalEObject, featureID: Number, msgs: NotificationChain): NotificationChain
    override fun eInverseRemove(otherEnd: InternalEObject, featureID: Number, msgs: NotificationChain): NotificationChain
    override fun eGet_number_boolean_boolean(featureID: Number, resolve: Boolean, coreType: Boolean): Any
    override fun eSet_number_any(featureID: Number, newValue: Any)

    companion object {
        var eStaticClass: EClass
    }
}