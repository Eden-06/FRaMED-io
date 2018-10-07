package io.framed.framework

import io.framed.framework.pictogram.Connection
import io.framed.framework.pictogram.Pictogram
import io.framed.framework.pictogram.Shape
import io.framed.framework.util.Property

/**
 * @author lars
 */
interface ConnectionLinker<M: ModelElement, P : Connection>: Linker<M,P>{
    val sourceIdProperty: Property<Long>
    val sourceShapeProperty: Property<Shape>

    val targetIdProperty: Property<Long>
    val targetShapeProperty: Property<Shape>
}