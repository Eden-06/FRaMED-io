package io.framed.framework

import io.framed.framework.pictogram.Shape

/**
 * @author lars
 */
interface PreviewLinker<M : ModelElement<M>, P : Shape, R : Shape> : ShapeLinker<M, P> {
    val listPreview: R
    val flatPreview: P
}
