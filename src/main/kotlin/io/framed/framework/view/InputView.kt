package io.framed.framework.view

import io.framed.framework.util.Property
import io.framed.framework.util.property
import org.w3c.dom.HTMLDivElement

/**
 * Represents html input element.
 *
 * @author lars
 */
class InputView() : View<HTMLDivElement>("div") {

    constructor(property: Property<String>) : this() {
        bind(property)
    }

    private val input = RawInputView().also {
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
        it.visible = false
    }
    private var autocompleteMap: List<Pair<String, TextView>> = emptyList()
    val autocompleteVisible: List<TextView>
        get() = autocompleteMap.map { it.second }.filter { it.visible }

    var autocomplete: List<String> = emptyList()
        set(value) {
            autocompleteListView.clear()

            value.forEach { txt ->
                val textView = TextView(txt)
                textView.onMouseDown { _ ->
                    this.value = txt
                }

                autocompleteMap += txt to textView
                autocompleteListView.append(textView)
            }
            field = value

            updateAutocomplete()
        }


    fun bind(property: Property<String>) = input.bind(property)

    private fun updateAutocomplete() {
        autocompleteMap.forEach { (auto, view) ->
            view.selectedView = false
            view.visible = auto.contains(value, ignoreCase = true)
        }
    }

    override fun focus() = input.focus()
    override fun blur() = input.blur()

    init {
        updateAutocomplete()
        valueProperty.onChange {
            updateAutocomplete()
        }
        onChange {
            updateAutocomplete()
        }

        onFocusEnter {
            if (autocomplete.isNotEmpty()) {
                autocompleteListView.visible = true
            }
        }
        onFocusLeave { _ ->
            autocompleteListView.visible = false
            autocompleteMap.forEach { it.second.selectedView = false }
        }

        onKeyPress { event ->
            val list = autocompleteVisible
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
                            list[index].onMouseDown.fire(js("{}"))
                        }
                    }
                    27 -> { // exit
                        if (index != -1) {
                            list[index].selectedView = false
                        }
                        autocompleteListView.visible = false
                    }
                }
            }
        }
    }
}

fun ViewCollection<in InputView, *>.inputView(init: InputView.() -> Unit) =
        InputView().also(this::append).also(init)

fun ViewCollection<in InputView, *>.inputView(property: Property<String>, init: InputView.() -> Unit)=
        InputView(property).also(this::append).also(init)