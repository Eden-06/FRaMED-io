@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package io.framed.export.ecore

external interface EPackage : ENamedElement {
    var nsURI: String
    var nsPrefix: String
    var eFactoryInstance: EFactory
    var eClassifiers: OrderedSet<EClassifier>
    var eSubpackages: OrderedSet<EPackage>
    var eSuperPackage: EPackage
    fun getEClassifier(name: String): EClassifier
}