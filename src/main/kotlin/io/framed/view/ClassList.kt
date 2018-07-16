package io.framed.view

import org.w3c.dom.DOMTokenList

/**
 * @author lars
 */
class ClassList(
        private val list: DOMTokenList
) {
    operator fun plusAssign(clazz: String) {
        list.add(clazz)
    }

    operator fun minusAssign(clazz: String) {
        list.remove(clazz)
    }

    operator fun get(clazz: String): Boolean = list.contains(clazz)

    operator fun set(clazz: String, present: Boolean) =
            if (present) {
                list.add(clazz)
            } else {
                list.remove(clazz)
            }
}