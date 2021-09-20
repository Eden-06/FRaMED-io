package io.framed.linker

import io.framed.framework.linker.ConnectionLinker
import io.framed.framework.linker.ShapeLinker
import io.framed.model.*
import io.framed.util.TestBase
import io.framed.util.TestMatchers
import io.framed.util.TestMatchers.Companion.contextMenuMatcher
import org.w3c.dom.get
import kotlin.test.*

class RoleEquivalenceLinkerTest : TestBase() {
    private var roleEquivalenceLinker1: ConnectionLinker<*>? = null

    @BeforeTest
    override fun setUpTest() {
        super.setUpTest()

        roleEquivalenceLinker1 = connectionManagerLinker!!
            .createConnection(roleType1!!.id,
                roleType2!!.id,
                RoleEquivalenceLinker.info)
    }

    @AfterTest
    override fun tearDownTest() {
        super.tearDownTest()
        roleEquivalenceLinker1 = null
    }

    @Test
    fun constructorTest() {
        roleEquivalenceLinker1?.let { linker ->
            assertEquals( roleType1!!.id, linker.sourceIdProperty.get(),
                "The initial source id does not match")
            assertEquals( roleType2!!.id, linker.targetIdProperty.get(),
                "The initial target id does not match")

            assertNotNull(linker.pictogram)
            assertNotNull(linker.sidebar)
            assertNotNull(linker.contextMenu)

        } ?: run {
            fail("RoleEquivalenceLinker object is null. Test setup failed")
        }
    }

    @Test
    fun contextMenuTest() {
        roleEquivalenceLinker1?.let { linker ->
            assertEquals(1, linker.contextMenu.html.children.length, "Context menu is missing child elements.")

            linker.contextMenu.html.children[0]?.let { contextMenuListView ->
                contextMenuMatcher("Role Equivalence",
                    "Delete") (contextMenuListView)
            }
        } ?: run {
            fail("RoleEquivalenceLinker object is null. Test setup failed")
        }
    }

    @Test
    fun sidebarTest() {
        roleEquivalenceLinker1?.let { linker ->
            TestMatchers.sidebarMatcher(
                "Role Equivalence",
                TestMatchers.sidebarSectionMatcher(
                    "Structure",
                    TestMatchers.inputFieldMatcher("Type")
                )
            )(linker.sidebar.html)
        } ?: run {
            fail("RoleEquivalenceLinker object is null. Test setup failed")
        }
    }

    @Test
    fun isLinkerForTest() {
        roleEquivalenceLinker1?.let { linker ->
            // Check isLinkerFor
            assertTrue(RoleEquivalenceLinker.isLinkerFor(linker))
            assertFalse(RoleEquivalenceLinker.isLinkerFor(roleTypeLinker1!!))
            assertFalse(RoleEquivalenceLinker.isLinkerFor(classTypeLinker!!))
        } ?: run {
            fail("RoleEquivalenceLinker object is null. Test setup failed")
        }
    }

    @Test
    fun canStartTest() {
        assertTrue(RoleEquivalenceLinker.canStart(roleTypeLinker1!!))
        assertTrue(RoleEquivalenceLinker.canStart(roleTypeLinker2!!))
        assertFalse(RoleEquivalenceLinker.canStart(classTypeLinker!!))
    }

    @Test
    fun canCreateTest() {
        assertTrue(RoleEquivalenceLinker.canCreate(roleTypeLinker1!!, roleTypeLinker2!!))
        assertFalse(RoleEquivalenceLinker.canCreate(classTypeLinker!!, roleTypeLinker2!!))
        assertFalse(RoleEquivalenceLinker.canCreate(roleTypeLinker1!!, classTypeLinker!!))
    }

    @Test
    fun createLinkerTest() {
        roleEquivalenceLinker1?.let { _ ->
            assertNotNull(RoleEquivalenceLinker.createLinker(
                RoleEquivalenceLinker.createModel(roleType1!!.id, roleType2!!.id),
                connectionManagerLinker!!))

            assertFailsWith(IllegalArgumentException::class,
                "Illegal argument exception was not thrown",
                block = {
                    RoleEquivalenceLinker.createLinker(
                        RoleImplicationLinker.createModel(roleType1!!.id, roleType2!!.id),
                        connectionManagerLinker!!)
                }
            )
        } ?: run {
            fail("RoleEquivalenceLinker object is null. Test setup failed")
        }
    }

    @Test
    fun swapTest() {
        roleEquivalenceLinker1?.let { linker ->
            assertTrue(linker.canSwap(RoleEquivalenceLinker.info), "Cannot swap symmetrical connection!")
            assertEquals(roleType1!!.id, linker.sourceIdProperty.value, "Unexpected initial source id!")
            assertEquals(roleType2!!.id, linker.targetIdProperty.value, "Unexpected initial target id!")
            linker.swap();
            assertEquals(roleType2!!.id, linker.sourceIdProperty.value, "Unexpected source id after swap!")
            assertEquals(roleType1!!.id, linker.targetIdProperty.value, "Unexpected target id after swap!")
        } ?: run {
            fail("RoleEquivalenceLinker object is null. Test setup failed")
        }
    }

    @Test
    fun deleteTest() {
        roleEquivalenceLinker1?.let { linker ->
            assertTrue(connectionManagerLinker!!.connections.contains(linker),
            "Linker not found in the connectionManagerLinker")
            linker.delete()
            assertFalse(connectionManagerLinker!!.connections.contains(linker),
                "Deleted linker found in the connectionManagerLinker")
        } ?: run {
            fail("RoleEquivalenceLinker object is null. Test setup failed")
        }
    }
}
