package io.framed.framework

import io.framed.framework.pictogram.ConnectionInfo

interface LinkerInfoConnection {
    val info: ConnectionInfo

    fun canStart(source: Linker<*, *>): Boolean
    fun canCreate(source: Linker<*, *>, target: Linker<*, *>): Boolean
    
    fun isLinkerFor(element: ModelConnection<*>): Boolean
    fun isLinkerFor(linker: Linker<*, *>): Boolean

    fun createModel(source: Long, target: Long): ModelConnection<*>
    fun createLinker(model: ModelConnection<*>, connectionManager: ConnectionManager): ConnectionLinker<*>
}