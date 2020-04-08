package io.framed.model

import kotlinx.serialization.*
import kotlin.js.Date

@Serializable
class Metadata {
    @Serializable(with = DateSerializer::class)
    var creationDate: Date = Date()

    @Serializable(with = DateSerializer::class)
    var modifiedDate: Date = Date()

    var author: String = ""
}

@Serializer(forClass = Date::class)
object DateSerializer : KSerializer<Date> {
    override val descriptor: SerialDescriptor = PrimitiveDescriptor("DateSerializer", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Date) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): Date {
        return Date(decoder.decodeString())
    }
}