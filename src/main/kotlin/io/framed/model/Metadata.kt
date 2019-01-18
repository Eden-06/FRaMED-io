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

    override fun serialize(output: Encoder, obj: Date) {
        output.encodeString(obj.toString())
    }

    override fun deserialize(input: Decoder): Date {
        return Date(input.decodeString())
    }
}