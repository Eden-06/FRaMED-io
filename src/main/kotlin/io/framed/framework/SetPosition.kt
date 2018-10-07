package io.framed.framework

import io.framed.framework.util.Point

data class SetPosition(
        val linker: PreviewLinker<*, *, *>,
        val position: Point
)