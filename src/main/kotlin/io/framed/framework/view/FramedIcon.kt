package io.framed.framework.view

import org.w3c.dom.Element
import org.w3c.dom.HTMLImageElement
import kotlinx.browser.document

/**
 * List of material design icons.
 */
enum class FramedIcon(private val src: String) : Icon {

    RELATIONSHIP("relationship"),
    FULFILLMENT("fulfillment"),
    COMPOSITION("composition"),
    AGGREGATION("aggregation"),
    INHERITANCE("inheritance"),
    IMPLICATION("implication"),
    EQUIVALENCE("equivalence"),
    PROHIBITION("prohibition"),
    EVENTRELATIONSHIP("eventrelationship"),
    ATTRIBUTE("attribute"),
    METHOD("method"),
    ROLETYPE("roletype"),
    EVENT("event"),
    RETURNEVENT("returnevent"),
    CLASS("class"),
    PACKAGE("package"),
    COMPARTMENT("compartment"),
    SCENE("scene"),
    ROLEGROUP("rolegroup"),

    EVENT_STANDARD("event_standard"),
    EVENT_TIMER("event_timer"),
    EVENT_MESSAGE("event_message"),
    EVENT_ERROR("event_error"),
    EVENT_CONDITION("event_condition"),
    EVENT_SIGNAL("event_signal");

    override val element: Element
        get() = document.createElement("img").also {
            if (it is HTMLImageElement) {
                it.classList.add("framed-icons")
                it.alt = name
                it.src = "$PATH$src.svg"
            }
        }

    companion object {
        private const val PATH = "public/icons/"
    }
}
