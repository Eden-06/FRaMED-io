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

external open class EGenericTypeBase : BasicEObjectImpl, EGenericType {
    open var _eUpperBound: Any
    open var _eTypeArguments: Any
    open var _eLowerBound: Any
    open var _eTypeParameter: Any
    open var _eClassifier: Any
    override fun isInstance(obj: Any): Boolean
    override fun eStaticClass(): EClass
    open fun basicSetETypeParameter(newobj: ETypeParameter, msgs: NotificationChain): NotificationChain
    open fun basicSetELowerBound(newobj: EGenericType, msgs: NotificationChain): NotificationChain
    open fun basicSetEClassifier(newobj: EClassifier, msgs: NotificationChain): NotificationChain
    open fun basicSetEUpperBound(newobj: EGenericType, msgs: NotificationChain): NotificationChain
    override fun eGet_number_boolean_boolean(featureID: Number, resolve: Boolean, coreType: Boolean): Any
    override fun eSet_number_any(featureID: Number, newValue: Any)

    companion object {
        var eStaticClass: EClass
    }
}