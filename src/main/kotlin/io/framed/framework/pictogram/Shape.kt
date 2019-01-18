package io.framed.framework.pictogram

import de.westermann.kobserve.basic.flatMapBinding

/**
 * @author lars
 */
abstract class Shape(id: Long?) : Pictogram(id) {

    val leftProperty = layerProperty.flatMapBinding { it[id].leftProperty }
    var left by leftProperty

    val topProperty = layerProperty.flatMapBinding { it[id].topProperty }
    var top by topProperty

    val autosizeProperty = layerProperty.flatMapBinding { it[id].autosizeProperty }
    var autosize by autosizeProperty

    val widthProperty = layerProperty.flatMapBinding { it[id].widthProperty }
    var width by widthProperty

    val heightProperty = layerProperty.flatMapBinding { it[id].heightProperty }
    var height by heightProperty

    var parent: BoxShape? = null

    val leftOffset: Double
        get() = left + (parent?.leftOffset ?: 0.0)

    val topOffset: Double
        get() = top + (parent?.topOffset ?: 0.0)

    var style: Style = Style()

    fun style(init: Style.() -> Unit) {
        init(style)
    }
}
