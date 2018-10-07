package io.framed.framework

/**
 * Base model interface for easier access.
 *
 * @author lars
 */
interface ModelElement {
    val id: Long

    companion object {
        var lastId: Long = 0
    }
}
