package io.framed.framework

interface LinkerInfoRelation {
    fun canStart(source: Linker<*, *>): Boolean
    fun canCreate(source: Linker<*, *>, target: Linker<*, *>): Boolean
}