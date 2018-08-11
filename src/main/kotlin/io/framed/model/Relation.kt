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
    var type: Type = Type.ASSOCIATION

    /**
     * Possible types
     */
    enum class Type {
        INHERITANCE,
        ASSOCIATION,
        AGGREGATION
    }

    companion object {
        val PRESETS = listOf(
                "0",
                "1",
                "*",
                "0..0",
                "0..1",
                "0..*",
                "1..1",
                "1..*",
                "m..n"
        )
    }
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