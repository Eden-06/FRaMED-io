package io.framed.view

import io.framed.toDashCase
import org.w3c.dom.Element
import kotlin.reflect.KProperty

/**
 * Delegate to easily set css classes as boolean attributes.
 *
 * @author lars
 */
class ClassDelegate(
        private val paramName: String? = null,
        private val element: Element? = null
) {

    @Suppress("UNCHECKED_CAST")
    private fun getAttribute(element: Element, property: KProperty<*>) = element.classList.contains(getParamName(property).toDashCase())

    private fun getParamName(property: KProperty<*>): String = paramName ?: property.name.toDashCase()

    operator fun getValue(container: Any, property: KProperty<*>) = element?.let {
        getAttribute(it, property)
    } ?: false

    operator fun getValue(container: View<*>, property: KProperty<*>) = getAttribute(container.html, property)

    operator fun setValue(container: Any, property: KProperty<*>, value: Boolean) {
        element?.classList?.toggle(getParamName(property), value)
    }

    operator fun setValue(container: View<*>, property: KProperty<*>, value: Boolean) {
        container.html.classList.toggle(getParamName(property), value)
    }
}