package io.framed.linker

import io.framed.framework.ConnectionManager
import io.framed.framework.model.ModelConnection
import io.framed.framework.model.ModelElement
import io.framed.model.*
import kotlin.test.*

class RoleProhibitionLinkerTest {
    private var roleProhibitionLinker1: RoleProhibitionLinker? = null
    private var connectionManagerLinker: ConnectionManagerLinker? = null

    @BeforeTest
    fun setUpTest() {
        ModelElement.lastId = 0

        val roleProhibition = RoleProhibition(1 ,2)
        roleProhibition.name =  "RoleProhibition1"

        connectionManagerLinker = ConnectionManagerLinker(Connections())
        roleProhibitionLinker1 = RoleProhibitionLinker(roleProhibition, connectionManagerLinker!!)
    }

    @AfterTest
    fun tearDownTest() {
        roleProhibitionLinker1 = null
    }

    @Test
    fun constructorTest() {
        roleProhibitionLinker1?.let { linker: RoleProhibitionLinker ->
            assertEquals( 1, linker.sourceIdProperty.get(),
                "The initial source id does not match")
            assertEquals( 2, linker.targetIdProperty.get(),
                "The initial target id does not match")

            assertEquals("RoleProhibition1", linker.name)
            assertNotNull(linker.pictogram)
            assertNotNull(linker.sidebar)
            assertNotNull(linker.contextMenu)

        } ?: run {
            fail("RoleEquivalenceLinker object is null. Test setup failed")
        }
    }

    @Test
    fun canConnectTest() {
        val packageModel = Package()
        val packageLinker = PackageLinker(packageModel, connectionManagerLinker!!)

        val role1 = RoleType()
        val roleLinker1 = RoleTypeLinker(role1, packageLinker)

        val role2 = RoleType()
        val roleLinker2 = RoleTypeLinker(role2, packageLinker)

        roleProhibitionLinker1?.let { linker: RoleProhibitionLinker ->
            assertTrue(RoleProhibitionLinker.isLinkerFor(linker))

            assertTrue(RoleProhibitionLinker.canStart(roleLinker1))
            assertTrue(RoleProhibitionLinker.canStart(roleLinker2))
            assertTrue(RoleProhibitionLinker.canCreate(roleLinker1, roleLinker2))

            assertNotNull(RoleProhibitionLinker.createLinker(
                RoleProhibitionLinker.createModel(role1.id, role2.id),
                connectionManagerLinker!!))

        } ?: run {
            fail("RoleEquivalenceLinker object is null. Test setup failed")
        }
    }
}
