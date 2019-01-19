package io.framed.framework

interface LinkerInfoItem {
    fun canCreateIn(container: ModelElement<*>): Boolean
    fun isLinkerOfType(element: ModelElement<*>): Boolean

    val name: String
}