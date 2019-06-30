package io.framed

import kotlinx.serialization.KSerializer
import kotlinx.serialization.internal.ArrayListSerializer

/**
 * A serializer for polymorphic [List]'s relying on the [PolymorphicSerializer] (supporting multiplatform).
 */
object PolymorphicListSerializer : KSerializer<List<Any>> by ArrayListSerializer(PolymorphicSerializer)