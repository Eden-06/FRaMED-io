package io.framed.controller

import io.framed.util.EventHandler

/**
 * @author lars
 */
abstract class NamedController : Controller {
    abstract var name: String
    val onNameChange = EventHandler<String>()
}