package io.framed.framework

/**
 * Base model interface for easier access.
 *
 * @author lars
 */
interface ModelElement<M : ModelElement<M>> {
    val id: Long

    fun copy(): M
    fun getChildren(): List<ModelElement<*>> = listOf(this)

    companion object {
        var lastId: Long = 0
    }
}
