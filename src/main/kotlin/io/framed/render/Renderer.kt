package io.framed.render

import io.framed.picto.ViewModel
import io.framed.util.Point

/**
 * @author lars
 */
interface Renderer {
    fun render(viewModel: ViewModel)
    fun zoomTo(zoom: Double)
    fun panTo(point: Point)
}