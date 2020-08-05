package io.framed.model

import io.framed.exporter.visitor.Visitor
import io.framed.framework.model.ModelConnection
import kotlinx.serialization.Serializable

/**
 * io.framed.exporter.crom.Model class for an BROS create relation
 *
 * @author Sebastian
 */
@Serializable
class CreateRelationship() : ModelConnection() {

    constructor(sourceId: Long, targetId: Long): this() {
        this.sourceId = sourceId
        this.targetId = targetId
    }

    /**
     * Name of this connection.
     */
    var name: String = ""

    override fun copy() = CreateRelationship().also { new ->
        new.name = name
    }

    override fun <T> acceptVisitor(visitor: Visitor<T>) {
        visitor.visit(this)
    }
}
