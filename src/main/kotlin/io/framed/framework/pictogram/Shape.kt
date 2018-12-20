package io.framed.framework.pictogram

import de.westermann.kobserve.EventHandler
import de.westermann.kobserve.ListenerReference
import de.westermann.kobserve.basic.flatMapBinding
import de.westermann.kobserve.basic.flatMapReadOnlyBinding
import de.westermann.kobserve.basic.mapBinding
import de.westermann.kobserve.basic.property
import de.westermann.kobserve.listenTo
import io.framed.framework.util.Dimension

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

    val onMove = EventHandler<Unit>()

    fun data(name: String) = layerProperty.flatMapBinding { it[id].data(name) }

    var style: Style = Style()

    fun style(init: Style.() -> Unit) {
        init(style)
    }

    init {
        onMove.listenTo(leftProperty.onChange)
        onMove.listenTo(topProperty.onChange)
        onMove.listenTo(widthProperty.onChange)
        onMove.listenTo(heightProperty.onChange)
    }
}
