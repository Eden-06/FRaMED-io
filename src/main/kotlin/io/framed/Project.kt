package io.framed

import io.framed.framework.model.ModelElement
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

/**
 * Represents a serializable project. It contains the data-model, the connections and the layer-model.
 */
@Serializable
class Project(
        @Serializable(with = PolymorphicSerializer::class)
        val root: Package,
        val connections: Connections,
        val layer: Map<Long, Layer>
) {

    @Transient
    val name: String
        get() = root.name.toLowerCase()

    /**
     * Calculate the largest id of all model elements.
     * This id will be used as the next id for a new element.
     */
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

        fun fromJSON(content: String): Project? {
            return try {
                val file = json.parse(serializer(), content)
                ModelElement.lastId = file.maxId() + 1

                file
            } catch (e: Exception) {
                println("Error while opening project!")
                e.log()
                dialog {
                    title = "Error while opening project"
                    contentView.textView("The selected project is invalid and cannot be parsed.")
                    closable = true
                    addButton("Abort", true) {}
                }.open()
                null
            }
        }

        /**
         * Create an empty project object that only contains a package.
         */
        fun empty(): Project {
            val file = Project(Package(), Connections(), emptyMap())
            ModelElement.lastId = file.root.maxId() + 1

            return file
        }
    }
}
