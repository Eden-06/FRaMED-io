package io.framed.linker

import io.framed.framework.linker.ConnectionLinker
import io.framed.framework.linker.ShapeLinker
import io.framed.model.*
import io.framed.util.TestBase
import io.framed.util.TestMatchers
import io.framed.util.TestMatchers.Companion.inputFieldMatcher
import io.framed.util.TestMatchers.Companion.sidebarMatcher
import io.framed.util.TestMatchers.Companion.sidebarSectionMatcher
import org.w3c.dom.get
import kotlin.test.*

class RoleProhibitionLinkerTest : TestBase() {
    private var roleProhibitionLinker1: ConnectionLinker<*>? = null

    @BeforeTest
    override fun setUpTest() {
        super.setUpTest()

        roleProhibitionLinker1 = connectionManagerLinker!!
            .createConnection(roleType1!!.id,
                roleType2!!.id,
                RoleProhibitionLinker.info)
    }

    @AfterTest
    override fun tearDownTest() {
        super.tearDownTest()

        roleProhibitionLinker1 = null
    }

    @Test
    fun constructorTest() {
        roleProhibitionLinker1?.let { linker ->
            assertEquals( roleType1!!.id, linker.sourceIdProperty.get(),
                "The initial source id does not match")
            assertEquals( roleType2!!.id, linker.targetIdProperty.get(),
                "The initial target id does not match")

            assertNotNull(linker.pictogram)
            assertNotNull(linker.sidebar)
            assertNotNull(linker.contextMenu)

        } ?: run {
            fail("RoleProhibitionLinker object is null. Test setup failed")
        }
    }

    @Test
    fun contextMenuTest() {
        roleProhibitionLinker1?.let { linker ->
            assertEquals(1, linker.contextMenu.html.children.length, "Context menu is missing child elements.")

            linker.contextMenu.html.children[0]?.let { contextMenuListView ->
                TestMatchers.contextMenuMatcher(
                    "Role Prohibition",
                    "Delete"
                )(contextMenuListView)
            }
        } ?: run {
            fail("RoleProhibitionLinker object is null. Test setup failed")
        }
    }

    @Test
    fun sidebarTest() {
        roleProhibitionLinker1?.let { linker ->
            sidebarMatcher("Role Prohibition",
                sidebarSectionMatcher(
                    "Structure",
                    inputFieldMatcher("Type")
                ))(linker.sidebar.html)
        } ?: run {
            fail("RoleProhibitionLinker object is null. Test setup failed")
        }
    }

    @Test
    fun isLinkerForTest() {
        roleProhibitionLinker1?.let { linker ->
            // Check isLinkerFor
            assertTrue(RoleProhibitionLinker.isLinkerFor(linker))

            assertFalse(RoleProhibitionLinker.isLinkerFor(roleTypeLinker1!!))
            assertFalse(RoleProhibitionLinker.isLinkerFor(classTypeLinker!!))
        } ?: run {
            fail("RoleProhibitionLinker object is null. Test setup failed")
        }
    }

    @Test
    fun canStartTest() {
        assertTrue(RoleProhibitionLinker.canStart(roleTypeLinker1!!))
        assertTrue(RoleProhibitionLinker.canStart(roleTypeLinker2!!))
        assertFalse(RoleProhibitionLinker.canStart(classTypeLinker!!))
    }

    @Test
    fun canCreateTest() {
        assertTrue(RoleProhibitionLinker.canCreate(roleTypeLinker1!!, roleTypeLinker2!!))
        assertFalse(RoleProhibitionLinker.canCreate(classTypeLinker!!, roleTypeLinker2!!))
        assertFalse(RoleProhibitionLinker.canCreate(roleTypeLinker1!!, classTypeLinker!!))
    }

    @Test
    fun createLinkerTest() {
        roleProhibitionLinker1?.let { _ ->
            assertNotNull(RoleProhibitionLinker.createLinker(
                RoleProhibitionLinker.createModel(roleType1!!.id, roleType2!!.id),
                connectionManagerLinker!!))

            assertFailsWith(IllegalArgumentException::class,
                "Illegal argument exception was not thrown",
                block = {
                    RoleProhibitionLinker.createLinker(
                        RoleImplicationLinker.createModel(roleType1!!.id, roleType2!!.id),
                        connectionManagerLinker!!)
                }
            )
        } ?: run {
            fail("RoleProhibitionLinker object is null. Test setup failed")
        }
    }

    @Test
    fun swapTest() {
        roleProhibitionLinker1?.let { linker ->
            assertTrue(linker.canSwap(RoleProhibitionLinker.info), "Cannot swap symmetrical connection!")
            assertEquals(roleType1!!.id, linker.sourceIdProperty.value, "Unexpected initial source id!")
            assertEquals(roleType2!!.id, linker.targetIdProperty.value, "Unexpected initial target id!")
            linker.swap();
            assertEquals(roleType2!!.id, linker.sourceIdProperty.value, "Unexpected source id after swap!")
            assertEquals(roleType1!!.id, linker.targetIdProperty.value, "Unexpected target id after swap!")
        } ?: run {
            fail("RoleProhibitionLinker object is null. Test setup failed")
        }
    }

    @Test
    fun deleteTest() {
        roleProhibitionLinker1?.let { linker ->
            assertTrue(connectionManagerLinker!!.connections.contains(linker),
            "Linker not found in the connectionManagerLinker")
            linker.delete()
            assertFalse(connectionManagerLinker!!.connections.contains(linker),
                "Deleted linker found in the connectionManagerLinker")
        } ?: run {
            fail("RoleProhibitionLinker object is null. Test setup failed")
        }
    }
}
