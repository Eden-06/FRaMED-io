package io.framed

import io.framed.framework.pictogram.Layer
import io.framed.linker.ConnectionManagerLinker
import io.framed.model.Connections
import io.framed.model.Container
import kotlinx.serialization.Serializable

@Serializable
class File(
        val root: Container,
        val connections: Connections,
        val layer: Map<Long, Layer>
)