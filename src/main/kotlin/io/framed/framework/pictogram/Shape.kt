package io.framed.framework.pictogram

import de.westermann.kobserve.EventHandler
import de.westermann.kobserve.basic.flatMapBinding
import de.westermann.kobserve.listenTo

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

    var style: Style = Style()

    fun style(init: Style.() -> Unit) {
        init(style)
    }
}
