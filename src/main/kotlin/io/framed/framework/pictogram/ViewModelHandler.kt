package io.framed.framework.pictogram

interface ViewModelHandler {

    fun canConnectionStart(source: Long): Boolean
    fun canConnectionCreate(source: Long, target: Long): Boolean
    fun createConnection(source: Long, target: Long)

    fun canDropShape(shape: Long, target: Long): Boolean
    fun dropShape(shape: Long, target: Long)

    fun delete(shapes: List<Long>)
    fun copy(shapes: List<Long>)
    fun cut(shapes: List<Long>)
    fun paste(target: Long)
}