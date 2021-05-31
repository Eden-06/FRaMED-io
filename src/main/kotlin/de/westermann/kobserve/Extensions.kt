@file:Suppress("unused")

package de.westermann.kobserve

import de.westermann.kobserve.property.join
import de.westermann.kobserve.property.mapBinding
import de.westermann.kobserve.list.ObservableReadOnlyList

// Boolean

infix fun ReadOnlyProperty<Boolean>.and(property: ReadOnlyProperty<Boolean>) =
    join(property, Boolean::and)

infix fun ReadOnlyProperty<Boolean>.or(property: ReadOnlyProperty<Boolean>) =
    join(property, Boolean::or)

infix fun ReadOnlyProperty<Boolean>.xor(property: ReadOnlyProperty<Boolean>) =
    join(property, Boolean::xor)

infix fun ReadOnlyProperty<Boolean>.implies(property: ReadOnlyProperty<Boolean>) =
    join(property) { a, b -> !a || b }

operator fun ReadOnlyProperty<Boolean>.not() =
    mapBinding(Boolean::not)

/* The following part is auto generated. Do NOT edit it manually! */

// Unary minus

operator fun ReadOnlyProperty<Int>.unaryMinus() = mapBinding(Int::unaryMinus)

operator fun ReadOnlyProperty<Long>.unaryMinus() = mapBinding(Long::unaryMinus)

operator fun ReadOnlyProperty<Float>.unaryMinus() = mapBinding(Float::unaryMinus)

operator fun ReadOnlyProperty<Double>.unaryMinus() = mapBinding(Double::unaryMinus)

operator fun ReadOnlyProperty<Short>.unaryMinus() = mapBinding(Short::unaryMinus)

operator fun ReadOnlyProperty<Byte>.unaryMinus() = mapBinding(Byte::unaryMinus)

// List sum

fun ObservableReadOnlyList<Int>.sumObservable() = mapBinding { it.sum() }

fun ObservableReadOnlyList<Long>.sumObservable() = mapBinding { it.sum() }

fun ObservableReadOnlyList<Float>.sumObservable() = mapBinding { it.sum() }

fun ObservableReadOnlyList<Double>.sumObservable() = mapBinding { it.sum() }

fun ObservableReadOnlyList<Short>.sumObservable() = mapBinding { it.sum() }

fun ObservableReadOnlyList<Byte>.sumObservable() = mapBinding { it.sum() }

// List average

fun ObservableReadOnlyList<Int>.averageObservable() = mapBinding { it.average() }

fun ObservableReadOnlyList<Long>.averageObservable() = mapBinding { it.average() }

fun ObservableReadOnlyList<Float>.averageObservable() = mapBinding { it.average() }

fun ObservableReadOnlyList<Double>.averageObservable() = mapBinding { it.average() }

fun ObservableReadOnlyList<Short>.averageObservable() = mapBinding { it.average() }

fun ObservableReadOnlyList<Byte>.averageObservable() = mapBinding { it.average() }

/*
 * Property - Property
 */

// Int - Int

operator fun ReadOnlyProperty<Int>.plus(property: ReadOnlyProperty<Int>) = join(property, Int::plus)

operator fun ReadOnlyProperty<Int>.minus(property: ReadOnlyProperty<Int>) = join(property, Int::minus)

operator fun ReadOnlyProperty<Int>.times(property: ReadOnlyProperty<Int>) = join(property, Int::times)

operator fun ReadOnlyProperty<Int>.div(property: ReadOnlyProperty<Int>) = join(property, Int::div)

operator fun ReadOnlyProperty<Int>.rem(property: ReadOnlyProperty<Int>) = join(property, Int::rem)

// Int - Long

operator fun ReadOnlyProperty<Int>.plus(property: ReadOnlyProperty<Long>) = join(property, Int::plus)

operator fun ReadOnlyProperty<Int>.minus(property: ReadOnlyProperty<Long>) = join(property, Int::minus)

operator fun ReadOnlyProperty<Int>.times(property: ReadOnlyProperty<Long>) = join(property, Int::times)

operator fun ReadOnlyProperty<Int>.div(property: ReadOnlyProperty<Long>) = join(property, Int::div)

operator fun ReadOnlyProperty<Int>.rem(property: ReadOnlyProperty<Long>) = join(property, Int::rem)

// Int - Float

operator fun ReadOnlyProperty<Int>.plus(property: ReadOnlyProperty<Float>) = join(property, Int::plus)

operator fun ReadOnlyProperty<Int>.minus(property: ReadOnlyProperty<Float>) = join(property, Int::minus)

operator fun ReadOnlyProperty<Int>.times(property: ReadOnlyProperty<Float>) = join(property, Int::times)

operator fun ReadOnlyProperty<Int>.div(property: ReadOnlyProperty<Float>) = join(property, Int::div)

operator fun ReadOnlyProperty<Int>.rem(property: ReadOnlyProperty<Float>) = join(property, Int::rem)

// Int - Double

operator fun ReadOnlyProperty<Int>.plus(property: ReadOnlyProperty<Double>) = join(property, Int::plus)

operator fun ReadOnlyProperty<Int>.minus(property: ReadOnlyProperty<Double>) = join(property, Int::minus)

operator fun ReadOnlyProperty<Int>.times(property: ReadOnlyProperty<Double>) = join(property, Int::times)

operator fun ReadOnlyProperty<Int>.div(property: ReadOnlyProperty<Double>) = join(property, Int::div)

operator fun ReadOnlyProperty<Int>.rem(property: ReadOnlyProperty<Double>) = join(property, Int::rem)

// Int - Short

operator fun ReadOnlyProperty<Int>.plus(property: ReadOnlyProperty<Short>) = join(property, Int::plus)

operator fun ReadOnlyProperty<Int>.minus(property: ReadOnlyProperty<Short>) = join(property, Int::minus)

operator fun ReadOnlyProperty<Int>.times(property: ReadOnlyProperty<Short>) = join(property, Int::times)

operator fun ReadOnlyProperty<Int>.div(property: ReadOnlyProperty<Short>) = join(property, Int::div)

operator fun ReadOnlyProperty<Int>.rem(property: ReadOnlyProperty<Short>) = join(property, Int::rem)

// Int - Byte

operator fun ReadOnlyProperty<Int>.plus(property: ReadOnlyProperty<Byte>) = join(property, Int::plus)

operator fun ReadOnlyProperty<Int>.minus(property: ReadOnlyProperty<Byte>) = join(property, Int::minus)

operator fun ReadOnlyProperty<Int>.times(property: ReadOnlyProperty<Byte>) = join(property, Int::times)

operator fun ReadOnlyProperty<Int>.div(property: ReadOnlyProperty<Byte>) = join(property, Int::div)

operator fun ReadOnlyProperty<Int>.rem(property: ReadOnlyProperty<Byte>) = join(property, Int::rem)

// Long - Int

operator fun ReadOnlyProperty<Long>.plus(property: ReadOnlyProperty<Int>) = join(property, Long::plus)

operator fun ReadOnlyProperty<Long>.minus(property: ReadOnlyProperty<Int>) = join(property, Long::minus)

operator fun ReadOnlyProperty<Long>.times(property: ReadOnlyProperty<Int>) = join(property, Long::times)

operator fun ReadOnlyProperty<Long>.div(property: ReadOnlyProperty<Int>) = join(property, Long::div)

operator fun ReadOnlyProperty<Long>.rem(property: ReadOnlyProperty<Int>) = join(property, Long::rem)

// Long - Long

operator fun ReadOnlyProperty<Long>.plus(property: ReadOnlyProperty<Long>) = join(property, Long::plus)

operator fun ReadOnlyProperty<Long>.minus(property: ReadOnlyProperty<Long>) = join(property, Long::minus)

operator fun ReadOnlyProperty<Long>.times(property: ReadOnlyProperty<Long>) = join(property, Long::times)

operator fun ReadOnlyProperty<Long>.div(property: ReadOnlyProperty<Long>) = join(property, Long::div)

