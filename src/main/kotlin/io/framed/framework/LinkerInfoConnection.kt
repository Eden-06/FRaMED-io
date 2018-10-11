package io.framed.framework

import io.framed.framework.pictogram.ConnectionInfo

interface LinkerInfoConnection {
    val info: ConnectionInfo

    fun canStart(source: Linker<*, *>): Boolean
    fun canCreate(source: Linker<*, *>, target: Linker<*, *>): Boolean
}