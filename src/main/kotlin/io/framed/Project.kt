package io.framed

import io.framed.framework.model.ModelConnection
import io.framed.framework.model.ModelElement
import io.framed.framework.pictogram.Layer
import io.framed.framework.util.log
import io.framed.framework.view.dialog
import io.framed.framework.view.textView
import io.framed.model.*
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import kotlin.math.max

/**
 * Represents a serializable project. It contains the data-model, the connections and the layer-model.
 */
@Serializable
class Project(
    @Polymorphic
    val root: Package,
    val connections: Connections,
    val layer: Map<Long, Layer>
) {

    val name: String
        get() = root.name.lowercase()

    /**
     * Calculate the largest id of all model elements.
     * This id will be used as the next id for a new element.
     */
    fun maxId(): Long {
        return max(
            root.maxId(),
            max(
                connections.maxId(),
                layer.values.flatMap { it.ids }.maxOrNull() ?: 0L
            )
        )
    }

    fun toJSON(): String {
        return json.encodeToString(serializer(), this) + "\n"
    }

    companion object {

        private val messageModule = SerializersModule {
            // Register relations
            polymorphic(ModelConnection::class) {
                subclass(Relationship::class)
                subclass(Fulfillment::class)
                subclass(Composition::class)
                subclass(Aggregation::class)
                subclass(Inheritance::class)
                subclass(CreateRelationship::class)
                subclass(DestroyRelationship::class)
            }

            // Register model elements
            polymorphic(ModelElement::class) {
                subclass(Parameter::class)
                subclass(Attribute::class)
                subclass(Method::class)
                subclass(RoleType::class)
                subclass(Event::class)
                subclass(ReturnEvent::class)
                subclass(Class::class)
                subclass(Package::class)
                subclass(Compartment::class)
                subclass(Scene::class)
            }

            polymorphic(Attribute::class) {
                subclass(Attribute::class)
            }

            polymorphic(Method::class) {
                subclass(Method::class)
            }

            polymorphic(Parameter::class) {
                subclass(Parameter::class)
            }

            polymorphic(Package::class) {
                subclass(Package::class)
            }
        }

        private val json = Json {
            prettyPrint = true
            useArrayPolymorphism = true
            serializersModule = messageModule
        }

        fun fromJSON(content: String): Project? {
            return try {
                val file = json.decodeFromString(serializer(), content)
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
