package io.framed.framework

interface ModelConnection<M : ModelConnection<M>> : ModelElement<M> {

    /**
     * The connections source class.
     */
    var sourceId: Long

    /**
     * The connections target class.
     */
    var targetId: Long
}
