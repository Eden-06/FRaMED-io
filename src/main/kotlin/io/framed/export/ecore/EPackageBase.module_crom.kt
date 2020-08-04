@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package io.framed.export.ecore

abstract external class EPackageBase : ENamedElementImpl, EPackage {
    open var _nsURI: Any
    open var _nsPrefix: Any
    open var _eFactoryInstance: Any
    open var _eClassifiers: Any
    open var _eSubpackages: Any
    override fun getEClassifier(name: String): EClassifier
    override fun eStaticClass(): EClass
    override fun eInverseAdd(otherEnd: InternalEObject, featureID: Number, msgs: NotificationChain): NotificationChain
    override fun eInverseRemove(otherEnd: InternalEObject, featureID: Number, msgs: NotificationChain): NotificationChain
    open fun basicSetEFactoryInstance(newobj: EFactory, msgs: NotificationChain): NotificationChain
    open fun basicSetESuperPackage(newobj: EPackage, msgs: NotificationChain): NotificationChain
    override fun eGet_number_boolean_boolean(featureID: Number, resolve: Boolean, coreType: Boolean): Any
    override fun eSet_number_any(featureID: Number, newValue: Any)

    companion object {
        var eStaticClass: EClass
    }
}