operator fun ReadOnlyProperty<Long>.rem(property: ReadOnlyProperty<Long>) = join(property, Long::rem)

// Long - Float

operator fun ReadOnlyProperty<Long>.plus(property: ReadOnlyProperty<Float>) = join(property, Long::plus)

operator fun ReadOnlyProperty<Long>.minus(property: ReadOnlyProperty<Float>) = join(property, Long::minus)

operator fun ReadOnlyProperty<Long>.times(property: ReadOnlyProperty<Float>) = join(property, Long::times)

operator fun ReadOnlyProperty<Long>.div(property: ReadOnlyProperty<Float>) = join(property, Long::div)

operator fun ReadOnlyProperty<Long>.rem(property: ReadOnlyProperty<Float>) = join(property, Long::rem)

// Long - Double

operator fun ReadOnlyProperty<Long>.plus(property: ReadOnlyProperty<Double>) = join(property, Long::plus)

operator fun ReadOnlyProperty<Long>.minus(property: ReadOnlyProperty<Double>) = join(property, Long::minus)

operator fun ReadOnlyProperty<Long>.times(property: ReadOnlyProperty<Double>) = join(property, Long::times)

operator fun ReadOnlyProperty<Long>.div(property: ReadOnlyProperty<Double>) = join(property, Long::div)

operator fun ReadOnlyProperty<Long>.rem(property: ReadOnlyProperty<Double>) = join(property, Long::rem)

// Long - Short

operator fun ReadOnlyProperty<Long>.plus(property: ReadOnlyProperty<Short>) = join(property, Long::plus)

operator fun ReadOnlyProperty<Long>.minus(property: ReadOnlyProperty<Short>) = join(property, Long::minus)

operator fun ReadOnlyProperty<Long>.times(property: ReadOnlyProperty<Short>) = join(property, Long::times)

operator fun ReadOnlyProperty<Long>.div(property: ReadOnlyProperty<Short>) = join(property, Long::div)

operator fun ReadOnlyProperty<Long>.rem(property: ReadOnlyProperty<Short>) = join(property, Long::rem)

// Long - Byte

operator fun ReadOnlyProperty<Long>.plus(property: ReadOnlyProperty<Byte>) = join(property, Long::plus)

operator fun ReadOnlyProperty<Long>.minus(property: ReadOnlyProperty<Byte>) = join(property, Long::minus)

operator fun ReadOnlyProperty<Long>.times(property: ReadOnlyProperty<Byte>) = join(property, Long::times)

operator fun ReadOnlyProperty<Long>.div(property: ReadOnlyProperty<Byte>) = join(property, Long::div)

operator fun ReadOnlyProperty<Long>.rem(property: ReadOnlyProperty<Byte>) = join(property, Long::rem)

// Float - Int

operator fun ReadOnlyProperty<Float>.plus(property: ReadOnlyProperty<Int>) = join(property, Float::plus)

operator fun ReadOnlyProperty<Float>.minus(property: ReadOnlyProperty<Int>) = join(property, Float::minus)

operator fun ReadOnlyProperty<Float>.times(property: ReadOnlyProperty<Int>) = join(property, Float::times)

operator fun ReadOnlyProperty<Float>.div(property: ReadOnlyProperty<Int>) = join(property, Float::div)

operator fun ReadOnlyProperty<Float>.rem(property: ReadOnlyProperty<Int>) = join(property, Float::rem)

// Float - Long

operator fun ReadOnlyProperty<Float>.plus(property: ReadOnlyProperty<Long>) = join(property, Float::plus)

operator fun ReadOnlyProperty<Float>.minus(property: ReadOnlyProperty<Long>) = join(property, Float::minus)

operator fun ReadOnlyProperty<Float>.times(property: ReadOnlyProperty<Long>) = join(property, Float::times)

operator fun ReadOnlyProperty<Float>.div(property: ReadOnlyProperty<Long>) = join(property, Float::div)

operator fun ReadOnlyProperty<Float>.rem(property: ReadOnlyProperty<Long>) = join(property, Float::rem)

// Float - Float

operator fun ReadOnlyProperty<Float>.plus(property: ReadOnlyProperty<Float>) = join(property, Float::plus)

operator fun ReadOnlyProperty<Float>.minus(property: ReadOnlyProperty<Float>) = join(property, Float::minus)

operator fun ReadOnlyProperty<Float>.times(property: ReadOnlyProperty<Float>) = join(property, Float::times)

operator fun ReadOnlyProperty<Float>.div(property: ReadOnlyProperty<Float>) = join(property, Float::div)

operator fun ReadOnlyProperty<Float>.rem(property: ReadOnlyProperty<Float>) = join(property, Float::rem)

// Float - Double

operator fun ReadOnlyProperty<Float>.plus(property: ReadOnlyProperty<Double>) = join(property, Float::plus)

operator fun ReadOnlyProperty<Float>.minus(property: ReadOnlyProperty<Double>) = join(property, Float::minus)

operator fun ReadOnlyProperty<Float>.times(property: ReadOnlyProperty<Double>) = join(property, Float::times)

operator fun ReadOnlyProperty<Float>.div(property: ReadOnlyProperty<Double>) = join(property, Float::div)

operator fun ReadOnlyProperty<Float>.rem(property: ReadOnlyProperty<Double>) = join(property, Float::rem)

// Float - Short

operator fun ReadOnlyProperty<Float>.plus(property: ReadOnlyProperty<Short>) = join(property, Float::plus)

operator fun ReadOnlyProperty<Float>.minus(property: ReadOnlyProperty<Short>) = join(property, Float::minus)

operator fun ReadOnlyProperty<Float>.times(property: ReadOnlyProperty<Short>) = join(property, Float::times)

operator fun ReadOnlyProperty<Float>.div(property: ReadOnlyProperty<Short>) = join(property, Float::div)

operator fun ReadOnlyProperty<Float>.rem(property: ReadOnlyProperty<Short>) = join(property, Float::rem)

// Float - Byte

operator fun ReadOnlyProperty<Float>.plus(property: ReadOnlyProperty<Byte>) = join(property, Float::plus)

operator fun ReadOnlyProperty<Float>.minus(property: ReadOnlyProperty<Byte>) = join(property, Float::minus)

operator fun ReadOnlyProperty<Float>.times(property: ReadOnlyProperty<Byte>) = join(property, Float::times)

operator fun ReadOnlyProperty<Float>.div(property: ReadOnlyProperty<Byte>) = join(property, Float::div)

operator fun ReadOnlyProperty<Float>.rem(property: ReadOnlyProperty<Byte>) = join(property, Float::rem)

// Double - Int

operator fun ReadOnlyProperty<Double>.plus(property: ReadOnlyProperty<Int>) = join(property, Double::plus)

operator fun ReadOnlyProperty<Double>.minus(property: ReadOnlyProperty<Int>) = join(property, Double::minus)

operator fun ReadOnlyProperty<Double>.times(property: ReadOnlyProperty<Int>) = join(property, Double::times)

operator fun ReadOnlyProperty<Double>.div(property: ReadOnlyProperty<Int>) = join(property, Double::div)

operator fun ReadOnlyProperty<Double>.rem(property: ReadOnlyProperty<Int>) = join(property, Double::rem)

// Double - Long

operator fun ReadOnlyProperty<Double>.plus(property: ReadOnlyProperty<Long>) = join(property, Double::plus)

operator fun ReadOnlyProperty<Double>.minus(property: ReadOnlyProperty<Long>) = join(property, Double::minus)

operator fun ReadOnlyProperty<Double>.times(property: ReadOnlyProperty<Long>) = join(property, Double::times)

operator fun ReadOnlyProperty<Double>.div(property: ReadOnlyProperty<Long>) = join(property, Double::div)

operator fun ReadOnlyProperty<Double>.rem(property: ReadOnlyProperty<Long>) = join(property, Double::rem)

// Double - Float

