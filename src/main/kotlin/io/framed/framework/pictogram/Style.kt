package io.framed.framework.pictogram

/**
 * Style attributes for the ViewModel [Shape] and [Border] classes.
 * @author lars
 */
class Style {
    var border: Border? = null
    var background: Paint? = null
    var padding: Box<Double>? = null
    var topNotch = false
    var notch = false
    var stretchHeight = false
    // Use html flex layout options.
    var flex = false
    // Allow overflow for the html element.
    var overflow = false
}
