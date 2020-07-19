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

external open class EStructuralFeatureImpl : EStructuralFeatureBase {
    open var featureID: Number
    open fun setFeatureID(id: Number)
    override fun getFeatureID(): Number
    open var containerClass_: Any
    open var setContainerClass: (f: Function<*>) -> Unit
    override fun getContainerClass(): Function<*>
}