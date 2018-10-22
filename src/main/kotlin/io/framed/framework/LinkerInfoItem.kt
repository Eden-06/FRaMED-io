package io.framed.framework

interface LinkerInfoItem {
    fun canCreate(container: Linker<*, *>): Boolean
    operator fun contains(linker: Linker<*, *>): Boolean
    val name: String
}