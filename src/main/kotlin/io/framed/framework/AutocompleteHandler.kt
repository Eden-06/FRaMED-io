package io.framed.framework

import io.framed.framework.view.ListView
import io.framed.framework.view.RawInputView
import io.framed.framework.view.TextView

sealed class AutocompleteHandler(val input: RawInputView, val container: ListView) {

    abstract fun update(partial: String)

    abstract val visible: List<TextView>

    class ListAutocompleteHandler(input: RawInputView, container: ListView, dataset: List<String>, val showAll: Boolean = false) : AutocompleteHandler(input, container) {
        private var autocompleteMap: Map<String, TextView> = emptyMap()

        override fun update(partial: String) {
            val str = partial.toLowerCase()
            for ((key, view) in autocompleteMap) {
                view.display = showAll || key.contains(str)
            }
        }

        override val visible: List<TextView>
            get() = autocompleteMap.values.filter { it.display }

        init {
            container.clear()

            dataset.forEach { txt ->
                val textView = TextView(txt)
                textView.onMouseDown { _ ->
                    input.value = txt
                }

                container += textView

                autocompleteMap += txt.toLowerCase() to textView
            }

            update(input.value)
        }
    }

    class DynamicAutocompleteHandler(input: RawInputView, container: ListView, val completer: (partial: String) -> List<String>) : AutocompleteHandler(input, container) {

        var last: String = ""
        override fun update(partial: String) {
            val dataset = completer(partial.toLowerCase()) - last
            last = partial

            container.clear()

            visible = dataset.map { txt ->
                val textView = TextView(txt)
                textView.onMouseDown { _ ->
                    input.value = txt
                }
                container += textView
                textView
            }
        }

        override var visible: List<TextView> = emptyList()
    }
}