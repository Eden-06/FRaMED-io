package io.framed.linker

import de.westermann.kobserve.EventHandler
import io.framed.framework.ConnectionLinker
import io.framed.framework.ConnectionManager
import io.framed.framework.ModelConnection
import io.framed.framework.ModelLinker
import io.framed.framework.pictogram.ConnectionInfo
import io.framed.framework.util.LinkerConnectionBox
import io.framed.framework.view.CyclicChooser
import io.framed.framework.view.iconView
import io.framed.framework.view.textView
import io.framed.model.*

class ConnectionManagerLinker(val modelConnections: Connections) : ConnectionManager {

    override var modelLinkers: Set<ModelLinker<*, *, *>> = emptySet()

    override val connections: Set<ConnectionLinker<*>>
        get() = connectionBox.linkers

    private val connectionBox = LinkerConnectionBox(modelConnections::connections, this)

    override fun addModel(modelLinker: ModelLinker<*, *, *>) {
        modelLinkers += modelLinker
    }

    override fun add(model: ModelConnection<*>) {
        when (model) {
            is Relationship -> connectionBox += RelationshipLinker(model, this)
            is Composition -> connectionBox += CompositionLinker(model, this)
            is Inheritance -> connectionBox += InheritanceLinker(model, this)
            is Fulfillment -> connectionBox += FulfillmentLinker(model, this)
            is CreateRelationship -> connectionBox += CreateRelationshipLinker(model, this)
            is DestroyRelationship -> connectionBox += DestroyRelationshipLinker(model, this)
        }
    }

    override fun remove(linker: ConnectionLinker<*>) {
        when (linker) {
            is RelationshipLinker -> connectionBox -= linker
            is CompositionLinker -> connectionBox -= linker
            is InheritanceLinker -> connectionBox -= linker
            is FulfillmentLinker -> connectionBox -= linker
            is CreateRelationshipLinker -> connectionBox -= linker
            is DestroyRelationshipLinker -> connectionBox -= linker
        }
    }

    override val onConnectionAdd = EventHandler<ConnectionLinker<*>>()
    override val onConnectionRemove = EventHandler<ConnectionLinker<*>>()

    override fun createConnection(source: Long, target: Long) {
        val types = canConnectionCreate(source, target)
        if (types.isEmpty()) return

        if (types.size == 1) {
            createConnection(source, target, types.first())
        } else {
            CyclicChooser(types) { type ->
                iconView(type.icon)
                textView(type.name)

                onClick {
                    createConnection(source, target, type)
                }
            }
        }
    }


    override fun createConnection(source: Long, target: Long, type: ConnectionInfo): ConnectionLinker<*> {
        return when (type) {
            RelationshipLinker.info -> RelationshipLinker(Relationship(source, target), this).also(connectionBox::add)
            InheritanceLinker.info -> InheritanceLinker(Inheritance(source, target), this).also(connectionBox::add)
            CompositionLinker.info -> CompositionLinker(Composition(source, target), this).also(connectionBox::add)
            FulfillmentLinker.info -> FulfillmentLinker(Fulfillment(source, target), this).also(connectionBox::add)
            CreateRelationshipLinker.info -> CreateRelationshipLinker(CreateRelationship(source, target), this).also(connectionBox::add)
            DestroyRelationshipLinker.info -> DestroyRelationshipLinker(DestroyRelationship(source, target), this).also(connectionBox::add)
            else -> throw IllegalArgumentException()
        }
    }

    private var isInit = false
    override fun init() {
        if (!isInit) {
            modelConnections.connections.forEach(this::add)
            isInit = true
        }
    }
}
