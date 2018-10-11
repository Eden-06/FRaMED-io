package io.framed.framework

interface ModelConnection : ModelElement {

    /**
     * The connections source class.
     */
    var sourceId: Long

    /**
     * The connections target class.
     */
    var targetId: Long
}
