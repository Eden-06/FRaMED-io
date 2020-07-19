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

external interface EClass : EClassifier {
    var abstract: Boolean
    var `interface`: Boolean
    var eSuperTypes: OrderedSet<EClass>
    var eOperations: OrderedSet<EOperation>
    var eAllAttributes: OrderedSet<EAttribute>
    var eAllReferences: OrderedSet<EReference>
    var eReferences: OrderedSet<EReference>
    var eAttributes: OrderedSet<EAttribute>
    var eAllContainments: OrderedSet<EReference>
    var eAllOperations: OrderedSet<EOperation>
    var eAllStructuralFeatures: OrderedSet<EStructuralFeature>
    var eAllSuperTypes: OrderedSet<EClass>
    var eIDAttribute: EAttribute
    var eStructuralFeatures: OrderedSet<EStructuralFeature>
    var eGenericSuperTypes: OrderedSet<EGenericType>
    var eAllGenericSuperTypes: OrderedSet<EGenericType>
    fun isSuperTypeOf(someClass: EClass): Boolean
    fun getOverride(operation: EOperation): EOperation
    fun getFeatureType(feature: EStructuralFeature): EGenericType
    fun getFeatureID(feature: EStructuralFeature): Number
    fun getFeatureCount(): Number
    fun getOperationID(operation: EOperation): Number
    fun getEStructuralFeature(vararg args: Any): Any
    fun getEStructuralFeature_number(featureID: Number): EStructuralFeature
    fun getEStructuralFeature_string(featureName: String): EStructuralFeature
    fun getOperationCount(): Number
    fun getEOperation(operationID: Number): EOperation
}