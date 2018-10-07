package io.framed.framework.view

import org.w3c.dom.DOMTokenList

/**
 * Represents the css classes of an html element.
 *
 * @author lars
 */
class ClassList(
        private val list: DOMTokenList
) {

    /**
     * Add css class.
     */
    operator fun plusAssign(clazz: String) {
        list.add(clazz)
    }

    /**
     * Remove css class.
     */
    operator fun minusAssign(clazz: String) {
        list.remove(clazz)
    }

    /**
     * Check if css class exits.
     */
    operator fun get(clazz: String): Boolean = list.contains(clazz)

    /**
     * Set css class present.
     */
    operator fun set(clazz: String, present: Boolean) =
            if (present) {
                list.add(clazz)
            } else {
                list.remove(clazz)
            }

    /**
     * Toggle css class.
     */
    fun toggle(clazz: String) = list.toggle(clazz)

}