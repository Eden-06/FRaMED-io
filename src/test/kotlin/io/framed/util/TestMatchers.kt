package io.framed.util

import org.w3c.dom.Element
import org.w3c.dom.get
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.fail

class TestMatchers {
    companion object {


        /**
         * Matcher checking for the textContent of an element.
         * @param value The expected value the textContent
         * @param elementName The name of the element who's textContent is checked against.
         * @author David Oberacker
         */
        fun elementTextContentMatcher(value: String, elementName: String = "TextContentElement") : (Element) -> Unit {
            return fun (element: Element) {
                assertNotNull(element.textContent,
                    "TextContent of $elementName is null")
                assertEquals(value, element.textContent!!,
                    "TextContent of $elementName not matching")
            }
        }

        /**
         * Matcher checking for the textContent of an element nested in a list view.
         * The list view can only have the text element as a child.
         * @see io.framed.framework.view.SidebarGroup
         * @param value The expected value the textContent
         * @param elementName The name of the element who's textContent is checked against.
         * @author David Oberacker
         */
        fun listWrappedTextContentMatcher(value: String, elementName: String = "ListWrappedTextLabel") : (Element) -> Unit {
            return fun (element: Element) {
                assertEquals(1, element.children.length,
                    "ListWrappedTextLabel element $elementName has invalid child element count")
                element.children[0]?.let { child ->
                    elementTextContentMatcher(value, elementName)(child)
                } ?: run {
                    fail("$elementName child is null.")
                }
            }
        }

        /**
         * Matcher checking for the textContent of the title of a [io.framed.framework.view.CheckBox] element.
         * The list view can only have the text element as a child.
         * @param value The expected value the textContent
         * @param elementName The name of the element who's textContent is checked against.
         * @author David Oberacker
         */
        fun checkBoxMatcher(value: String, elementName: String = "CheckboxElement") : (Element) -> Unit {
            return fun (element: Element) {
                assertEquals(2, element.children.length,
                    "$elementName has invalid child element count")
                element.children[1]?.let { title ->
                    elementTextContentMatcher(value, "$elementName title")(title)
                }?: run {
                    fail("$elementName second child is null.")
                }
            }
        }

        /**
         * Matcher checking a [io.framed.framework.view.InputView] element.
         * @param label The expected value the textContent
         * @param elementName The name of the element who's textContent is checked against.
         * @author David Oberacker
         */
        fun inputFieldMatcher(label: String, elementName: String = "InputElement") : (Element) -> Unit {
            return fun (element: Element) {
                assertEquals(2, element.children.length,
                    "$elementName has an invalid child count")
                element.children[0]?.let { titleView ->
                    elementTextContentMatcher(label,
                        "$elementName title is not matching")(titleView)
                } ?: run {
                    fail("$elementName first child is null")
                }
                assertNotNull( element.children[1],
                    "$elementName second child is null")
            }
        }

        /**
         * Matcher checking the correctness of a [io.framed.framework.view.Sidebar].
         * @param sidebarTitle The title of the sidebar.
         * @param subMatchers Matchers for the items in the sidebar.
         * @author David Oberacker
         */
        fun sidebarMatcher(sidebarTitle: String, vararg subMatchers: (Element) -> Unit) : (Element) -> Unit {
            return fun (sidebarElement: Element) {
                // Test HTML Children
                assertEquals(
                    1 + subMatchers.size, sidebarElement.children.length,
                    "Unexpected amount of sidebar children in $sidebarTitle!"
                )

                // Test Title entry
                sidebarElement.children[0]?.let { titleListView ->
                    listWrappedTextContentMatcher(sidebarTitle)(titleListView)
                }

                for (index in 1 until subMatchers.size) {
                    subMatchers[index - 1](sidebarElement.children[index]!!)
                }
            }
        }

        /**
         * Matcher checking the correctness of a [io.framed.framework.view.SidebarGroup].
         * @param title The title of the sidebar group.
         * @param subMatchers Matchers for the items in the sidebar group.
         * @author David Oberacker
         */
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

        /**
         * Matcher checking the correctness of a [io.framed.framework.view.ContextMenu].
         * @param contextMenuTitle The title of the context menu.
         * @param elementNames Matchers for the item titles in the context menu.
         * @author David Oberacker
         */
        fun contextMenuMatcher(contextMenuTitle: String, vararg elementNames: String) : (Element) -> Unit {
            return fun (contextMenu: Element) {
                assertEquals(
                    1 + elementNames.size, contextMenu.children.length,
                    "Context menu has invalid number of child elements"
                )

                // Title entry
                contextMenu.children[0]?.let { titleView ->
                    elementTextContentMatcher(contextMenuTitle)(titleView)
                }

                for (index in 1 until contextMenu.children.length) {
                    contextMenuButtonMatcher(elementNames[index - 1])(contextMenu.children[index]!!)
                }
            }
        }

        /**
         * Matcher checking the correctness of a [io.framed.framework.view.Button] with a [io.framed.framework.view.Icon]  in
         * a [io.framed.framework.view.ContextMenu].
         * @param buttonName The name of the button.
         * @author David Oberacker
         */
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
