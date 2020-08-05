package io.framed.model

import io.framed.exporter.visitor.Visitor
import io.framed.framework.model.ModelConnection
import kotlinx.serialization.Serializable

/**
 * io.framed.exporter.crom.Model class for an uml connection.
 *
 * @author lars
 */
@Serializable
class Relationship() : ModelConnection() {

    constructor(sourceId: Long, targetId: Long): this() {
        this.sourceId = sourceId
        this.targetId = targetId
    }

    /**
     * Name of this connection.
     */
    var name: String = ""

    /**
     * Cardinality for the source side of this connection.
     */
    var sourceCardinality: String = "*"

    /**
     * Cardinality for the target side of this connection.
     */
    var targetCardinality: String = "*"

    override fun copy() = Relationship().also { new ->
        new.name = name
        new.sourceCardinality = sourceCardinality
        new.targetCardinality = targetCardinality
    }

    override fun <T> acceptVisitor(visitor: Visitor<T>) {
        visitor.visit(this)
    }
}
