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

external open class RelationshipBase : RelationImpl, Relationship {
    open var _name: Any
    open var _direction: Any
    open var _first: Any
    open var _second: Any
    open var _tr_constraints: Any
    open fun eStaticClass(): EClass
    open fun basicSetFirst(newobj: Place, msgs: NotificationChain): NotificationChain
    open fun basicSetSecond(newobj: Place, msgs: NotificationChain): NotificationChain
    open fun eGet_number_boolean_boolean(featureID: Number, resolve: Boolean, coreType: Boolean): Any
    open fun eSet_number_any(featureID: Number, newValue: Any)
    open fun eBaseStructuralFeatureID(derivedFeatureID: Number, baseClass: Function<*>): Number
    open fun eDerivedStructuralFeatureID_number_Function(baseFeatureID: Number, baseClass: Function<*>): Number

    companion object {
        var eStaticClass: Any
    }
}