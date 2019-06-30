package io.framed.framework.linker

import io.framed.framework.model.ModelElement
import io.framed.framework.pictogram.Shape

/**
 * This interface preview shape to the linker. The preview shape will be shown in the list preview of a [ModelLinker].
 *
 * @author lars
 */
interface PreviewLinker<M : ModelElement<M>, P : Shape, R : Shape> : ShapeLinker<M, P> {
    val preview: R
}
