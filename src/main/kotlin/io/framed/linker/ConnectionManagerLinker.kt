package io.framed.linker

import io.framed.framework.*
import io.framed.framework.pictogram.ConnectionInfo
import io.framed.framework.pictogram.Shape
import io.framed.framework.util.EventHandler
import io.framed.framework.util.LinkerConnectionBox
import io.framed.framework.view.CyclicChooser
import io.framed.framework.view.iconView
import io.framed.framework.view.textView
import io.framed.model.*

class ConnectionManagerLinker(val modelConnections: Connections) : ConnectionManager {

    override var modelLinkers: Set<ModelLinker<*, *, *>> = emptySet()

    override val connections: Set<ConnectionLinker<*>>
        get() = associations.linkers + inheritances.linkers + aggregations.linkers + compositions.linkers

    private val associations = LinkerConnectionBox<Association, AssociationLinker>(modelConnections::associations, this)
    private val inheritances = LinkerConnectionBox<Inheritance, InheritanceLinker>(modelConnections::inheritances, this)
    private val aggregations = LinkerConnectionBox<Aggregation, AggregationLinker>(modelConnections::aggregations, this)
    private val compositions = LinkerConnectionBox<Composition, CompositionLinker>(modelConnections::compositions, this)

    override fun addModel(modelLinker: ModelLinker<*, *, *>) {
        modelLinkers += modelLinker
    }

    override fun add(model: ModelConnection<*>) {
        when (model) {
            is Association -> associations += AssociationLinker(model, this)
            is Aggregation -> aggregations += AggregationLinker(model, this)
            is Composition -> compositions += CompositionLinker(model, this)
            is Inheritance -> inheritances += InheritanceLinker(model, this)
        }
    }

    override fun remove(linker: ConnectionLinker<*>) {
        when (linker) {
            is AssociationLinker -> associations -= linker
            is AggregationLinker -> aggregations -= linker
            is CompositionLinker -> compositions -= linker
            is InheritanceLinker -> inheritances -= linker
        }
    }

    override val onConnectionAdd = EventHandler<ConnectionLinker<*>>()
    override val onConnectionRemove = EventHandler<ConnectionLinker<*>>()

    override fun createConnection(source: Shape, target: Shape) {
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

    override fun createConnection(source: Shape, target: Shape, type: ConnectionInfo): ConnectionLinker<*> {
        val sourceId = source.id
        val targetId = target.id

        return when (type) {
            AssociationLinker.info -> AssociationLinker(Association(sourceId, targetId), this).also(associations::add)
            AggregationLinker.info -> AggregationLinker(Aggregation(sourceId, targetId), this).also(aggregations::add)
            InheritanceLinker.info -> InheritanceLinker(Inheritance(sourceId, targetId), this).also(inheritances::add)
            CompositionLinker.info -> CompositionLinker(Composition(sourceId, targetId), this).also(compositions::add)
            else -> throw IllegalArgumentException()
        }
    }

    private var isInit = false
    override fun init() {
        if (!isInit) {
            modelConnections.associations.forEach(this::add)
            modelConnections.aggregations.forEach(this::add)
            modelConnections.inheritances.forEach(this::add)
            modelConnections.compositions.forEach(this::add)

            isInit = true
        }
    }
}