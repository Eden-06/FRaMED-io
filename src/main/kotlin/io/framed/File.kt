package io.framed

import io.framed.framework.ControllerManager
import io.framed.framework.ModelElement
import io.framed.framework.pictogram.Layer
import io.framed.linker.ConnectionManagerLinker
import io.framed.linker.ContainerLinker
import io.framed.model.Connections
import io.framed.model.Container
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.JSON

@Serializable
class File(
        val root: Container,
        val connections: Connections,
        val layer: Map<Long, Layer>
) {

    @Transient
    val name: String
        get() = root.name.toLowerCase()

    fun toJSON(): String {
        return JSON.indented.stringify(File.serializer(), this) + "\n"
    }

    companion object {

        fun fromJSON(content: String): File {
            val file = JSON.parse(File.serializer(), content)

            ModelElement.lastId = file.root.maxId() + 1

            ControllerManager.layers = file.layer
            ControllerManager.display(ContainerLinker(file.root, ConnectionManagerLinker(file.connections)))
            return file
        }
    }
}