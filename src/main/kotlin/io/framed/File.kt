package io.framed

import io.framed.framework.pictogram.Layer
import io.framed.model.Container
import kotlinx.serialization.Serializable

@Serializable
class File(
        val root: Container,
        val layer: Layer
)