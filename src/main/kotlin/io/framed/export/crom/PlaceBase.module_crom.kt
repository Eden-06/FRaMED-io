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

external open class PlaceBase : BasicEObjectImpl, Place {
    open var _lower: Any
    open var _upper: Any
    open var _holder: Any
    open fun eStaticClass(): EClass
    open fun basicSetHolder(newobj: RoleType, msgs: NotificationChain): NotificationChain
    open fun eGet_number_boolean_boolean(featureID: Number, resolve: Boolean, coreType: Boolean): Any
    open fun eSet_number_any(featureID: Number, newValue: Any)

    companion object {
        var eStaticClass: Any
    }
}