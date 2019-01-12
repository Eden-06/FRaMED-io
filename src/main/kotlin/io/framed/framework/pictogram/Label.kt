package io.framed.framework.pictogram

import de.westermann.kobserve.basic.property
import io.framed.framework.util.trackHistory
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class Label(
        private var text: String = "",
        private var left: Double = 0.0,
        private var top: Double = 0.0,
        private var position: Double = 0.0,
        val id: String? = null
) {

    @Transient
    var autocomplete: List<String> = emptyList()

    @Transient
    val textProperty = property(this::text).trackHistory()

    @Transient
    val leftProperty = property(this::left).trackHistory()

    @Transient
    val topProperty = property(this::top).trackHistory()

    @Transient
    val positionProperty = property(this::position).trackHistory()
}