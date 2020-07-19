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

external open class BasicEObjectImpl : BasicNotifierImpl, EObject, InternalEObject {
    open var _uuid: String
    open var _eContainer: InternalEObject
    open var _eContainerFeatureID: Number
    open var _eStaticClass: EClass
    override fun eBasicSetContainer(newContainer: InternalEObject, newContainerFeatureID: Number, notifications: NotificationChain): NotificationChain
    open fun eBasicSetContainer_(newContainer: InternalEObject, newContainerFeatureID: Number)
    override fun eInternalContainer(): InternalEObject
    override fun eContainerFeatureID(): Number
    override fun eBasicRemoveFromContainer(notifications: NotificationChain): NotificationChain
    override fun eGet(vararg args: Any): Any
    override fun eGet_EStructuralFeature_boolean_boolean(feature: EStructuralFeature, resolve: Boolean, coreType: Boolean): Any
    override fun eGet_number_boolean_boolean(featureID: Number, resolve: Boolean, coreType: Boolean): Any
    override fun eDerivedStructuralFeatureID_EStructuralFeature(eStructuralFeature: EStructuralFeature): Number
    override fun eDerivedStructuralFeatureID_number_Function(baseFeatureID: Number, baseClass: Function<*>): Number
    open fun eDerivedStructuralFeatureID(vararg args: Any): Number
    override fun eClass(): EClass
    open fun eStaticClass(): EClass
    override fun eInverseAdd(vararg args: Any): NotificationChain
    override fun eInverseAdd4(otherEnd: InternalEObject, featureID: Number, baseClass: Function<*>, msgs: NotificationChain): NotificationChain
    override fun eInverseRemove(vararg args: Any): NotificationChain
    override fun eInverseAdd3(otherEnd: InternalEObject, featureID: Number, msgs: NotificationChain): NotificationChain
    override fun eInverseRemove4(otherEnd: InternalEObject, featureID: Number, baseClass: Function<*>, msgs: NotificationChain): NotificationChain
    override fun eInverseRemove3(otherEnd: InternalEObject, featureID: Number, msgs: NotificationChain): NotificationChain
    override fun eIsSet(feature: EStructuralFeature): Boolean
    override fun eContainmentFeature(): EReference
    override fun eIsProxy(): Boolean
    override fun eContainingFeature(): EStructuralFeature
    override fun eContainer(): EObject
    override fun eInvoke(operation: EOperation, args: EList<Any>): Any
    override fun eSet(feature: EStructuralFeature, newValue: Any)
    open fun eSet_number_any(featureId: Number, newValue: Any)
    override fun eResource(): Resource
    override fun eContents(): EList<EObject>
    override fun eCrossReferences(): EList<EObject>
    override fun eAllContents(): TreeIterator<EObject>
    override fun eGet_EStructuralFeature(feature: EStructuralFeature): Any
    override fun eGet_EStructuralFeature_boolean(feature: EStructuralFeature, resolve: Boolean): Any
    override fun eUnset(feature: EStructuralFeature)
    override fun eURIFragmentSegment(eFeature: EStructuralFeature, eObject: EObject): String
    override fun eObjectForURIFragmentSegment(uriFragmentSegment: String): EObject
    override fun eSetClass(eClass: EClass)
    override fun eBaseStructuralFeatureID(derivedFeatureID: Number, baseClass: Function<*>): Number
    override fun eDerivedOperationID(baseOperationID: Number, baseClass: Function<*>): Number
    override fun eResolveProxy(proxy: InternalEObject): EObject

    companion object {
        var generateUUID: Any
        var EOPPOSITE_FEATURE_BASE: Number
    }
}