package io.framed.model

import io.framed.framework.ModelElement
import kotlinx.serialization.Serializable

/**
 * Model class for an uml connection.
 *
 * @author lars
 */
@Serializable
class Relation(

        /**
         * The connections source class.
         */
        var sourceId: Long,

        /**
         * The connections target class.
         */
        var targetId: Long
) : ModelElement {

    override val id: Long = ModelElement.lastId++

    /**
     * Name of this connection.
     */
    var name: String = ""

    /**
     * Cardinality for the source side of this connection.
     */
    var sourceCardinality: String = ""

    /**
     * Cardinality for the target side of this connection.
     */
    var targetCardinality: String = ""

    /**
     * Type of this connection
     */
    var type: RelationType = RelationType.ASSOCIATION
}

enum class RelationMultiplicity(val value: String) {
    NEVER("0"),
    ONCE("1"),
    MANY("*"),

    NEVER_TO_NEVER("0..0"),
    NEVER_TO_ONCE("0..1"),
    NEVER_TO_MANY("0..*"),

    ONCE_TO_ONCE("1..1"),
    ONCE_TO_MANY("1..*"),

    MANY_TO_MANY("*..*");

    override fun toString(): String = name.toLowerCase().replace("_", " ")

    companion object {
        val STRING_VALUES = values().map { it.value }

        fun parse(str: String): RelationMultiplicity? = values().find {
            it.value == str || it.toString() == str
        }
    }
}

enum class RelationType {
    INHERITANCE,
    ASSOCIATION,
    AGGREGATION;

    val value: String = name.first() + name.drop(1).toLowerCase()
}

/**
 * Creates a new connection within the current model.
 *
 * @param source Relations source.
 * @param target Relations target.
 * @param name Optional name for this connection.
 * @param init Optional builder callback for the new connection.
 *
 * @return The new connection.
 */
fun Container.relation(source: Class, target: Class, name: String = "", init: Relation.() -> Unit = {}): Relation {
    val relation = Relation(source.id, target.id)
    relation.name = name
    relations += relation
    init(relation)
    return relation
}