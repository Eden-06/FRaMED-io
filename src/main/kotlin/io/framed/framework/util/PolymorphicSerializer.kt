package io.framed

import kotlinx.serialization.*
import kotlin.reflect.KClass

object PolymorphicSerializer : KSerializer<Any> {

    override val descriptor: SerialDescriptor
        get() = PolymorphicClassDesc

    private val simpleNameSerializers = mutableMapOf<String, KSerializer<Any>>()
    private val qualifiedSerializers = mutableMapOf<String, KSerializer<Any>>()

    fun <T : Any> registerSerializer(klass: KClass<T>, serializer: KSerializer<T>, qualifiedName: String) {
        val className = klass.simpleName!!
        @Suppress("UNCHECKED_CAST")
        val anySerializer = serializer as KSerializer<Any>

        // Cannot register duplicate class names.
        val error = "For now, polymorphic serialization in JavaScript does not allow duplicate class names."
        if (simpleNameSerializers.containsKey(className)) {
            throw IllegalArgumentException("A class with the name '$className$' is already registered. $error")
        }
        if (qualifiedSerializers.containsKey(qualifiedName)) {
            throw IllegalArgumentException("A class with the qualified name '$qualifiedName' is already registered. $error")
        }

        simpleNameSerializers[className] = anySerializer
        qualifiedSerializers[qualifiedName] = anySerializer
    }

    private fun getSerializerBySimpleClassName(className: String): KSerializer<Any> {
        if (!simpleNameSerializers.containsKey(className)) {
            throw NoSuchElementException("No polymorphic serializer is registered for the class '$className'.")
        }

        return simpleNameSerializers[className]!!
    }

    private fun getSerializerByQualifiedName(qualifiedName: String): KSerializer<Any> {
        if (!isSerializerByQualifiedNameRegistered(qualifiedName)) {
            throw NoSuchElementException("No polymorphic serializer is registered with the qualified name '$qualifiedName'.")
        }

        return qualifiedSerializers[qualifiedName]!!
    }

    private fun isSerializerByQualifiedNameRegistered(qualifiedName: String): Boolean {
        return qualifiedSerializers.containsKey(qualifiedName)
    }


    override fun serialize(encoder: Encoder, obj: Any) {
        val saver = getSerializerBySimpleClassName(obj::class.simpleName!!)

        val output = encoder.beginStructure(descriptor)
        output.encodeStringElement(descriptor, 0, saver.descriptor.name)
        output.encodeSerializableElement(descriptor, 1, saver, obj)
        output.endStructure(descriptor)
    }

    override fun deserialize(decoder: Decoder): Any {
        val input = decoder.beginStructure(descriptor)
        var klassName: String? = null
        var value: Any? = null
        mainLoop@ while (true) {
            when (input.decodeElementIndex(descriptor)) {
                CompositeDecoder.READ_ALL -> {
                    klassName = input.decodeStringElement(descriptor, 0)
                    val loader = getSerializerByQualifiedName(klassName)
                    value = input.decodeSerializableElement(descriptor, 1, loader)
                    break@mainLoop
                }
                CompositeDecoder.READ_DONE -> {
                    break@mainLoop
                }
                0 -> {
                    klassName = input.decodeStringElement(descriptor, 0)
                }
                1 -> {
                    klassName = requireNotNull(klassName) { "Cannot read polymorphic value before its type token" }
                    val loader = getSerializerByQualifiedName(klassName)
                    value = input.decodeSerializableElement(descriptor, 1, loader)
                }
                else -> throw SerializationException("Invalid index")
            }
        }

        input.endStructure(descriptor)
        return requireNotNull(value) { "Polymorphic value have not been read" }
    }
}