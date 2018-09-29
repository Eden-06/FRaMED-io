package io.framed.picto

import io.framed.util.EventHandler

/**
 * @author lars
 */
abstract class Shape : Picto() {

    var left: Double?
        get() = layer[this, Layer.Prop.LEFT]
        set(value) {
            layer[this, Layer.Prop.LEFT] = value
        }
    var top: Double?
        get() = layer[this, Layer.Prop.TOP]
        set(value) {
            layer[this, Layer.Prop.TOP] = value
        }
    var width: Double?
        get() = layer[this, Layer.Prop.WIDTH]
        set(value) {
            layer[this, Layer.Prop.WIDTH] = value
        }
    var height: Double?
        get() = layer[this, Layer.Prop.HEIGHT]
        set(value) {
            layer[this, Layer.Prop.HEIGHT] = value
        }

    val onPositionChange = EventHandler<Boolean>()

    var style: Style = Style()
    var acceptRelation: Boolean = false;

    fun style(init: Style.() -> Unit) {
        init(style)
    }

    init {
        layer.onUpdate(this).addListener { force ->
            onPositionChange.fire(force)
        }
        onLayerChange {
            layer.onUpdate(this).addListener { force ->
                onPositionChange.fire(force)
            }
        }
    }
}