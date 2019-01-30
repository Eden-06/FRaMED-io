package io.framed.model

import kotlinx.serialization.*
import kotlinx.serialization.internal.StringDescriptor
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
    override val descriptor: SerialDescriptor = StringDescriptor

    override fun serialize(encoder: Encoder, obj: Date) {
        encoder.encodeString(obj.toString())
    }

    override fun deserialize(decoder: Decoder): Date {
        return Date(decoder.decodeString())
    }
}