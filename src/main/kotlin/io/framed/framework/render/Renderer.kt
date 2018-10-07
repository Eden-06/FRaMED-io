package io.framed.framework.render

import io.framed.framework.pictogram.ViewModel
import io.framed.framework.util.Point

/**
 * @author lars
 */
interface Renderer {
    fun render(viewModel: ViewModel)
    fun zoomTo(zoom: Double)
    fun panTo(point: Point)
}