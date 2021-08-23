package io.framed.util

import org.w3c.dom.Element
import org.w3c.dom.get
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class TestMatchers {
    companion object {

        fun labelMatcher(value: String) : (Element) -> Unit {
            return fun (element: Element) {
                assertEquals(value, element.textContent!!,
                    "Label title not matching.")
            }
        }

        fun listLabelMatcher(value: String) : (Element) -> Unit {
            return fun (element: Element) {
                assertEquals(1, element.children.length,
                    "Label element $value has invalid child element count")
                element.children[0]?.let { title ->
                    assertEquals(value, title.textContent!!,
                        "List label title not matching.")
                }
            }
        }

        fun checkBoxMatcher(value: String) : (Element) -> Unit {
            return fun (element: Element) {
                assertEquals(2, element.children.length,
                    "Check box element $value has invalid child element count")
                element.children[1]?.let { title ->
                    assertEquals(value, title.textContent!!,
                        "Check box label title not matching.")
                }
            }
        }

        fun inputFieldMatcher(label: String) : (Element) -> Unit {
            return fun (element: Element) {
                assertEquals(2, element.children.length)
                element.children[0]?.let { titleView ->
                    assertEquals(label, titleView.textContent, "Input field label is not matching!")
                }
                element.children[1]?.let { inputView ->
                    assertNotNull(inputView, "Input view is null!")
                }
            }
        }

        fun sidebarMatcher(sidebarTitle: String, vararg subMatchers: (Element) -> Unit) : (Element) -> Unit {
            return fun (sidebarElement: Element) {
                // Test HTML Children
                assertEquals(
                    1 + subMatchers.size, sidebarElement.children.length,
                    "Unexpected amount of sidebar children in $sidebarTitle!"
                )

                // Test Title entry
                sidebarElement.children[0]?.let { titleListView ->
                    listLabelMatcher(sidebarTitle)(titleListView)
                }

                for (index in 1 until subMatchers.size) {
                    subMatchers[index - 1](sidebarElement.children[index]!!)
                }
            }
        }
        fun sidebarSectionMatcher(title: String, vararg subMatchers: (Element) -> Unit) : (Element) -> Unit {
            return fun (element: Element) {
                assertEquals(
                    1 + subMatchers.size, element.children.length,
                    "Unexpected amount of children in the nested element: $title"
                )
                element.children[0]!!.let { groupHeader ->
                    assertEquals(2, groupHeader.children.length,
                        "Sidebar group header element $title has invalid child element count")
                    groupHeader.children[0]?.let { headerTextField ->
                        assertEquals(title, headerTextField.textContent!!,
                            "Sidebar group header element title not matching.")
                    }
                }

                for (index in 1 until element.children.length) {
                    subMatchers[index-1](element.children[index]!!)
                }
            }
        }

        fun contextMenuMatcher(contextMenuTitle: String, vararg elementNames: String) : (Element) -> Unit {
            return fun (contextMenu: Element) {
                assertEquals(
                    1 + elementNames.size, contextMenu.children.length,
                    "Context menu has invalid number of child elements"
                )

                // Title entry
                contextMenu.children[0]?.let { titleView ->
                    labelMatcher(contextMenuTitle)(titleView)
                }

                for (index in 1 until contextMenu.children.length) {
                    contextMenuButtonMatcher(elementNames[index - 1])(contextMenu.children[index]!!)
                }
            }
        }

        private fun contextMenuButtonMatcher(buttonName: String) : (Element) -> Unit {
            return fun (buttonElement: Element) {
                assertEquals(
                    2,
                    buttonElement.children.length,
                    "Context menu button has invalid child element count, one icon and one text child are expected."
                )
                // Delete Button Icon
                buttonElement.children[0]?.let { deleteButtonIconView ->
                    assertNotNull(
                        deleteButtonIconView,
                        "Button icon is null!"
                    )
                }

                // Delete Button Text
                buttonElement.children[1]?.let { deleteButtonTextView ->
                    assertEquals(
                        buttonName, deleteButtonTextView.textContent!!,
                        "Button label not matching the expected value!"
                    )
                }
            }
        }
    }
}
