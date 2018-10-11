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
class Inheritance(

        /**
         * The connections source class.
         */
        override var sourceId: Long,

        /**
         * The connections target class.
         */
        override var targetId: Long
) : ModelConnection {

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
}
