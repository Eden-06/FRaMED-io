package io.framed.framework.render.html

import de.westermann.kobserve.ListenerReference
import io.framed.framework.JsPlumbInstance
import io.framed.framework.pictogram.BoxShape
import io.framed.framework.pictogram.IconShape
import io.framed.framework.view.View
import io.framed.framework.view.ViewCollection
import io.framed.framework.view.iconView

class HtmlIconShape(
        htmlRenderer: HtmlRenderer,
        override val shape: IconShape,
        parent: HtmlContentShape?,
        parentContainer: HtmlShapeContainer?,
        container: ViewCollection<View<*>, *>,
        val position: BoxShape.Position,
        override val jsPlumbInstance: JsPlumbInstance
) : HtmlShape(htmlRenderer, shape, parent, parentContainer, container, jsPlumbInstance) {

    override val view: View<*> = container.iconView(shape.property) {
        style(this, shape.style)
        events(this, shape)
    }

    override fun remove() {
        super.remove()
        reference?.remove()
    }

    override val viewList: List<View<*>> = listOf(view)

    var reference: ListenerReference<*>? = null

    init {
        if (position == BoxShape.Position.ABSOLUTE) {
            absolutePosition(view,  view.html)
        } else if (position == BoxShape.Position.BORDER) {
            borderPosition(view,  view.html, parent?.parent as HtmlBoxShape)

            shape.property.onChange.reference {
                view.zIndex = if (shape.property.value == null) -1 else null
            }?.trigger(Unit)
        }

        parentContainer?.onParentMove?.reference {
            jsPlumbInstance.revalidate(view.html)
        }?.let { reference = it }
    }
}