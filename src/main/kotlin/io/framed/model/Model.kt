package io.framed.model

/**
 * Base model interface for easier access.
 *
 * @author lars
 */
interface Model {
    val metadata: Metadata
    val id: Long

    companion object {
        var lastId: Long = 0
    }
}
