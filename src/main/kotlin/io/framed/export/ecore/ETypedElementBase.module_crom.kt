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

external open class ETypedElementBase : ENamedElementImpl, ETypedElement {
    open var _ordered: Any
    open var _unique: Any
    open var _lowerBound: Any
    open var _upperBound: Any
    open var _eType: Any
    open var _eGenericType: Any
    override fun eStaticClass(): EClass
    open fun basicSetEGenericType(newobj: EGenericType, msgs: NotificationChain): NotificationChain
    open fun basicSetEType(newobj: EClassifier, msgs: NotificationChain): NotificationChain
    override fun eGet_number_boolean_boolean(featureID: Number, resolve: Boolean, coreType: Boolean): Any
    override fun eSet_number_any(featureID: Number, newValue: Any)

    companion object {
        var eStaticClass: EClass
    }
}