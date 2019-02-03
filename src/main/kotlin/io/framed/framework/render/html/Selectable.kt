package io.framed.framework.render.html

import io.framed.framework.pictogram.Pictogram
import io.framed.framework.util.Dimension
import io.framed.framework.util.Point
import io.framed.framework.view.View
import io.framed.framework.view.ViewCollection

interface Selectable {
    val id: Long
    val pictogram: Pictogram

    val left: Double
    val top: Double
    val width: Double
    val height: Double

    fun select()
    fun unselect()
    fun selectArea(area: Dimension)

    val isSelected: Boolean

    val isDraggable: Boolean
    fun drag(delta: Point)

    fun setZoom(zoom: Double)

    fun highlightSnap()
    fun unhighlightSnap()

    fun isChildOf(container: ViewCollection<View<*>,*>): Boolean

    val positionView: View<*>?
}