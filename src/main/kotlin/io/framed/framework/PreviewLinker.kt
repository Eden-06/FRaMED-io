package io.framed.framework

import de.westermann.kobserve.ReadOnlyProperty
import io.framed.framework.pictogram.Shape

/**
 * @author lars
 */
interface PreviewLinker<M : ModelElement<M>, P : Shape, R : Shape> : ShapeLinker<M, P> {
    val nameProperty: ReadOnlyProperty<String>
    val name: String

    val listPreview: R
    val flatPreview: P
}
