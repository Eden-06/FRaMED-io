package io.framed.model

import io.framed.framework.ModelConnection
import io.framed.framework.ModelElement
import kotlinx.serialization.Serializable

/**
 * Model class for an uml connection.
 *
 * @author lars
 */
@Serializable
class Association(

        /**
         * The connections source class.
         */
        override var sourceId: Long,

        /**
         * The connections target class.
         */
        override var targetId: Long
) : ModelConnection<Association> {
    
    constructor(sourceId: Long, targetId: Long, init: (Association) -> Unit) : this(sourceId, targetId) {
        init(this)
    }

    override val id: Long = ModelElement.lastId++

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
    
    override fun copy() = Association(sourceId, targetId) { new ->
        new.name = name
        new.sourceCardinality = sourceCardinality
        new.targetCardinality = targetCardinality
    }
}
