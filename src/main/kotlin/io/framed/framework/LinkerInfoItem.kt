package io.framed.framework

interface LinkerInfoItem {
    fun canCreate(container: Linker<*, *>): Boolean

    val name: String
}