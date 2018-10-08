package io.framed.framework.render

import io.framed.framework.pictogram.ViewModel
import io.framed.framework.util.EventHandler
import io.framed.framework.util.Point

/**
 * @author lars
 */
interface Renderer {
    fun render(viewModel: ViewModel)
    fun panTo(point: Point)

    var zoom: Double
    val onZoom: EventHandler<Double>
}