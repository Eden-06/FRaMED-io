package io.framed.framework.view

import de.westermann.kobserve.property.join
import de.westermann.kobserve.property.readOnly
import io.framed.framework.pictogram.BoxShape
import io.framed.framework.pictogram.Shape
import org.w3c.dom.HTMLDivElement
import kotlin.math.roundToInt

/**
 * @author lars
 */
class Sidebar : ViewCollection<View<*>, HTMLDivElement>("div") {

    fun open() {
        Application.propertyBar.content.clear()
        Application.propertyBar.content += this
    }

    private var groups: Map<String, SidebarGroup> = emptyMap()

    fun group(name: String, init: SidebarGroup.() -> Unit): SidebarGroup {
        val group = groups[name] ?: run {
            SidebarGroup(name).also(this::append).also {
                groups += name to it
            }
        }
        group.init()
        return group
    }

    fun title(text: String): TextView {
        val view = TextView().also {
            it.classes += "header"
        }
        view.text = text
        append(ListView().also {
            it += view
        })
        return view
    }
}

/**
 * Create a new sidebar.
 *
 * @param init Builder for the new sidebar.
 *
 * @return The new sidebar.
 */
fun sidebar(init: Sidebar.() -> Unit): Sidebar = Sidebar().also(init)

fun Sidebar.advanced(pictogram: Shape) {
    group("Advanced") {
        input("Position", pictogram.leftProperty.join(pictogram.topProperty) { left, top ->
            "x=${left.roundToInt()}, y=${top.roundToInt()}"
        })
        input("Size", pictogram.widthProperty.join(pictogram.heightProperty) { width, height ->
            "width=${width.roundToInt()}, height=${height.roundToInt()}"
        })
        checkBox("Autosize", pictogram.autosizeProperty.readOnly(), CheckBox.Type.SWITCH)
        button("Log pictogram") {
            fun log(shape: Shape): String {
                val type = shape::class.simpleName ?: "Unknown"
                val position = (shape as? BoxShape)?.position?.name?.first()?.let { "[$it]" } ?: ""
                return buildString {
                    append("$type$position(${shape.id?.toInt()}: ${shape.width} x ${shape.height})")
                    if (shape is BoxShape) {
                        for (child in shape.shapes) {
                            append("\n")
                            append(log(child).prependIndent("| "))
                        }
                    }
                }
            }

            console.log(log(pictogram))
        }
    }
}
