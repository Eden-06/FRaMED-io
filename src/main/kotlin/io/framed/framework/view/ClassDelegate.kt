package io.framed.framework.view

import de.westermann.kobserve.Property
import de.westermann.kobserve.basic.FunctionAccessor
import de.westermann.kobserve.basic.property
import io.framed.framework.util.toDashCase
import kotlin.reflect.KProperty

/**
 * Delegate to easily set css classes as boolean attributes.
 *
 * @author lars
 */
class ClassDelegate(
        className: String? = null
) {


    private lateinit var container: View<*>
    private lateinit var paramName: String

    val classProperty = property(object : FunctionAccessor<Boolean> {
        override fun set(value: Boolean): Boolean {
            container.html.classList.toggle(paramName, value)
            return true
        }

        override fun get(): Boolean {
            return container.html.classList.contains(paramName)
        }

    })

    operator fun getValue(container: View<*>, property: KProperty<*>): Property<Boolean> {
        if (!this::container.isInitialized) {
            this.container = container
        }

        if (!this::paramName.isInitialized) {
            var name = property.name.toDashCase()
            if (name.endsWith("-property")) {
                name = name.replace("-property", "")
            }
            paramName = name
        }

        return classProperty
    }

    init {
        if (className != null) {
            this.paramName = className
        }
    }
}