operator fun ReadOnlyProperty<Double>.plus(property: ReadOnlyProperty<Float>) = join(property, Double::plus)

operator fun ReadOnlyProperty<Double>.minus(property: ReadOnlyProperty<Float>) = join(property, Double::minus)

operator fun ReadOnlyProperty<Double>.times(property: ReadOnlyProperty<Float>) = join(property, Double::times)

operator fun ReadOnlyProperty<Double>.div(property: ReadOnlyProperty<Float>) = join(property, Double::div)

operator fun ReadOnlyProperty<Double>.rem(property: ReadOnlyProperty<Float>) = join(property, Double::rem)

// Double - Double

operator fun ReadOnlyProperty<Double>.plus(property: ReadOnlyProperty<Double>) = join(property, Double::plus)

operator fun ReadOnlyProperty<Double>.minus(property: ReadOnlyProperty<Double>) = join(property, Double::minus)

operator fun ReadOnlyProperty<Double>.times(property: ReadOnlyProperty<Double>) = join(property, Double::times)

operator fun ReadOnlyProperty<Double>.div(property: ReadOnlyProperty<Double>) = join(property, Double::div)

operator fun ReadOnlyProperty<Double>.rem(property: ReadOnlyProperty<Double>) = join(property, Double::rem)

// Double - Short

operator fun ReadOnlyProperty<Double>.plus(property: ReadOnlyProperty<Short>) = join(property, Double::plus)

operator fun ReadOnlyProperty<Double>.minus(property: ReadOnlyProperty<Short>) = join(property, Double::minus)

operator fun ReadOnlyProperty<Double>.times(property: ReadOnlyProperty<Short>) = join(property, Double::times)

operator fun ReadOnlyProperty<Double>.div(property: ReadOnlyProperty<Short>) = join(property, Double::div)

operator fun ReadOnlyProperty<Double>.rem(property: ReadOnlyProperty<Short>) = join(property, Double::rem)

// Double - Byte

operator fun ReadOnlyProperty<Double>.plus(property: ReadOnlyProperty<Byte>) = join(property, Double::plus)

operator fun ReadOnlyProperty<Double>.minus(property: ReadOnlyProperty<Byte>) = join(property, Double::minus)

operator fun ReadOnlyProperty<Double>.times(property: ReadOnlyProperty<Byte>) = join(property, Double::times)

operator fun ReadOnlyProperty<Double>.div(property: ReadOnlyProperty<Byte>) = join(property, Double::div)

operator fun ReadOnlyProperty<Double>.rem(property: ReadOnlyProperty<Byte>) = join(property, Double::rem)

// Short - Int

operator fun ReadOnlyProperty<Short>.plus(property: ReadOnlyProperty<Int>) = join(property, Short::plus)

operator fun ReadOnlyProperty<Short>.minus(property: ReadOnlyProperty<Int>) = join(property, Short::minus)

operator fun ReadOnlyProperty<Short>.times(property: ReadOnlyProperty<Int>) = join(property, Short::times)

operator fun ReadOnlyProperty<Short>.div(property: ReadOnlyProperty<Int>) = join(property, Short::div)

operator fun ReadOnlyProperty<Short>.rem(property: ReadOnlyProperty<Int>) = join(property, Short::rem)

// Short - Long

operator fun ReadOnlyProperty<Short>.plus(property: ReadOnlyProperty<Long>) = join(property, Short::plus)

operator fun ReadOnlyProperty<Short>.minus(property: ReadOnlyProperty<Long>) = join(property, Short::minus)

operator fun ReadOnlyProperty<Short>.times(property: ReadOnlyProperty<Long>) = join(property, Short::times)

operator fun ReadOnlyProperty<Short>.div(property: ReadOnlyProperty<Long>) = join(property, Short::div)

operator fun ReadOnlyProperty<Short>.rem(property: ReadOnlyProperty<Long>) = join(property, Short::rem)

// Short - Float

operator fun ReadOnlyProperty<Short>.plus(property: ReadOnlyProperty<Float>) = join(property, Short::plus)

operator fun ReadOnlyProperty<Short>.minus(property: ReadOnlyProperty<Float>) = join(property, Short::minus)

operator fun ReadOnlyProperty<Short>.times(property: ReadOnlyProperty<Float>) = join(property, Short::times)

operator fun ReadOnlyProperty<Short>.div(property: ReadOnlyProperty<Float>) = join(property, Short::div)

operator fun ReadOnlyProperty<Short>.rem(property: ReadOnlyProperty<Float>) = join(property, Short::rem)

// Short - Double

operator fun ReadOnlyProperty<Short>.plus(property: ReadOnlyProperty<Double>) = join(property, Short::plus)

operator fun ReadOnlyProperty<Short>.minus(property: ReadOnlyProperty<Double>) = join(property, Short::minus)

operator fun ReadOnlyProperty<Short>.times(property: ReadOnlyProperty<Double>) = join(property, Short::times)

operator fun ReadOnlyProperty<Short>.div(property: ReadOnlyProperty<Double>) = join(property, Short::div)

operator fun ReadOnlyProperty<Short>.rem(property: ReadOnlyProperty<Double>) = join(property, Short::rem)

// Short - Short

operator fun ReadOnlyProperty<Short>.plus(property: ReadOnlyProperty<Short>) = join(property, Short::plus)

operator fun ReadOnlyProperty<Short>.minus(property: ReadOnlyProperty<Short>) = join(property, Short::minus)

operator fun ReadOnlyProperty<Short>.times(property: ReadOnlyProperty<Short>) = join(property, Short::times)

operator fun ReadOnlyProperty<Short>.div(property: ReadOnlyProperty<Short>) = join(property, Short::div)

operator fun ReadOnlyProperty<Short>.rem(property: ReadOnlyProperty<Short>) = join(property, Short::rem)

// Short - Byte

operator fun ReadOnlyProperty<Short>.plus(property: ReadOnlyProperty<Byte>) = join(property, Short::plus)

operator fun ReadOnlyProperty<Short>.minus(property: ReadOnlyProperty<Byte>) = join(property, Short::minus)

operator fun ReadOnlyProperty<Short>.times(property: ReadOnlyProperty<Byte>) = join(property, Short::times)

operator fun ReadOnlyProperty<Short>.div(property: ReadOnlyProperty<Byte>) = join(property, Short::div)

operator fun ReadOnlyProperty<Short>.rem(property: ReadOnlyProperty<Byte>) = join(property, Short::rem)

// Byte - Int

operator fun ReadOnlyProperty<Byte>.plus(property: ReadOnlyProperty<Int>) = join(property, Byte::plus)

operator fun ReadOnlyProperty<Byte>.minus(property: ReadOnlyProperty<Int>) = join(property, Byte::minus)

operator fun ReadOnlyProperty<Byte>.times(property: ReadOnlyProperty<Int>) = join(property, Byte::times)

operator fun ReadOnlyProperty<Byte>.div(property: ReadOnlyProperty<Int>) = join(property, Byte::div)

operator fun ReadOnlyProperty<Byte>.rem(property: ReadOnlyProperty<Int>) = join(property, Byte::rem)

// Byte - Long

operator fun ReadOnlyProperty<Byte>.plus(property: ReadOnlyProperty<Long>) = join(property, Byte::plus)

operator fun ReadOnlyProperty<Byte>.minus(property: ReadOnlyProperty<Long>) = join(property, Byte::minus)

operator fun ReadOnlyProperty<Byte>.times(property: ReadOnlyProperty<Long>) = join(property, Byte::times)

operator fun ReadOnlyProperty<Byte>.div(property: ReadOnlyProperty<Long>) = join(property, Byte::div)

operator fun ReadOnlyProperty<Byte>.rem(property: ReadOnlyProperty<Long>) = join(property, Byte::rem)

// Byte - Float

operator fun ReadOnlyProperty<Byte>.plus(property: ReadOnlyProperty<Float>) = join(property, Byte::plus)

