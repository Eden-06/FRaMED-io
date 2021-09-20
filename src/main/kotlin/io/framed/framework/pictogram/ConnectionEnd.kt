package io.framed.framework.pictogram

/**
 * ViewModel class representing a endpoint of a connection.
 *
 * @author Lars Westermann, David Oberacker
 */
class ConnectionEnd {

    /**
     * Defines the foldback of the arrow on the connection end.
     *
     * @see <a href="https://docs.jsplumbtoolkit.com/community/current/articles/overlays.html">JsPlumb Endpoints</a>
     */
    var foldback: Double = 1.0

    /**
     * Direction of the endpoint arrow on the connection.
     * 0 points towards the source, 1 towards the target.
     *
     * TODO: Rethink the attributes for source and target connection ends.
     *
     * @see <a href="https://docs.jsplumbtoolkit.com/community/current/articles/overlays.html">JsPlumb Endpoints</a>
     */
    var direction: Int = 1

    /**
     * Location of the endpoint arrow on the connection.
     * 0 represents the beginning, 1 the end.
     *
     * TODO: Rethink the attributes for source and target connection ends.
     *
     * @see <a href="https://docs.jsplumbtoolkit.com/community/current/articles/overlays.html">JsPlumb Endpoints</a>
     */
    var location: Double = 1.0

    /**
     * Width of the endpoint arrow.
     */
    var width: Int = 20

    /**
     * Length of the endpoint arrow.
     */
    var length: Int = 20

    var paintStyle: PaintStyle = PaintStyle()
}

fun connectionEnd(init: ConnectionEnd.() -> Unit) = ConnectionEnd().also(init)
