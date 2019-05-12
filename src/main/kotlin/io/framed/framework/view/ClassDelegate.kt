package io.framed.framework.view

import de.westermann.kobserve.Property
import de.westermann.kobserve.property.property
import io.framed.framework.util.toDashCase
import kotlin.reflect.KProperty

/**
 * Delegate to easily set css classes as boolean attributes.
 *
 * @author lars
 */
class ClassDelegate(
        className: String? = null,
        container: View<*>? = null
) {

    private lateinit var container: View<*>
    private lateinit var paramName: String
    private lateinit var classProperty: Property<Boolean>

    operator fun getValue(container: View<*>, property: KProperty<*>): Property<Boolean> {
        if (!this::container.isInitialized) {
            this.container = container
        }

        return getValue(Unit, property)
    }

    operator fun getValue(c: Any?, property: KProperty<*>): Property<Boolean> {
        if (!this::container.isInitialized) {
            throw IllegalArgumentException()
        }

        if (!this::paramName.isInitialized) {
            var name = property.name.toDashCase()
            if (name.endsWith("-property")) {
                name = name.replace("-property", "")
            }
            paramName = name
        }

        if (!this::classProperty.isInitialized) {
            classProperty = property(container.html.classList.contains(paramName))

            classProperty.onChange {
                container.html.classList.toggle(paramName, classProperty.value)
            }
        }

        return classProperty
    }

    init {
        if (className != null) {
            this.paramName = className
        }
        if (container != null) {
            this.container = container
        }
    }
}