operator fun ReadOnlyProperty<Byte>.minus(property: ReadOnlyProperty<Float>) = join(property, Byte::minus)

operator fun ReadOnlyProperty<Byte>.times(property: ReadOnlyProperty<Float>) = join(property, Byte::times)

operator fun ReadOnlyProperty<Byte>.div(property: ReadOnlyProperty<Float>) = join(property, Byte::div)

operator fun ReadOnlyProperty<Byte>.rem(property: ReadOnlyProperty<Float>) = join(property, Byte::rem)

// Byte - Double

operator fun ReadOnlyProperty<Byte>.plus(property: ReadOnlyProperty<Double>) = join(property, Byte::plus)

operator fun ReadOnlyProperty<Byte>.minus(property: ReadOnlyProperty<Double>) = join(property, Byte::minus)

operator fun ReadOnlyProperty<Byte>.times(property: ReadOnlyProperty<Double>) = join(property, Byte::times)

operator fun ReadOnlyProperty<Byte>.div(property: ReadOnlyProperty<Double>) = join(property, Byte::div)

operator fun ReadOnlyProperty<Byte>.rem(property: ReadOnlyProperty<Double>) = join(property, Byte::rem)

// Byte - Short

operator fun ReadOnlyProperty<Byte>.plus(property: ReadOnlyProperty<Short>) = join(property, Byte::plus)

operator fun ReadOnlyProperty<Byte>.minus(property: ReadOnlyProperty<Short>) = join(property, Byte::minus)

operator fun ReadOnlyProperty<Byte>.times(property: ReadOnlyProperty<Short>) = join(property, Byte::times)

operator fun ReadOnlyProperty<Byte>.div(property: ReadOnlyProperty<Short>) = join(property, Byte::div)

operator fun ReadOnlyProperty<Byte>.rem(property: ReadOnlyProperty<Short>) = join(property, Byte::rem)

// Byte - Byte

operator fun ReadOnlyProperty<Byte>.plus(property: ReadOnlyProperty<Byte>) = join(property, Byte::plus)

operator fun ReadOnlyProperty<Byte>.minus(property: ReadOnlyProperty<Byte>) = join(property, Byte::minus)

operator fun ReadOnlyProperty<Byte>.times(property: ReadOnlyProperty<Byte>) = join(property, Byte::times)

operator fun ReadOnlyProperty<Byte>.div(property: ReadOnlyProperty<Byte>) = join(property, Byte::div)

operator fun ReadOnlyProperty<Byte>.rem(property: ReadOnlyProperty<Byte>) = join(property, Byte::rem)

/*
 * Property - primitive
 */

// Int - Int

operator fun ReadOnlyProperty<Int>.plus(value: Int) = mapBinding { it + value }

operator fun ReadOnlyProperty<Int>.minus(value: Int) = mapBinding { it - value }

operator fun ReadOnlyProperty<Int>.times(value: Int) = mapBinding { it * value }

operator fun ReadOnlyProperty<Int>.div(value: Int) = mapBinding { it / value }

operator fun ReadOnlyProperty<Int>.rem(value: Int) = mapBinding { it % value }

// Int - Long

operator fun ReadOnlyProperty<Int>.plus(value: Long) = mapBinding { it + value }

operator fun ReadOnlyProperty<Int>.minus(value: Long) = mapBinding { it - value }

operator fun ReadOnlyProperty<Int>.times(value: Long) = mapBinding { it * value }

operator fun ReadOnlyProperty<Int>.div(value: Long) = mapBinding { it / value }

operator fun ReadOnlyProperty<Int>.rem(value: Long) = mapBinding { it % value }

// Int - Float

operator fun ReadOnlyProperty<Int>.plus(value: Float) = mapBinding { it + value }

operator fun ReadOnlyProperty<Int>.minus(value: Float) = mapBinding { it - value }

operator fun ReadOnlyProperty<Int>.times(value: Float) = mapBinding { it * value }

operator fun ReadOnlyProperty<Int>.div(value: Float) = mapBinding { it / value }

operator fun ReadOnlyProperty<Int>.rem(value: Float) = mapBinding { it % value }

// Int - Double

operator fun ReadOnlyProperty<Int>.plus(value: Double) = mapBinding { it + value }

operator fun ReadOnlyProperty<Int>.minus(value: Double) = mapBinding { it - value }

operator fun ReadOnlyProperty<Int>.times(value: Double) = mapBinding { it * value }

operator fun ReadOnlyProperty<Int>.div(value: Double) = mapBinding { it / value }

operator fun ReadOnlyProperty<Int>.rem(value: Double) = mapBinding { it % value }

// Int - Short

operator fun ReadOnlyProperty<Int>.plus(value: Short) = mapBinding { it + value }

operator fun ReadOnlyProperty<Int>.minus(value: Short) = mapBinding { it - value }

operator fun ReadOnlyProperty<Int>.times(value: Short) = mapBinding { it * value }

operator fun ReadOnlyProperty<Int>.div(value: Short) = mapBinding { it / value }

operator fun ReadOnlyProperty<Int>.rem(value: Short) = mapBinding { it % value }

// Int - Byte

operator fun ReadOnlyProperty<Int>.plus(value: Byte) = mapBinding { it + value }

operator fun ReadOnlyProperty<Int>.minus(value: Byte) = mapBinding { it - value }

operator fun ReadOnlyProperty<Int>.times(value: Byte) = mapBinding { it * value }

operator fun ReadOnlyProperty<Int>.div(value: Byte) = mapBinding { it / value }

operator fun ReadOnlyProperty<Int>.rem(value: Byte) = mapBinding { it % value }

// Long - Int

operator fun ReadOnlyProperty<Long>.plus(value: Int) = mapBinding { it + value }

operator fun ReadOnlyProperty<Long>.minus(value: Int) = mapBinding { it - value }

operator fun ReadOnlyProperty<Long>.times(value: Int) = mapBinding { it * value }

operator fun ReadOnlyProperty<Long>.div(value: Int) = mapBinding { it / value }

operator fun ReadOnlyProperty<Long>.rem(value: Int) = mapBinding { it % value }

// Long - Long

operator fun ReadOnlyProperty<Long>.plus(value: Long) = mapBinding { it + value }

operator fun ReadOnlyProperty<Long>.minus(value: Long) = mapBinding { it - value }

operator fun ReadOnlyProperty<Long>.times(value: Long) = mapBinding { it * value }

operator fun ReadOnlyProperty<Long>.div(value: Long) = mapBinding { it / value }

operator fun ReadOnlyProperty<Long>.rem(value: Long) = mapBinding { it % value }

// Long - Float

operator fun ReadOnlyProperty<Long>.plus(value: Float) = mapBinding { it + value }

operator fun ReadOnlyProperty<Long>.minus(value: Float) = mapBinding { it - value }

operator fun ReadOnlyProperty<Long>.times(value: Float) = mapBinding { it * value }

operator fun ReadOnlyProperty<Long>.div(value: Float) = mapBinding { it / value }

operator fun ReadOnlyProperty<Long>.rem(value: Float) = mapBinding { it % value }

// Long - Double

operator fun ReadOnlyProperty<Long>.plus(value: Double) = mapBinding { it + value }

operator fun ReadOnlyProperty<Long>.minus(value: Double) = mapBinding { it - value }

operator fun ReadOnlyProperty<Long>.times(value: Double) = mapBinding { it * value }

operator fun ReadOnlyProperty<Long>.div(value: Double) = mapBinding { it / value }

operator fun ReadOnlyProperty<Long>.rem(value: Double) = mapBinding { it % value }

// Long - Short

operator fun ReadOnlyProperty<Long>.plus(value: Short) = mapBinding { it + value }

operator fun ReadOnlyProperty<Long>.minus(value: Short) = mapBinding { it - value }

operator fun ReadOnlyProperty<Long>.times(value: Short) = mapBinding { it * value }

operator fun ReadOnlyProperty<Long>.div(value: Short) = mapBinding { it / value }

