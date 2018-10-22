package io.framed.model

import io.framed.framework.ModelElement

/**
 * Base model interface for easier access.
 *
 * @author lars
 */
interface ModelElementMetadata<M : ModelElementMetadata<M>> : ModelElement<M> {
    val metadata: Metadata
}
