@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package io.framed.export.crom

import io.framed.exporter.crom.ModelBase

abstract external class ModelImpl : ModelBase {
    companion object {
        var allInstances_: Any
    }
}