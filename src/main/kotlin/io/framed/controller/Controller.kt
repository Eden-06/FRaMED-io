package io.framed.controller

import io.framed.view.Sidebar
import io.framed.view.View

/**
 * @author lars
 */
interface Controller {
    val view: View<*>
    val sidebar: Sidebar
}