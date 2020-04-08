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
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.modules.SerializersModule
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
        private val messageModule = SerializersModule {
            // Register relations
            polymorphic(ModelConnection::class) {
                Relationship::class with Relationship.serializer()
                Fulfillment::class with Fulfillment.serializer()
                Composition::class with Composition.serializer()
                Aggregation::class with Aggregation.serializer()
                Inheritance::class with Inheritance.serializer()
                CreateRelationship::class with CreateRelationship.serializer()
                DestroyRelationship::class with DestroyRelationship.serializer()
            }

            polymorphic(ModelElement::class) {
                // Register model elements
                Parameter::class with Parameter.serializer()
                Attribute::class with Attribute.serializer()
                Method::class with Method.serializer()
                RoleType::class with RoleType.serializer()
                Event::class with Event.serializer()
                ReturnEvent::class with ReturnEvent.serializer()
                Class::class with Class.serializer()
                Package::class with Package.serializer()
                Compartment::class with Compartment.serializer()
                Scene::class with Scene.serializer()
            }

            polymorphic(Attribute::class) {
                Attribute::class with Attribute.serializer()
            }
            polymorphic(Method::class) {
                Method::class with Method.serializer()
            }
            polymorphic(Parameter::class) {
                Parameter::class with Parameter.serializer()
            }
            polymorphic(Package::class) {
                Package::class with Package.serializer()
            }
        }
        private val json = Json(
                JsonConfiguration.Stable.copy(
                        prettyPrint = true,
                        useArrayPolymorphism = true
                ),
                context = messageModule
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
