package io.framed.model

import io.framed.framework.ModelElement
import kotlinx.serialization.Serializable

/**
 * Base model interface for easier access.
 *
 * @author lars
 */
@Serializable
abstract class ModelElementMetadata<M : ModelElementMetadata<M>> : ModelElement<M>() {
    val metadata: Metadata = Metadata()
}
