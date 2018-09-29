package io.framed.model

import kotlinx.serialization.*
import kotlinx.serialization.internal.SerialClassDescImpl
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
object DateSerializer: KSerializer<Date> {
    override fun save(output: KOutput, obj: Date) {
        output.writeStringValue(obj.toUTCString())
    }

    override fun load(input: KInput): Date {
        return Date(input.readStringValue())
    }

    override val serialClassDesc: KSerialClassDesc = SerialClassDescImpl("kotlin.js.Date")

}