operator fun ReadOnlyProperty<Long>.rem(value: Short) = mapBinding { it % value }

// Long - Byte

operator fun ReadOnlyProperty<Long>.plus(value: Byte) = mapBinding { it + value }

operator fun ReadOnlyProperty<Long>.minus(value: Byte) = mapBinding { it - value }

operator fun ReadOnlyProperty<Long>.times(value: Byte) = mapBinding { it * value }

operator fun ReadOnlyProperty<Long>.div(value: Byte) = mapBinding { it / value }

operator fun ReadOnlyProperty<Long>.rem(value: Byte) = mapBinding { it % value }

// Float - Int

operator fun ReadOnlyProperty<Float>.plus(value: Int) = mapBinding { it + value }

operator fun ReadOnlyProperty<Float>.minus(value: Int) = mapBinding { it - value }

operator fun ReadOnlyProperty<Float>.times(value: Int) = mapBinding { it * value }

operator fun ReadOnlyProperty<Float>.div(value: Int) = mapBinding { it / value }

operator fun ReadOnlyProperty<Float>.rem(value: Int) = mapBinding { it % value }

// Float - Long

operator fun ReadOnlyProperty<Float>.plus(value: Long) = mapBinding { it + value }

operator fun ReadOnlyProperty<Float>.minus(value: Long) = mapBinding { it - value }

operator fun ReadOnlyProperty<Float>.times(value: Long) = mapBinding { it * value }

operator fun ReadOnlyProperty<Float>.div(value: Long) = mapBinding { it / value }

operator fun ReadOnlyProperty<Float>.rem(value: Long) = mapBinding { it % value }

// Float - Float

operator fun ReadOnlyProperty<Float>.plus(value: Float) = mapBinding { it + value }

operator fun ReadOnlyProperty<Float>.minus(value: Float) = mapBinding { it - value }

operator fun ReadOnlyProperty<Float>.times(value: Float) = mapBinding { it * value }

operator fun ReadOnlyProperty<Float>.div(value: Float) = mapBinding { it / value }

operator fun ReadOnlyProperty<Float>.rem(value: Float) = mapBinding { it % value }

// Float - Double

operator fun ReadOnlyProperty<Float>.plus(value: Double) = mapBinding { it + value }

operator fun ReadOnlyProperty<Float>.minus(value: Double) = mapBinding { it - value }

operator fun ReadOnlyProperty<Float>.times(value: Double) = mapBinding { it * value }

operator fun ReadOnlyProperty<Float>.div(value: Double) = mapBinding { it / value }

operator fun ReadOnlyProperty<Float>.rem(value: Double) = mapBinding { it % value }

// Float - Short

operator fun ReadOnlyProperty<Float>.plus(value: Short) = mapBinding { it + value }

operator fun ReadOnlyProperty<Float>.minus(value: Short) = mapBinding { it - value }

operator fun ReadOnlyProperty<Float>.times(value: Short) = mapBinding { it * value }

operator fun ReadOnlyProperty<Float>.div(value: Short) = mapBinding { it / value }

operator fun ReadOnlyProperty<Float>.rem(value: Short) = mapBinding { it % value }

// Float - Byte

operator fun ReadOnlyProperty<Float>.plus(value: Byte) = mapBinding { it + value }

operator fun ReadOnlyProperty<Float>.minus(value: Byte) = mapBinding { it - value }

operator fun ReadOnlyProperty<Float>.times(value: Byte) = mapBinding { it * value }

operator fun ReadOnlyProperty<Float>.div(value: Byte) = mapBinding { it / value }

operator fun ReadOnlyProperty<Float>.rem(value: Byte) = mapBinding { it % value }

// Double - Int

operator fun ReadOnlyProperty<Double>.plus(value: Int) = mapBinding { it + value }

operator fun ReadOnlyProperty<Double>.minus(value: Int) = mapBinding { it - value }

operator fun ReadOnlyProperty<Double>.times(value: Int) = mapBinding { it * value }

operator fun ReadOnlyProperty<Double>.div(value: Int) = mapBinding { it / value }

operator fun ReadOnlyProperty<Double>.rem(value: Int) = mapBinding { it % value }

// Double - Long

operator fun ReadOnlyProperty<Double>.plus(value: Long) = mapBinding { it + value }

operator fun ReadOnlyProperty<Double>.minus(value: Long) = mapBinding { it - value }

operator fun ReadOnlyProperty<Double>.times(value: Long) = mapBinding { it * value }

operator fun ReadOnlyProperty<Double>.div(value: Long) = mapBinding { it / value }

operator fun ReadOnlyProperty<Double>.rem(value: Long) = mapBinding { it % value }

// Double - Float

operator fun ReadOnlyProperty<Double>.plus(value: Float) = mapBinding { it + value }

operator fun ReadOnlyProperty<Double>.minus(value: Float) = mapBinding { it - value }

operator fun ReadOnlyProperty<Double>.times(value: Float) = mapBinding { it * value }

operator fun ReadOnlyProperty<Double>.div(value: Float) = mapBinding { it / value }

operator fun ReadOnlyProperty<Double>.rem(value: Float) = mapBinding { it % value }

// Double - Double

operator fun ReadOnlyProperty<Double>.plus(value: Double) = mapBinding { it + value }

operator fun ReadOnlyProperty<Double>.minus(value: Double) = mapBinding { it - value }

operator fun ReadOnlyProperty<Double>.times(value: Double) = mapBinding { it * value }

operator fun ReadOnlyProperty<Double>.div(value: Double) = mapBinding { it / value }

operator fun ReadOnlyProperty<Double>.rem(value: Double) = mapBinding { it % value }

// Double - Short

operator fun ReadOnlyProperty<Double>.plus(value: Short) = mapBinding { it + value }

operator fun ReadOnlyProperty<Double>.minus(value: Short) = mapBinding { it - value }

operator fun ReadOnlyProperty<Double>.times(value: Short) = mapBinding { it * value }

operator fun ReadOnlyProperty<Double>.div(value: Short) = mapBinding { it / value }

operator fun ReadOnlyProperty<Double>.rem(value: Short) = mapBinding { it % value }

// Double - Byte

operator fun ReadOnlyProperty<Double>.plus(value: Byte) = mapBinding { it + value }

operator fun ReadOnlyProperty<Double>.minus(value: Byte) = mapBinding { it - value }

operator fun ReadOnlyProperty<Double>.times(value: Byte) = mapBinding { it * value }

operator fun ReadOnlyProperty<Double>.div(value: Byte) = mapBinding { it / value }

operator fun ReadOnlyProperty<Double>.rem(value: Byte) = mapBinding { it % value }

// Short - Int

operator fun ReadOnlyProperty<Short>.plus(value: Int) = mapBinding { it + value }

operator fun ReadOnlyProperty<Short>.minus(value: Int) = mapBinding { it - value }

operator fun ReadOnlyProperty<Short>.times(value: Int) = mapBinding { it * value }

operator fun ReadOnlyProperty<Short>.div(value: Int) = mapBinding { it / value }

operator fun ReadOnlyProperty<Short>.rem(value: Int) = mapBinding { it % value }

// Short - Long

operator fun ReadOnlyProperty<Short>.plus(value: Long) = mapBinding { it + value }

operator fun ReadOnlyProperty<Short>.minus(value: Long) = mapBinding { it - value }

operator fun ReadOnlyProperty<Short>.times(value: Long) = mapBinding { it * value }

operator fun ReadOnlyProperty<Short>.div(value: Long) = mapBinding { it / value }

operator fun ReadOnlyProperty<Short>.rem(value: Long) = mapBinding { it % value }

// Short - Float

operator fun ReadOnlyProperty<Short>.plus(value: Float) = mapBinding { it + value }

operator fun ReadOnlyProperty<Short>.minus(value: Float) = mapBinding { it - value }

operator fun ReadOnlyProperty<Short>.times(value: Float) = mapBinding { it * value }

