package io.framed.framework.view

import de.westermann.kobserve.ReadOnlyProperty
import de.westermann.kobserve.property.property
import io.framed.framework.AutocompleteHandler
import org.w3c.dom.HTMLDivElement

/**
 * Represents html input element.
 *
 * @author lars
 */
class InputView() : View<HTMLDivElement>("div") {

    constructor(property: ReadOnlyProperty<String>) : this() {
        bind(property)
    }

    val input = RawInputView().also {
        html.appendChild(it.html)
    }

    private val valueProperty = property(input::value)

    /**
     * Inputs value.
     */
    var value by valueProperty

    /**
     * Set input to readonly.
     */
    var readOnly by property(input::readOnly)

    /**
     * Fires on every user change to the content
     */
    val onChange = input.onChange

    /**
     * Fires on focus leave.
     */
    val onFocusLeave = input.onFocusLeave

    /**
     * Fires on focus gain.
     */
    val onFocusEnter = input.onFocusEnter

    var invalid by property(input::invalid)

    private var autocompleteListView: ListView = ListView().also {
        html.appendChild(it.html)
        it.classes += "autocomplete"
        it.display = false
    }

    var autocomplete: AutocompleteHandler? = null

    fun autocomplete(dataset: List<String>, showAll: Boolean = false) {
        autocomplete = AutocompleteHandler.ListAutocompleteHandler(input, autocompleteListView, dataset, showAll)
    }

    fun autocomplete(autocompleter: (partial: String) -> List<String>) {
        autocomplete = AutocompleteHandler.DynamicAutocompleteHandler(input, autocompleteListView, autocompleter)
    }

    fun bind(property: ReadOnlyProperty<String>) = input.bind(property)

    private fun updateAutocomplete() {
        autocomplete?.update(value)
    }

    override fun focus() = input.focus()
    override fun blur() = input.blur()

    var size by property(input::size)
    fun sizeMatchText() = input.sizeMatchText()

    private val focusClassProperty by ClassDelegate("focus")
    private var focusClass by focusClassProperty

    init {
        updateAutocomplete()
        valueProperty.onChange {
            updateAutocomplete()
        }
        onChange {
            updateAutocomplete()
        }

        onFocusEnter {
            if (autocomplete != null) {
                autocompleteListView.display = true
                updateAutocomplete()
            }
            focusClass = true
        }
        onFocusLeave { _ ->
            autocompleteListView.display = false
            focusClass = false
        }

        onKeyPress { event ->
            val list = autocomplete?.visible ?: return@onKeyPress
            val index = list.indexOfFirst { it.selectedView }
            if (list.isNotEmpty()) {
                when (event.keyCode) {
                    38 -> { // key up
                        if (index != -1) {
                            list[index].selectedView = false
                        }
                        if (index > 0) {
                            list[index - 1].selectedView = true
                        } else {
                            list[list.lastIndex].selectedView = true
                        }
                    }
                    40 -> { // key down
                        if (index != -1) {
                            list[index].selectedView = false
                        }
                        if (index < list.lastIndex) {
                            list[index + 1].selectedView = true
                        } else {
                            list[0].selectedView = true
                        }
                    }
                    13 -> { // submit
                        if (index != -1) {
                            list[index].onMouseDown.emit(js("{}"))
                        }
                    }
                    27 -> { // exit
                        if (index != -1) {
                            list[index].selectedView = false
                        }
                        autocompleteListView.display = false
                    }
                }
            }
        }
    }
}

fun ViewCollection<in InputView, *>.inputView(init: InputView.() -> Unit) =
        InputView().also(this::append).also(init)

fun ViewCollection<in InputView, *>.inputView(property: ReadOnlyProperty<String>, init: InputView.() -> Unit) =
        InputView(property).also(this::append).also(init)