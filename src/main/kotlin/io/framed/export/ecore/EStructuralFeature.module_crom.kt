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

external interface EStructuralFeature : ETypedElement {
    var changeable: Boolean
    var volatile: Boolean
    var transient: Boolean
    var defaultValueLiteral: String
    var defaultValue: Any
    var unsettable: Boolean
    var derived: Boolean
    var eContainingClass: EClass
    fun getContainerClass(): Function<*>
    fun getFeatureID(): Number
}