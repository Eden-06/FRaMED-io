package io.framed.framework.render.html

import io.framed.framework.JsPlumbInstance
import io.framed.framework.pictogram.BoxShape
import io.framed.framework.pictogram.IconShape
import io.framed.framework.view.View
import io.framed.framework.view.ViewCollection
import io.framed.framework.view.iconView
import io.framed.framework.view.listView

class HtmlIconShape(
        htmlRenderer: HtmlRenderer,
        override val shape: IconShape,
        parent: HtmlContentShape?,
        parentContainer: HtmlShapeContainer,
        container: ViewCollection<View<*>, *>,
        val position: BoxShape.Position,
        override val jsPlumbInstance: JsPlumbInstance
) : HtmlShape(htmlRenderer, shape, parent, parentContainer, container, jsPlumbInstance) {

    override val view: View<*> = container.listView {
        iconView(shape.property) {
            classes += "icon-shape"
            style(this, shape.style)
        }

        events(this, shape)
    }

    override val viewList: List<View<*>> = listOf(view)

    override val positionView: View<*>
        get() = view

    init {
        if (position == BoxShape.Position.ABSOLUTE) {
            absolutePosition(view)
        } else if (position == BoxShape.Position.BORDER) {
            borderPosition(view, parent?.parent as HtmlBoxShape)

            // shape.property.onChange.reference {
            //     view.zIndex = if (shape.property.value == null) -1 else null
            // }.emit(Unit)
        }

        init()
    }
}
