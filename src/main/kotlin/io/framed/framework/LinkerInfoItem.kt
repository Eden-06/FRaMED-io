package io.framed.framework

interface LinkerInfoItem {
    fun canCreateIn(container: ModelElement<*>): Boolean

    fun isLinkerFor(element: ModelElement<*>): Boolean
    fun isLinkerFor(linker: Linker<*, *>): Boolean

    fun createModel(): ModelElement<*>
    fun createLinker(model: ModelElement<*>, parent: Linker<*, *>, connectionManager: ConnectionManager?): Linker<*, *>

    val name: String
}
