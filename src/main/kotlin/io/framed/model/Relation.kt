package io.framed.model

/**
 * Model class for an uml relation.
 *
 * @author lars
 */
class Relation(

        /**
         * The relations source class.
         */
        var source: Class,

        /**
         * The relations target class.
         */
        var target: Class
) {

    /**
     * Name of this relation.
     */
    var name: String = ""

    /**
     * Cardinality for the source side of this relation.
     */
    var sourceCardinality: String = ""

    /**
     * Cardinality for the target side of this relation.
     */
    var targetCardinality: String = ""

    /**
     * Type of this relation
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

    override fun toString() = name.first() + name.drop(1).toLowerCase()
}

/**
 * Creates a new relation within the current container.
 *
 * @param source Relations source.
 * @param target Relations target.
 * @param name Optional name for this relation.
 * @param init Optional builder callback for the new relation.
 *
 * @return The new relation.
 */
fun Container.relation(source: Class, target: Class, name: String = "", init: Relation.() -> Unit = {}): Relation {
    val relation = Relation(source, target)
    relation.name = name
    relations += relation
    init(relation)
    return relation
}