operator fun ReadOnlyProperty<Short>.div(value: Float) = mapBinding { it / value }

operator fun ReadOnlyProperty<Short>.rem(value: Float) = mapBinding { it % value }

// Short - Double

operator fun ReadOnlyProperty<Short>.plus(value: Double) = mapBinding { it + value }

operator fun ReadOnlyProperty<Short>.minus(value: Double) = mapBinding { it - value }

operator fun ReadOnlyProperty<Short>.times(value: Double) = mapBinding { it * value }

operator fun ReadOnlyProperty<Short>.div(value: Double) = mapBinding { it / value }

operator fun ReadOnlyProperty<Short>.rem(value: Double) = mapBinding { it % value }

// Short - Short

operator fun ReadOnlyProperty<Short>.plus(value: Short) = mapBinding { it + value }

operator fun ReadOnlyProperty<Short>.minus(value: Short) = mapBinding { it - value }

operator fun ReadOnlyProperty<Short>.times(value: Short) = mapBinding { it * value }

operator fun ReadOnlyProperty<Short>.div(value: Short) = mapBinding { it / value }

operator fun ReadOnlyProperty<Short>.rem(value: Short) = mapBinding { it % value }

// Short - Byte

operator fun ReadOnlyProperty<Short>.plus(value: Byte) = mapBinding { it + value }

operator fun ReadOnlyProperty<Short>.minus(value: Byte) = mapBinding { it - value }

operator fun ReadOnlyProperty<Short>.times(value: Byte) = mapBinding { it * value }

operator fun ReadOnlyProperty<Short>.div(value: Byte) = mapBinding { it / value }

operator fun ReadOnlyProperty<Short>.rem(value: Byte) = mapBinding { it % value }

// Byte - Int

operator fun ReadOnlyProperty<Byte>.plus(value: Int) = mapBinding { it + value }

operator fun ReadOnlyProperty<Byte>.minus(value: Int) = mapBinding { it - value }

operator fun ReadOnlyProperty<Byte>.times(value: Int) = mapBinding { it * value }

operator fun ReadOnlyProperty<Byte>.div(value: Int) = mapBinding { it / value }

operator fun ReadOnlyProperty<Byte>.rem(value: Int) = mapBinding { it % value }

// Byte - Long

operator fun ReadOnlyProperty<Byte>.plus(value: Long) = mapBinding { it + value }

operator fun ReadOnlyProperty<Byte>.minus(value: Long) = mapBinding { it - value }

operator fun ReadOnlyProperty<Byte>.times(value: Long) = mapBinding { it * value }

operator fun ReadOnlyProperty<Byte>.div(value: Long) = mapBinding { it / value }

operator fun ReadOnlyProperty<Byte>.rem(value: Long) = mapBinding { it % value }

// Byte - Float

operator fun ReadOnlyProperty<Byte>.plus(value: Float) = mapBinding { it + value }

operator fun ReadOnlyProperty<Byte>.minus(value: Float) = mapBinding { it - value }

operator fun ReadOnlyProperty<Byte>.times(value: Float) = mapBinding { it * value }

operator fun ReadOnlyProperty<Byte>.div(value: Float) = mapBinding { it / value }

operator fun ReadOnlyProperty<Byte>.rem(value: Float) = mapBinding { it % value }

// Byte - Double

operator fun ReadOnlyProperty<Byte>.plus(value: Double) = mapBinding { it + value }

operator fun ReadOnlyProperty<Byte>.minus(value: Double) = mapBinding { it - value }

operator fun ReadOnlyProperty<Byte>.times(value: Double) = mapBinding { it * value }

operator fun ReadOnlyProperty<Byte>.div(value: Double) = mapBinding { it / value }

operator fun ReadOnlyProperty<Byte>.rem(value: Double) = mapBinding { it % value }

// Byte - Short

operator fun ReadOnlyProperty<Byte>.plus(value: Short) = mapBinding { it + value }

operator fun ReadOnlyProperty<Byte>.minus(value: Short) = mapBinding { it - value }

operator fun ReadOnlyProperty<Byte>.times(value: Short) = mapBinding { it * value }

operator fun ReadOnlyProperty<Byte>.div(value: Short) = mapBinding { it / value }

operator fun ReadOnlyProperty<Byte>.rem(value: Short) = mapBinding { it % value }

// Byte - Byte

operator fun ReadOnlyProperty<Byte>.plus(value: Byte) = mapBinding { it + value }

operator fun ReadOnlyProperty<Byte>.minus(value: Byte) = mapBinding { it - value }

operator fun ReadOnlyProperty<Byte>.times(value: Byte) = mapBinding { it * value }

operator fun ReadOnlyProperty<Byte>.div(value: Byte) = mapBinding { it / value }

operator fun ReadOnlyProperty<Byte>.rem(value: Byte) = mapBinding { it % value }

/*
 * primitive - Property
 */

// Int - Int

operator fun Int.plus(property: ReadOnlyProperty<Int>) = property.mapBinding { this + it }

operator fun Int.minus(property: ReadOnlyProperty<Int>) = property.mapBinding { this - it }

operator fun Int.times(property: ReadOnlyProperty<Int>) = property.mapBinding { this * it }

operator fun Int.div(property: ReadOnlyProperty<Int>) = property.mapBinding { this / it }

operator fun Int.rem(property: ReadOnlyProperty<Int>) = property.mapBinding { this % it }

// Int - Long

operator fun Int.plus(property: ReadOnlyProperty<Long>) = property.mapBinding { this + it }

operator fun Int.minus(property: ReadOnlyProperty<Long>) = property.mapBinding { this - it }

operator fun Int.times(property: ReadOnlyProperty<Long>) = property.mapBinding { this * it }

operator fun Int.div(property: ReadOnlyProperty<Long>) = property.mapBinding { this / it }

operator fun Int.rem(property: ReadOnlyProperty<Long>) = property.mapBinding { this % it }

// Int - Float

operator fun Int.plus(property: ReadOnlyProperty<Float>) = property.mapBinding { this + it }

operator fun Int.minus(property: ReadOnlyProperty<Float>) = property.mapBinding { this - it }

operator fun Int.times(property: ReadOnlyProperty<Float>) = property.mapBinding { this * it }

operator fun Int.div(property: ReadOnlyProperty<Float>) = property.mapBinding { this / it }

operator fun Int.rem(property: ReadOnlyProperty<Float>) = property.mapBinding { this % it }

// Int - Double

operator fun Int.plus(property: ReadOnlyProperty<Double>) = property.mapBinding { this + it }

operator fun Int.minus(property: ReadOnlyProperty<Double>) = property.mapBinding { this - it }

operator fun Int.times(property: ReadOnlyProperty<Double>) = property.mapBinding { this * it }

operator fun Int.div(property: ReadOnlyProperty<Double>) = property.mapBinding { this / it }

operator fun Int.rem(property: ReadOnlyProperty<Double>) = property.mapBinding { this % it }

// Int - Short

operator fun Int.plus(property: ReadOnlyProperty<Short>) = property.mapBinding { this + it }

operator fun Int.minus(property: ReadOnlyProperty<Short>) = property.mapBinding { this - it }

operator fun Int.times(property: ReadOnlyProperty<Short>) = property.mapBinding { this * it }

operator fun Int.div(property: ReadOnlyProperty<Short>) = property.mapBinding { this / it }

operator fun Int.rem(property: ReadOnlyProperty<Short>) = property.mapBinding { this % it }

// Int - Byte

operator fun Int.plus(property: ReadOnlyProperty<Byte>) = property.mapBinding { this + it }

operator fun Int.minus(property: ReadOnlyProperty<Byte>) = property.mapBinding { this - it }

operator fun Int.times(property: ReadOnlyProperty<Byte>) = property.mapBinding { this * it }

operator fun Int.div(property: ReadOnlyProperty<Byte>) = property.mapBinding { this / it }

operator fun Int.rem(property: ReadOnlyProperty<Byte>) = property.mapBinding { this % it }

