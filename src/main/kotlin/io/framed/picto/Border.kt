package io.framed.picto

/**
 * @author lars
 */
data class Border(
        val style: BorderStyle,
        val width: Double,
        val color: Color,
        val radius: Double = 0.0
) {
    enum class BorderStyle {
        SOLID, DASHED, NONE
    }

    companion object {
        val NONE: Border = Border(BorderStyle.NONE, 0.0, Color.TRANSPARENT)
    }
}