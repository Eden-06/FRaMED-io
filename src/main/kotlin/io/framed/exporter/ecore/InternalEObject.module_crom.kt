@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package io.framed.exporter.ecore

external interface InternalEObject : EObject {
    fun eNotificationRequired(): Boolean
    fun eURIFragmentSegment(eFeature: EStructuralFeature, eObject: EObject): String
    fun eObjectForURIFragmentSegment(uriFragmentSegment: String): EObject
    fun eSetClass(eClass: EClass)
    fun eBaseStructuralFeatureID(derivedFeatureID: Number, baseClass: Function<*>): Number
    fun eContainerFeatureID(): Number
    fun eDerivedStructuralFeatureID_number_Function(baseFeatureID: Number, baseClass: Function<*>): Number
    fun eDerivedStructuralFeatureID_EStructuralFeature(feature: EStructuralFeature): Number
    fun eDerivedOperationID(baseOperationID: Number, baseClass: Function<*>): Number
    fun eInverseAdd(vararg args: Any): NotificationChain
    fun eInverseAdd3(otherEnd: InternalEObject, featureID: Number, notifications: NotificationChain): NotificationChain
    fun eInverseAdd4(otherEnd: InternalEObject, featureID: Number, baseClass: Function<*>, notifications: NotificationChain): NotificationChain
    fun eInverseRemove(vararg args: Any): NotificationChain
    fun eInverseRemove3(otherEnd: InternalEObject, featureID: Number, notifications: NotificationChain): NotificationChain
    fun eInverseRemove4(otherEnd: InternalEObject, featureID: Number, baseClass: Function<*>, notifications: NotificationChain): NotificationChain
    fun eBasicSetContainer(newContainer: InternalEObject, newContainerFeatureID: Number, notifications: NotificationChain): NotificationChain
    fun eBasicRemoveFromContainer(notifications: NotificationChain): NotificationChain
    fun eResolveProxy(proxy: InternalEObject): EObject
    fun eInternalContainer(): InternalEObject
    override fun eGet(vararg args: Any): Any
    fun eGet_EStructuralFeature_boolean_boolean(eFeature: EStructuralFeature, resolve: Boolean, coreType: Boolean): Any
    fun eGet_number_boolean_boolean(featureID: Number, resolve: Boolean, coreType: Boolean): Any
}