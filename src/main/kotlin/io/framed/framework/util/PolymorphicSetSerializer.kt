package io.framed

import kotlinx.serialization.KSerializer
import kotlinx.serialization.internal.HashSetSerializer

/**
 * A serializer for polymorphic [Set]'s relying on the [PolymorphicSerializer] (supporting multiplatform).
 */
object PolymorphicSetSerializer : KSerializer<Set<Any>> by HashSetSerializer(PolymorphicSerializer)