package io.framed.linker

import de.westermann.kobserve.property.FunctionAccessor
import de.westermann.kobserve.property.constProperty
import de.westermann.kobserve.property.property
import de.westermann.kobserve.property.validate
import io.framed.framework.*
import io.framed.framework.linker.*
import io.framed.framework.model.ModelElement
import io.framed.framework.pictogram.*
import io.framed.framework.util.*
import io.framed.framework.view.*
import io.framed.model.*

/**
 * @author lars
 */
class ClassLinker(
        override val model: Class,
        override val parent: ModelLinker<*, *, *>
) : PreviewLinker<Class, BoxShape, BoxShape> {

    override val nameProperty = property(model::name)
            .validate(RegexValidator.IDENTIFIER::validate)
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
                    width = box(Border.DEFAULT_WIDTH, 0.0, 0.0, 0.0)
                    color = box(color(0, 0, 0, 0.3))
                }
                padding = box(8.0)
            }
        }
        methods.view = boxShape {
            style {
                border {
                    style = Border.BorderStyle.SOLID
                    width = box(Border.DEFAULT_WIDTH, 0.0, 0.0, 0.0)
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
                width = box(Border.DEFAULT_WIDTH)
                color = box(color(0, 0, 0, 0.3))
            }
        }

        resizeable = true
    }

    override val preview: BoxShape = boxShape(BoxShape.Position.HORIZONTAL) {
        iconShape(constProperty(info.icon))
        textShape(nameProperty)

        style {
            padding = box(0.0, 0.0, 0.0, 30.0)
        }

        ignoreLabels = true
    }

    /**
     * Specify if this view should be displayed in the complete view or only the name.
     */
    private val isCompleteViewStringProperty = pictogram.data("complete-view")
    private val isCompleteViewProperty = property(object : FunctionAccessor<Boolean> {
        val default: Boolean
            get() = true

        override fun set(value: Boolean): Boolean {
            if (value == default) {
                isCompleteViewStringProperty.value = null
            } else {
                isCompleteViewStringProperty.value = value.toString()
            }
            return true
        }

        override fun get(): Boolean {
            return isCompleteViewStringProperty.value?.toBoolean() ?: default
        }
    }, isCompleteViewStringProperty)

    private lateinit var sidebarViewGroup: SidebarGroup

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
            button("Auto size") {
                updateSize(true)
            }
            checkBox("Complete view", isCompleteViewProperty, CheckBox.Type.SWITCH)
        }

        advanced(pictogram)
    }

    override fun Sidebar.onOpen(event: SidebarEvent) {
        sidebarViewGroup.display = event.target == pictogram
    }

    override val contextMenu = defaultContextMenu()

    override fun add(model: ModelElement): ShapeLinker<*, *> {
        val linker = LinkerManager.createLinker<ShapeLinker<*, *>>(model, this)
        when (linker) {
            is AttributeLinker -> attributes.add(linker)
            is MethodLinker -> methods.add(linker)
            else -> super.add(model)
        }
        linker.focus(pictogram)
        return linker
    }

    override fun remove(linker: ShapeLinker<*, *>) {
        when (linker) {
            is AttributeLinker -> attributes.remove(linker)
            is MethodLinker -> methods.remove(linker)
            else -> super.remove(linker)
        }
    }

    /**
     * Update the sidebar attribute overview. Add/remove missing/old items.
     */
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

    /**
     * Update the sidebar method overview. Add/remove missing/old items.
     */
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
        attributes.view.visibleProperty.bind(isCompleteViewProperty)
        methods.view.visibleProperty.bind(isCompleteViewProperty)

        pictogram.onAutoSize {
            updateSize()
        }

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

        override val info = ElementInfo("Class", FramedIcon.CLASS)

        override fun canCreateIn(container: ModelElement): Boolean {
            return container is Package
        }

        override fun isLinkerFor(element: ModelElement): Boolean = element is Class
        override fun isLinkerFor(linker: Linker<*, *>): Boolean = linker is ClassLinker

        override fun createModel(): ModelElement = Class()
        override fun createLinker(model: ModelElement, parent: Linker<*, *>, connectionManager: ConnectionManager?): Linker<*, *> {
            if (model is Class && (parent is ModelLinker<*, *, *>)) {
                return ClassLinker(model, parent)
            } else throw IllegalArgumentException("Cannot create ${info.name} linker for model element ${model::class}")
        }
    }
}
