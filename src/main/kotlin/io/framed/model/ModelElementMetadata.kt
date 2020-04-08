package io.framed.model

import io.framed.framework.model.ModelElement
import kotlinx.serialization.Serializable

/**
 * Base model interface for easier access.
 *
 * @author lars
 */
@Serializable
abstract class ModelElementMetadata : ModelElement() {
    val metadata: Metadata = Metadata()
}
