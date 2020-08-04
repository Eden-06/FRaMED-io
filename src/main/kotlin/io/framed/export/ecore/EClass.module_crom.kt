@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package io.framed.export.ecore

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