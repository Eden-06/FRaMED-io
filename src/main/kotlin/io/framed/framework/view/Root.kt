package io.framed.framework.view

import org.w3c.dom.HTMLElement
import kotlin.browser.document

/**
 * @author lars
 */
object Root : ViewCollection<View<*>, HTMLElement>(document.body!!) {
}