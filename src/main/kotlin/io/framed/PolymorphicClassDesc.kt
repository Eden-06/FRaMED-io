package io.framed

import kotlinx.serialization.SerialKind
import kotlinx.serialization.UnionKind
import kotlinx.serialization.internal.SerialClassDescImpl

object PolymorphicClassDesc : SerialClassDescImpl("kotlin.Any") {
    override val kind: SerialKind = UnionKind.POLYMORPHIC

    init {
        addElement("klass")
        addElement("value")
    }
}