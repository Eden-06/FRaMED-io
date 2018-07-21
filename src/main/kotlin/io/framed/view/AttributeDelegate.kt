package io.framed.view

import org.w3c.dom.Element
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

/**
 * Delegate to easily access html attributes.
 *
 * @author lars
 */
class AttributeDelegate<T : Any>(
        private val type: KClass<T>,
        private val default: T,
        private val paramName: String? = null,
        private val element: Element? = null
) {

    @Suppress("UNCHECKED_CAST")
    private fun getAttribute(element: Element, property: KProperty<*>): T =
            element.getAttribute(getParamName(property)).let {
                when (type) {
                    Int::class -> it?.toIntOrNull()
                    Double::class -> it?.toDoubleOrNull()
                    Boolean::class -> it?.toBoolean()
                    String::class -> it
                    else -> null
                } as T?
            } ?: default

    private fun getParamName(property: KProperty<*>): String = paramName ?: property.name.toLowerCase()

    operator fun getValue(container: Any, property: KProperty<*>): T = element?.let {
        getAttribute(it, property)
    } ?: default

    operator fun getValue(container: View<*>, property: KProperty<*>): T = getAttribute(container.html, property)

    operator fun setValue(container: Any, property: KProperty<*>, value: T) {
        element?.setAttribute(getParamName(property), value.toString())
    }

    operator fun setValue(container: View<*>, property: KProperty<*>, value: T) {
        container.html.setAttribute(getParamName(property), value.toString())
    }
}