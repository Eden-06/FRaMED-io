package io.framed.linker

import de.westermann.kobserve.Property
import de.westermann.kobserve.event.EventListener
import io.framed.framework.model.ModelElement
import io.framed.framework.linker.ShapeLinker
import io.framed.framework.pictogram.TextShape
import io.framed.framework.view.*


class SidebarEntry<T : ModelElement<T>>(private val sidebarGroup: SidebarGroup, private var param: ShapeLinker<T, TextShape>) {

    private lateinit var nameInput: InputView
    private val listView: ListView

    private var reference: EventListener<*>? = null

    fun bind(parameter: ShapeLinker<T, TextShape>) {
        if (param !== parameter) {
            reference?.detach()

            param = parameter
            nameInput.value = param.pictogram.property.value

            reference = param.pictogram.property.onChange.reference {
                val value = param.pictogram.property.value
                if (value.trim() != nameInput.value.trim()) {
                    nameInput.value = value
                }
            }
        }
    }

    private fun deleteParameter() {
        param.delete()
        remove()
    }

    fun remove() {
        sidebarGroup.remove(listView)
    }

    init {
        listView = sidebarGroup.custom {
            tableView {
                row {
                    cellBox {
                        nameInput = inputView {
                            value = param.pictogram.property.value
                            onChange {
                                val property = param.pictogram.property
                                if (property is Property<String>) {
                                    property.value = it
                                }
                            }
                            onFocusLeave {
                                val property = param.pictogram.property
                                if (property is Property<String>) {
                                    property.value = property.value.trim()
                                }
                            }
                        }
                    }
                    cellBox {
                        iconView(MaterialIcon.CLEAR) {
                            onClick {
                                deleteParameter()
                            }
                        }
                    }
                }
            }
        }

        reference = param.pictogram.property.onChange.reference {
            val value = param.pictogram.property.value
            if (value.trim() != nameInput.value.trim()) {
                nameInput.value = value
            }
        }
    }
}