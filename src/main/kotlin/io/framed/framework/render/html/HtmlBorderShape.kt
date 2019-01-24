package io.framed.framework.render.html

import io.framed.framework.JsPlumbInstance
import io.framed.framework.pictogram.BoxShape
import io.framed.framework.view.View
import io.framed.framework.view.ViewCollection
import io.framed.framework.view.listView

class HtmlBorderShape(
        htmlRenderer: HtmlRenderer,
        override val shape: BoxShape,
        parent: HtmlContentShape,
        parentContainer: HtmlShapeContainer,
        container: ViewCollection<View<*>, *>,
        val position: BoxShape.Position,
        override val jsPlumbInstance: JsPlumbInstance
) : HtmlContentShape(htmlRenderer, shape, parent, parentContainer, container, jsPlumbInstance) {

    private val realContainer: ViewCollection<View<*>, *> = (parent as HtmlBoxShape).positionView

    override val view: ViewCollection<View<*>, *> = realContainer.listView {
        classes += "border-box"
        realContainer.toBackground(this)
    }

    override val positionView: View<*>
        get() = view

    override val content = HtmlShapeContainer(
            htmlRenderer,
            shape,
            view,
            jsPlumbInstance,
            this
    )

    override val viewList: List<View<*>> = listOf(view)

    override fun remove() {
        super.remove()
        content.remove()
        realContainer -= view
    }

    init {
        events(view, parent.shape)
    }
}