// Long - Int

operator fun Long.plus(property: ReadOnlyProperty<Int>) = property.mapBinding { this + it }

operator fun Long.minus(property: ReadOnlyProperty<Int>) = property.mapBinding { this - it }

operator fun Long.times(property: ReadOnlyProperty<Int>) = property.mapBinding { this * it }

operator fun Long.div(property: ReadOnlyProperty<Int>) = property.mapBinding { this / it }

operator fun Long.rem(property: ReadOnlyProperty<Int>) = property.mapBinding { this % it }

// Long - Long

operator fun Long.plus(property: ReadOnlyProperty<Long>) = property.mapBinding { this + it }

operator fun Long.minus(property: ReadOnlyProperty<Long>) = property.mapBinding { this - it }

operator fun Long.times(property: ReadOnlyProperty<Long>) = property.mapBinding { this * it }

operator fun Long.div(property: ReadOnlyProperty<Long>) = property.mapBinding { this / it }

operator fun Long.rem(property: ReadOnlyProperty<Long>) = property.mapBinding { this % it }

// Long - Float

operator fun Long.plus(property: ReadOnlyProperty<Float>) = property.mapBinding { this + it }

operator fun Long.minus(property: ReadOnlyProperty<Float>) = property.mapBinding { this - it }

operator fun Long.times(property: ReadOnlyProperty<Float>) = property.mapBinding { this * it }

operator fun Long.div(property: ReadOnlyProperty<Float>) = property.mapBinding { this / it }

operator fun Long.rem(property: ReadOnlyProperty<Float>) = property.mapBinding { this % it }

// Long - Double

operator fun Long.plus(property: ReadOnlyProperty<Double>) = property.mapBinding { this + it }

operator fun Long.minus(property: ReadOnlyProperty<Double>) = property.mapBinding { this - it }

operator fun Long.times(property: ReadOnlyProperty<Double>) = property.mapBinding { this * it }

operator fun Long.div(property: ReadOnlyProperty<Double>) = property.mapBinding { this / it }

operator fun Long.rem(property: ReadOnlyProperty<Double>) = property.mapBinding { this % it }

// Long - Short

operator fun Long.plus(property: ReadOnlyProperty<Short>) = property.mapBinding { this + it }

operator fun Long.minus(property: ReadOnlyProperty<Short>) = property.mapBinding { this - it }

operator fun Long.times(property: ReadOnlyProperty<Short>) = property.mapBinding { this * it }

operator fun Long.div(property: ReadOnlyProperty<Short>) = property.mapBinding { this / it }

operator fun Long.rem(property: ReadOnlyProperty<Short>) = property.mapBinding { this % it }

// Long - Byte

operator fun Long.plus(property: ReadOnlyProperty<Byte>) = property.mapBinding { this + it }

operator fun Long.minus(property: ReadOnlyProperty<Byte>) = property.mapBinding { this - it }

operator fun Long.times(property: ReadOnlyProperty<Byte>) = property.mapBinding { this * it }

operator fun Long.div(property: ReadOnlyProperty<Byte>) = property.mapBinding { this / it }

operator fun Long.rem(property: ReadOnlyProperty<Byte>) = property.mapBinding { this % it }

// Float - Int

operator fun Float.plus(property: ReadOnlyProperty<Int>) = property.mapBinding { this + it }

operator fun Float.minus(property: ReadOnlyProperty<Int>) = property.mapBinding { this - it }

operator fun Float.times(property: ReadOnlyProperty<Int>) = property.mapBinding { this * it }

operator fun Float.div(property: ReadOnlyProperty<Int>) = property.mapBinding { this / it }

operator fun Float.rem(property: ReadOnlyProperty<Int>) = property.mapBinding { this % it }

// Float - Long

operator fun Float.plus(property: ReadOnlyProperty<Long>) = property.mapBinding { this + it }

operator fun Float.minus(property: ReadOnlyProperty<Long>) = property.mapBinding { this - it }

operator fun Float.times(property: ReadOnlyProperty<Long>) = property.mapBinding { this * it }

operator fun Float.div(property: ReadOnlyProperty<Long>) = property.mapBinding { this / it }

operator fun Float.rem(property: ReadOnlyProperty<Long>) = property.mapBinding { this % it }

// Float - Float

operator fun Float.plus(property: ReadOnlyProperty<Float>) = property.mapBinding { this + it }

operator fun Float.minus(property: ReadOnlyProperty<Float>) = property.mapBinding { this - it }

operator fun Float.times(property: ReadOnlyProperty<Float>) = property.mapBinding { this * it }

operator fun Float.div(property: ReadOnlyProperty<Float>) = property.mapBinding { this / it }

operator fun Float.rem(property: ReadOnlyProperty<Float>) = property.mapBinding { this % it }

// Float - Double

operator fun Float.plus(property: ReadOnlyProperty<Double>) = property.mapBinding { this + it }

operator fun Float.minus(property: ReadOnlyProperty<Double>) = property.mapBinding { this - it }

operator fun Float.times(property: ReadOnlyProperty<Double>) = property.mapBinding { this * it }

operator fun Float.div(property: ReadOnlyProperty<Double>) = property.mapBinding { this / it }

operator fun Float.rem(property: ReadOnlyProperty<Double>) = property.mapBinding { this % it }

// Float - Short

operator fun Float.plus(property: ReadOnlyProperty<Short>) = property.mapBinding { this + it }

operator fun Float.minus(property: ReadOnlyProperty<Short>) = property.mapBinding { this - it }

operator fun Float.times(property: ReadOnlyProperty<Short>) = property.mapBinding { this * it }

operator fun Float.div(property: ReadOnlyProperty<Short>) = property.mapBinding { this / it }

operator fun Float.rem(property: ReadOnlyProperty<Short>) = property.mapBinding { this % it }

// Float - Byte

operator fun Float.plus(property: ReadOnlyProperty<Byte>) = property.mapBinding { this + it }

operator fun Float.minus(property: ReadOnlyProperty<Byte>) = property.mapBinding { this - it }

operator fun Float.times(property: ReadOnlyProperty<Byte>) = property.mapBinding { this * it }

operator fun Float.div(property: ReadOnlyProperty<Byte>) = property.mapBinding { this / it }

operator fun Float.rem(property: ReadOnlyProperty<Byte>) = property.mapBinding { this % it }

// Double - Int

operator fun Double.plus(property: ReadOnlyProperty<Int>) = property.mapBinding { this + it }

operator fun Double.minus(property: ReadOnlyProperty<Int>) = property.mapBinding { this - it }

operator fun Double.times(property: ReadOnlyProperty<Int>) = property.mapBinding { this * it }

operator fun Double.div(property: ReadOnlyProperty<Int>) = property.mapBinding { this / it }

operator fun Double.rem(property: ReadOnlyProperty<Int>) = property.mapBinding { this % it }

// Double - Long

operator fun Double.plus(property: ReadOnlyProperty<Long>) = property.mapBinding { this + it }

operator fun Double.minus(property: ReadOnlyProperty<Long>) = property.mapBinding { this - it }

operator fun Double.times(property: ReadOnlyProperty<Long>) = property.mapBinding { this * it }

operator fun Double.div(property: ReadOnlyProperty<Long>) = property.mapBinding { this / it }

operator fun Double.rem(property: ReadOnlyProperty<Long>) = property.mapBinding { this % it }

// Double - Float

operator fun Double.plus(property: ReadOnlyProperty<Float>) = property.mapBinding { this + it }

operator fun Double.minus(property: ReadOnlyProperty<Float>) = property.mapBinding { this - it }

operator fun Double.times(property: ReadOnlyProperty<Float>) = property.mapBinding { this * it }

operator fun Double.div(property: ReadOnlyProperty<Float>) = property.mapBinding { this / it }

operator fun Double.rem(property: ReadOnlyProperty<Float>) = property.mapBinding { this % it }

// Double - Double

