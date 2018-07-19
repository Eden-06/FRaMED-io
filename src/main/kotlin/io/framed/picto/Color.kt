package io.framed.picto

/**
 * @author lars
 */
data class Color(
        val red: Int,
        val green: Int,
        val blue: Int,
        val alpha: Double
) {
    companion object {
        val TRANSPARENT = Color(0, 0, 0, 0.0)
    }
}