package io.framed.framework.pictogram

interface ViewModelHandler {

    fun canConnectionStart(source: Shape): Boolean
    fun canConnectionCreate(source: Shape, target: Shape): Boolean
    fun createConnection(source: Shape, target: Shape)

    fun canDropShape(shape: Shape, target: Shape): Boolean
    fun dropShape(shape: Shape, target: Shape)
}