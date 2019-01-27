package io.framed.linker

import de.westermann.kobserve.basic.join
import de.westermann.kobserve.basic.mapBinding
import de.westermann.kobserve.basic.property
import de.westermann.kobserve.basic.validate
import io.framed.framework.*
import io.framed.framework.pictogram.*
import io.framed.framework.util.LinkerShapeBox
import io.framed.framework.util.RegexValidator
import io.framed.framework.util.shapeBox
import io.framed.framework.util.trackHistory
import io.framed.framework.view.*
import io.framed.model.*
import kotlin.math.roundToInt

/**
 * @author lars
 */
class ClassLinker(
        override val model: Class,
        override val parent: ModelLinker<*, *, *>
) : PreviewLinker<Class, BoxShape, TextShape> {

    override val nameProperty = property(model::name)
            .validate(RegexValidator("[a-zA-Z]([a-zA-Z0-9 ])*".toRegex())::validate)
            .trackHistory()
    override var name by nameProperty

    val attributes: LinkerShapeBox<Attribute, AttributeLinker> = shapeBox(model::attributes)
    val methods: LinkerShapeBox<Method, MethodLinker> = shapeBox(model::methods)

    override val subTypes: Set<String>
        get() = (attributes.linkers.flatMap { it.subTypes } + methods.linkers.flatMap { it.subTypes }).toSet() + model.name

    override val pictogram = boxShape {
        boxShape {
            textShape(nameProperty)
            style {
                padding = box(8.0)
            }
        }
        attributes.view = boxShape {
            style {
                border {
                    style = Border.BorderStyle.SOLID
                    width = box(1.0, 0.0, 0.0, 0.0)
                    color = box(color(0, 0, 0, 0.3))
                }
                padding = box(8.0)
            }
        }
        methods.view = boxShape {
            style {
                border {
                    style = Border.BorderStyle.SOLID
                    width = box(1.0, 0.0, 0.0, 0.0)
                    color = box(color(0, 0, 0, 0.3))
                }
                padding = box(8.0)
            }
        }

        style {
            background = linearGradient("to bottom") {
                add(color("#f9f9f9"), 0.0)
                add(color("#eaeaea"), 1.0)
            }
            border {
                style = Border.BorderStyle.SOLID
                width = box(1.0)
                color = box(color(0, 0, 0, 0.3))
            }
        }

        resizeable = true
    }

    override val listPreview: TextShape = textShape(nameProperty)

    override val flatPreview = boxShape {
        textShape(nameProperty)

        style {
            background = linearGradient("to bottom") {
                add(color("#f9f9f9"), 0.0)
                add(color("#eaeaea"), 1.0)
            }
            border {
                style = Border.BorderStyle.SOLID
                width = box(1.0)
                color = box(color(0, 0, 0, 0.3))
            }
            padding = box(10.0)
        }

        resizeable = true
    }

    private lateinit var sidebarViewGroup: SidebarGroup
    private lateinit var sidebarFlatViewGroup: SidebarGroup

    private lateinit var sidebarAttributes: SidebarGroup
    private lateinit var sidebarAttributesAdd: ListView
    private val sidebarAttributesList: MutableList<SidebarEntry<Attribute>> = mutableListOf()
    private lateinit var sidebarMethods: SidebarGroup
    private lateinit var sidebarMethodsAdd: ListView
    private val sidebarMethodsList: MutableList<SidebarEntry<Method>> = mutableListOf()

    override val sidebar = sidebar {
        title("Class")
        group("General") {
            input("Name", nameProperty)
        }
        sidebarAttributes = group("Attributes") {
            collapse()
            sidebarAttributesAdd = custom {
                iconView(MaterialIcon.ADD)
                textView("Add attribute")
                onClick {
                    attributes += AttributeLinker(Attribute(), this@ClassLinker)
                }
            }
        }
        sidebarMethods = group("Methods") {
            collapse()
            sidebarMethodsAdd = custom {
                iconView(MaterialIcon.ADD)
                textView("Add method")
                onClick {
                    methods += MethodLinker(Method(), this@ClassLinker)
                }
            }
        }
        sidebarViewGroup = group("Layout") {
            input("Position", pictogram.leftProperty.join(pictogram.topProperty) { left, top ->
                "x=${left.roundToInt()}, y=${top.roundToInt()}"
            })
            input("Size", pictogram.widthProperty.join(pictogram.heightProperty) { width, height ->
                "width=${width.roundToInt()}, height=${height.roundToInt()}"
            })
            checkBox("Autosize", pictogram.autosizeProperty, CheckBox.Type.SWITCH)
        }
        sidebarFlatViewGroup = group("Preview layout") {
            input("Position", flatPreview.leftProperty.join(flatPreview.topProperty) { left, top ->
                "x=${left.roundToInt()}, y=${top.roundToInt()}"
            })
            input("Size", flatPreview.widthProperty.join(flatPreview.heightProperty) { width, height ->
                "width=${width.roundToInt()}, height=${height.roundToInt()}"
            })
            checkBox("Autosize", flatPreview.autosizeProperty, CheckBox.Type.SWITCH)
        }
    }

    override fun Sidebar.onOpen(event: SidebarEvent) {
        sidebarViewGroup.display = event.target == pictogram
        sidebarFlatViewGroup.display = event.target == flatPreview
    }

    override val contextMenu = contextMenu {
        titleProperty.bind(nameProperty.mapBinding { "Class: $it" })
        addItem(MaterialIcon.ADD, "Add attribute") { event ->
            attributes += AttributeLinker(Attribute(), this@ClassLinker).also { linker ->
                linker.focus(event.target)
            }
        }
        addItem(MaterialIcon.ADD, "Add method") { event ->
            methods += MethodLinker(Method(), this@ClassLinker).also { linker ->
                linker.focus(event.target)
            }
        }
        addItem(MaterialIcon.DELETE, "Delete") {
            delete()
        }
    }

    override fun remove(linker: ShapeLinker<*, *>) {
        when (linker) {
            is AttributeLinker -> attributes.remove(linker)
            is MethodLinker -> methods.remove(linker)
            else -> super.remove(linker)
        }
    }

    private fun updateSidebarAttributes() {
        while (sidebarAttributesList.size > attributes.linkers.size) {
            val last = sidebarAttributesList.last()
            last.remove()
            sidebarAttributesList -= last
        }

        for (i in 0 until sidebarAttributesList.size) {
            sidebarAttributesList[i].bind(attributes.linkers[i])
        }

        for (i in sidebarAttributesList.size until attributes.linkers.size) {
            sidebarAttributesList += SidebarEntry(sidebarAttributes, attributes.linkers[i])
        }

        sidebarAttributes.toForeground(sidebarAttributesAdd)
    }

    private fun updateSidebarMethods() {
        while (sidebarMethodsList.size > methods.linkers.size) {
            val last = sidebarMethodsList.last()
            last.remove()
            sidebarMethodsList -= last
        }

        for (i in 0 until sidebarMethodsList.size) {
            sidebarMethodsList[i].bind(methods.linkers[i])
        }

        for (i in sidebarMethodsList.size until methods.linkers.size) {
            sidebarMethodsList += SidebarEntry(sidebarMethods, methods.linkers[i])
        }

        sidebarMethods.toForeground(sidebarMethodsAdd)
    }

    init {
        model.attributes.forEach { attributes += AttributeLinker(it, this) }
        model.methods.forEach { methods += MethodLinker(it, this) }

        updateSidebarAttributes()
        updateSidebarMethods()

        attributes.view.onAdd {
            updateSidebarAttributes()
        }
        attributes.view.onRemove {
            updateSidebarAttributes()
        }
        methods.view.onAdd {
            updateSidebarMethods()
        }
        methods.view.onRemove {
            updateSidebarMethods()
        }

        LinkerManager.setup(this)
    }

    companion object : LinkerInfoItem {
        override fun canCreateIn(container: ModelElement<*>): Boolean {
            return container is Package || container is Compartment
        }

        override fun isLinkerOfType(element: ModelElement<*>): Boolean = element is Class

        override val name: String = "Class"
    }
}