operator fun Double.plus(property: ReadOnlyProperty<Double>) = property.mapBinding { this + it }

operator fun Double.minus(property: ReadOnlyProperty<Double>) = property.mapBinding { this - it }

operator fun Double.times(property: ReadOnlyProperty<Double>) = property.mapBinding { this * it }

operator fun Double.div(property: ReadOnlyProperty<Double>) = property.mapBinding { this / it }

operator fun Double.rem(property: ReadOnlyProperty<Double>) = property.mapBinding { this % it }

// Double - Short

operator fun Double.plus(property: ReadOnlyProperty<Short>) = property.mapBinding { this + it }

operator fun Double.minus(property: ReadOnlyProperty<Short>) = property.mapBinding { this - it }

operator fun Double.times(property: ReadOnlyProperty<Short>) = property.mapBinding { this * it }

operator fun Double.div(property: ReadOnlyProperty<Short>) = property.mapBinding { this / it }

operator fun Double.rem(property: ReadOnlyProperty<Short>) = property.mapBinding { this % it }

// Double - Byte

operator fun Double.plus(property: ReadOnlyProperty<Byte>) = property.mapBinding { this + it }

operator fun Double.minus(property: ReadOnlyProperty<Byte>) = property.mapBinding { this - it }

operator fun Double.times(property: ReadOnlyProperty<Byte>) = property.mapBinding { this * it }

operator fun Double.div(property: ReadOnlyProperty<Byte>) = property.mapBinding { this / it }

operator fun Double.rem(property: ReadOnlyProperty<Byte>) = property.mapBinding { this % it }

// Short - Int

operator fun Short.plus(property: ReadOnlyProperty<Int>) = property.mapBinding { this + it }

operator fun Short.minus(property: ReadOnlyProperty<Int>) = property.mapBinding { this - it }

operator fun Short.times(property: ReadOnlyProperty<Int>) = property.mapBinding { this * it }

operator fun Short.div(property: ReadOnlyProperty<Int>) = property.mapBinding { this / it }

operator fun Short.rem(property: ReadOnlyProperty<Int>) = property.mapBinding { this % it }

// Short - Long

operator fun Short.plus(property: ReadOnlyProperty<Long>) = property.mapBinding { this + it }

operator fun Short.minus(property: ReadOnlyProperty<Long>) = property.mapBinding { this - it }

operator fun Short.times(property: ReadOnlyProperty<Long>) = property.mapBinding { this * it }

operator fun Short.div(property: ReadOnlyProperty<Long>) = property.mapBinding { this / it }

operator fun Short.rem(property: ReadOnlyProperty<Long>) = property.mapBinding { this % it }

// Short - Float

operator fun Short.plus(property: ReadOnlyProperty<Float>) = property.mapBinding { this + it }

operator fun Short.minus(property: ReadOnlyProperty<Float>) = property.mapBinding { this - it }

operator fun Short.times(property: ReadOnlyProperty<Float>) = property.mapBinding { this * it }

operator fun Short.div(property: ReadOnlyProperty<Float>) = property.mapBinding { this / it }

operator fun Short.rem(property: ReadOnlyProperty<Float>) = property.mapBinding { this % it }

// Short - Double

operator fun Short.plus(property: ReadOnlyProperty<Double>) = property.mapBinding { this + it }

operator fun Short.minus(property: ReadOnlyProperty<Double>) = property.mapBinding { this - it }

operator fun Short.times(property: ReadOnlyProperty<Double>) = property.mapBinding { this * it }

operator fun Short.div(property: ReadOnlyProperty<Double>) = property.mapBinding { this / it }

operator fun Short.rem(property: ReadOnlyProperty<Double>) = property.mapBinding { this % it }

// Short - Short

operator fun Short.plus(property: ReadOnlyProperty<Short>) = property.mapBinding { this + it }

operator fun Short.minus(property: ReadOnlyProperty<Short>) = property.mapBinding { this - it }

operator fun Short.times(property: ReadOnlyProperty<Short>) = property.mapBinding { this * it }

operator fun Short.div(property: ReadOnlyProperty<Short>) = property.mapBinding { this / it }

operator fun Short.rem(property: ReadOnlyProperty<Short>) = property.mapBinding { this % it }

// Short - Byte

operator fun Short.plus(property: ReadOnlyProperty<Byte>) = property.mapBinding { this + it }

operator fun Short.minus(property: ReadOnlyProperty<Byte>) = property.mapBinding { this - it }

operator fun Short.times(property: ReadOnlyProperty<Byte>) = property.mapBinding { this * it }

operator fun Short.div(property: ReadOnlyProperty<Byte>) = property.mapBinding { this / it }

operator fun Short.rem(property: ReadOnlyProperty<Byte>) = property.mapBinding { this % it }

// Byte - Int

operator fun Byte.plus(property: ReadOnlyProperty<Int>) = property.mapBinding { this + it }

operator fun Byte.minus(property: ReadOnlyProperty<Int>) = property.mapBinding { this - it }

operator fun Byte.times(property: ReadOnlyProperty<Int>) = property.mapBinding { this * it }

operator fun Byte.div(property: ReadOnlyProperty<Int>) = property.mapBinding { this / it }

operator fun Byte.rem(property: ReadOnlyProperty<Int>) = property.mapBinding { this % it }

// Byte - Long

operator fun Byte.plus(property: ReadOnlyProperty<Long>) = property.mapBinding { this + it }

operator fun Byte.minus(property: ReadOnlyProperty<Long>) = property.mapBinding { this - it }

operator fun Byte.times(property: ReadOnlyProperty<Long>) = property.mapBinding { this * it }

operator fun Byte.div(property: ReadOnlyProperty<Long>) = property.mapBinding { this / it }

operator fun Byte.rem(property: ReadOnlyProperty<Long>) = property.mapBinding { this % it }

// Byte - Float

operator fun Byte.plus(property: ReadOnlyProperty<Float>) = property.mapBinding { this + it }

operator fun Byte.minus(property: ReadOnlyProperty<Float>) = property.mapBinding { this - it }

operator fun Byte.times(property: ReadOnlyProperty<Float>) = property.mapBinding { this * it }

operator fun Byte.div(property: ReadOnlyProperty<Float>) = property.mapBinding { this / it }

operator fun Byte.rem(property: ReadOnlyProperty<Float>) = property.mapBinding { this % it }

// Byte - Double

operator fun Byte.plus(property: ReadOnlyProperty<Double>) = property.mapBinding { this + it }

operator fun Byte.minus(property: ReadOnlyProperty<Double>) = property.mapBinding { this - it }

operator fun Byte.times(property: ReadOnlyProperty<Double>) = property.mapBinding { this * it }

operator fun Byte.div(property: ReadOnlyProperty<Double>) = property.mapBinding { this / it }

operator fun Byte.rem(property: ReadOnlyProperty<Double>) = property.mapBinding { this % it }

// Byte - Short

operator fun Byte.plus(property: ReadOnlyProperty<Short>) = property.mapBinding { this + it }

operator fun Byte.minus(property: ReadOnlyProperty<Short>) = property.mapBinding { this - it }

operator fun Byte.times(property: ReadOnlyProperty<Short>) = property.mapBinding { this * it }

operator fun Byte.div(property: ReadOnlyProperty<Short>) = property.mapBinding { this / it }

operator fun Byte.rem(property: ReadOnlyProperty<Short>) = property.mapBinding { this % it }

// Byte - Byte

operator fun Byte.plus(property: ReadOnlyProperty<Byte>) = property.mapBinding { this + it }

operator fun Byte.minus(property: ReadOnlyProperty<Byte>) = property.mapBinding { this - it }

operator fun Byte.times(property: ReadOnlyProperty<Byte>) = property.mapBinding { this * it }

operator fun Byte.div(property: ReadOnlyProperty<Byte>) = property.mapBinding { this / it }

operator fun Byte.rem(property: ReadOnlyProperty<Byte>) = property.mapBinding { this % it }
