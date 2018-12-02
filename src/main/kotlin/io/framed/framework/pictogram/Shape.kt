package io.framed.framework.pictogram

import de.westermann.kobserve.EventHandler
import de.westermann.kobserve.ListenerReference

/**
 * @author lars
 */
abstract class Shape : Pictogram() {

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

    fun style(init: Style.() -> Unit) {
        init(style)
    }

    private var listenerReference: ListenerReference<Boolean>? = null

    init {
        onLayerChange {
            listenerReference?.remove()
            listenerReference = layer.onUpdate(this)?.reference { force ->
                onPositionChange.emit(force)
            }
        }
    }
}