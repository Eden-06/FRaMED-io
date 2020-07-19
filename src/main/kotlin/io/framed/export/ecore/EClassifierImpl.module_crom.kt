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

external open class EClassifierImpl : EClassifierBase {
    open var metaObjectID: Number
    open var setClassifierID: (id: Number) -> Unit
    open var generatedInstanceClassName: String
    open var setGeneratedInstanceClass: (isGenerated: Boolean) -> Unit
    open var basicSetInstanceTypeName: (newInstanceTypeName: String) -> Unit
    open var _instanceClass: Any
    override fun getClassifierID(): Number
    open var computeClassifierID: Any
}