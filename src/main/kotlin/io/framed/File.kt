package io.framed

import io.framed.framework.ModelElement
import io.framed.framework.pictogram.Layer
import io.framed.framework.util.log
import io.framed.framework.view.dialog
import io.framed.framework.view.textView
import io.framed.model.Connections
import io.framed.model.Package
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlin.math.max

@Serializable
class File(
        @Serializable(with = PolymorphicSerializer::class)
        val root: Package,
        val connections: Connections,
        val layer: Map<Long, Layer>
) {

    @Transient
    val name: String
        get() = root.name.toLowerCase()

    fun maxId(): Long {
        return max(
                root.maxId(),
                max(
                        connections.maxId(),
                        layer.values.flatMap { it.ids }.max() ?: 0L
                )
        )
    }

    fun toJSON(): String {
        return json.stringify(serializer(), this) + "\n"
    }

    companion object {
        private val json = Json(
                JsonConfiguration.Stable.copy(
                        strictMode = false,
                        prettyPrint = true
                )
        )

        fun fromJSON(content: String): File? {
            return try {
                val file = json.parse(serializer(), content)
                ModelElement.lastId = file.maxId() + 1

                file
            } catch (e: Exception) {
                println("Error while opening file!")
                e.log()
                dialog {
                    title = "Error while opening file"
                    contentView.textView("The selected file is invalid and cannot be parsed.")
                    closable = true
                    addButton("Abort", true) {}
                }.open()
                null
            }
        }

        fun empty(): File {
            val file = File(Package(), Connections(), emptyMap())
            ModelElement.lastId = file.root.maxId() + 1

            return file
        }
    }
}
