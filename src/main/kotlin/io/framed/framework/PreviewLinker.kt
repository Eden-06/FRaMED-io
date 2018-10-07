package io.framed.framework

import io.framed.framework.pictogram.Pictogram
import io.framed.framework.pictogram.Shape

/**
 * @author lars
 */
interface PreviewLinker<M : ModelElement, P : Shape, R : Shape> : Linker<M, P> {
    val preview: R
}