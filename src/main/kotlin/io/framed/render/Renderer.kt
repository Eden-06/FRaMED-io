package io.framed.render

import io.framed.picto.ViewModel

/**
 * @author lars
 */
interface Renderer {
    fun render(viewModel: ViewModel)
}