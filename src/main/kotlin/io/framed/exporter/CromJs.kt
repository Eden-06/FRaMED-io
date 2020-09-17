package io.framed.exporter

import io.framed.exporter.crom.Crom_l1_composedFactory
import io.framed.exporter.crom.Crom_l1_composedFactoryImpl
import io.framed.exporter.ecore.*
import org.w3c.dom.Element

@JsModule("crom")
@JsNonModule
@JsName("crom")
external class CromJs {
    companion object {
        fun Crom_l1_composedFactoryImpl(): Crom_l1_composedFactoryImpl
        var EcorePackageImpl: EcorePackageImpl.Companion
        var EcoreFactoryImpl: EcoreFactoryImpl.Companion

        open class XmiResource(epackage: EPackage, efactory: EFactory) {
            open var save: (eobjects: Array<EObject>) -> String
            open var load: (xml: String) -> Array<EObject>
            open var rootnode: (node: Element) -> Unit
            open var addEStructuralFeatures: (eobject: EObject, node: Element) -> Unit
            open var lateResolve: () -> Unit
        }
    }
}
