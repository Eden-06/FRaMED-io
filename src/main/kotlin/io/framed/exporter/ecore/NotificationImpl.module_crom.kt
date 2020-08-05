@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package io.framed.exporter.ecore

abstract external class NotificationImpl(eventType: Number, oldValue: Any, newValue: Any, position: Number, wasSet: Boolean = definedExternally) : NotificationChain, Notification {
    open var next: NotificationChain
    open var eventType: Number
    open var oldValue: Any
    open var newValue: Any
    open var position: Number
    open var primitiveType: Number
    open var add: (notification: Notification) -> Boolean
    open var dispatch: () -> Unit
    override fun getNotifier(): Any
    override fun getEventType(): Number
    override fun getNewValue(): Any

    companion object {
        var SET: Number
        var UNSET: Number
        var ADD: Number
        var REMOVE: Number
        var ADD_MANY: Number
        var REMOVE_MANY: Number
        var MOVE: Number
        var NO_INDEX: Number
        var IS_SET_CHANGE_INDEX: Number
        var NO_FEATURE_ID: Number
        var PRIMITIVE_TYPE_OBJECT: Number
    }
}