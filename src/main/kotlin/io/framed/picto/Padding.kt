package io.framed.picto

/**
 * The padding class controls the visual padding of HTML Elements
 */
class Padding(
        /**
         * controlls the top padding of an element
         */
        var paddingTop: Double = 0.0,
        /**
         * controlls the left padding of an element
         */
        var paddingLeft: Double = 0.0,
        /**
         * controlls the bottom padding of an element
         */
        var paddingBottom: Double = 0.0,
        /**
         * controlls the right padding of an element
         */
        var paddingRight: Double = 0.0
) {
    enum class BorderStyle(cssValue: String) {
        SMALL("10px"),
        MIDDLE("20px"),
        BIG("40px");

        override fun toString(): String = name.toLowerCase()
    }

    fun toCss(): String = "${paddingTop}px ${paddingRight}px ${paddingBottom}px ${paddingLeft}px"
}
// The function initializes a new instance
fun Style.padding(init: Padding.() -> Unit): Padding = Padding().also(init).also { padding = it }
fun Style.padding(padding: Double): Padding = Padding().also {
    it.paddingTop = padding
    it.paddingLeft = padding
    it.paddingBottom = padding
    it.paddingRight = padding
    this.padding = it
}