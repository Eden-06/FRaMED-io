package io.framed.linker

enum class CardinalityPreset(val symbol: String) {
    ONCE("1"),
    MANY("*"),

    NEVER_TO_ONCE("0..1"),
    NEVER_TO_MANY("0..*"),

    ONCE_TO_MANY("1..*");

    override fun toString(): String = name.lowercase().replace("_", " ")

    companion object {
        val STRING_VALUES = values().map { it.symbol }

        fun parse(str: String): CardinalityPreset? = values().find {
            it.symbol == str || it.toString() == str
        }
    }
}