package io.framed.framework.render

import de.westermann.kobserve.event.EventHandler
import io.framed.framework.pictogram.ViewModel
import io.framed.framework.util.Point

/**
 * @author lars
 */
interface Renderer {
    fun render(viewModel: ViewModel)
    fun panTo(point: Point)
    fun panBy(point: Point)

    var zoom: Double
    val onZoom: EventHandler<Double>
}