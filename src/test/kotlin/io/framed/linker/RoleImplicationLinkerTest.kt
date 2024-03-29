package io.framed.linker

import io.framed.framework.linker.ConnectionLinker
import io.framed.framework.linker.ShapeLinker
import io.framed.model.*
import io.framed.util.TestBase
import io.framed.util.TestMatchers
import org.w3c.dom.get
import kotlin.test.*

class RoleImplicationLinkerTest : TestBase() {
    private var roleImplicationLinker1: ConnectionLinker<*>? = null

    @BeforeTest
    override fun setUpTest() {
        super.setUpTest()

        roleImplicationLinker1 = connectionManagerLinker!!
            .createConnection(roleType1!!.id,
                roleType2!!.id,
                RoleImplicationLinker.info)
    }

    @AfterTest
    override fun tearDownTest() {
        super.tearDownTest()

        roleImplicationLinker1 = null
    }

    @Test
    fun constructorTest() {
        roleImplicationLinker1?.let { linker ->
            assertEquals( roleType1!!.id, linker.sourceIdProperty.get(),
                "The initial source id does not match")
            assertEquals( roleType2!!.id, linker.targetIdProperty.get(),
                "The initial target id does not match")

            assertNotNull(linker.pictogram)
            assertNotNull(linker.sidebar)
            assertNotNull(linker.contextMenu)

        } ?: run {
            fail("RoleImplicationLinker object is null. Test setup failed")
        }
    }

    @Test
    fun contextMenuTest() {
        roleImplicationLinker1?.let { linker ->
            assertEquals(1, linker.contextMenu.html.children.length, "Context menu is missing child elements.")

            linker.contextMenu.html.children[0]?.let { contextMenuListView ->
                TestMatchers.contextMenuMatcher(
                    "Role Implication",
                    "Delete"
                )(contextMenuListView)
            }
        } ?: run {
            fail("RoleImplicationLinker object is null. Test setup failed")
        }
    }

    @Test
    fun sidebarTest() {
        roleImplicationLinker1?.let { linker ->
            // Test HTML Children
            assertEquals(2, linker.sidebar.html.children.length,
                "Unexpected amount of sidebar children!")

            // Test Title entry
            linker.sidebar.html.children[0]?.let { titleListView ->
                assertEquals(1, titleListView.children.length)
                titleListView.children[0]?.let { title ->
                    assertEquals("Role Implication", title.textContent!!)
                }
            }

            // First Group Element
            linker.sidebar.html.children[1]?.let { firstGroupView ->
                assertEquals(3, firstGroupView.children.length,
                    "Unexpected amount of children in the second sidebar group!")

                // General Title Header
                firstGroupView.children[0]?.let { titleListView ->
                    assertEquals(2, titleListView.children.length)
                    titleListView.children[0]?.let { titleView ->
                        assertEquals("Structure", titleView.textContent)
                    }
                }
                // Name Selection Element
                firstGroupView.children[1]?.let { nameListView ->
                    assertEquals(2, nameListView.children.length)
                    nameListView.children[0]?.let { titleView ->
                        assertEquals("Type", titleView.textContent)
                    }
                }
            }
        } ?: run {
            fail("RoleImplicationLinker object is null. Test setup failed")
        }
    }

    @Test
    fun isLinkerForTest() {
        roleImplicationLinker1?.let { linker ->
            // Check isLinkerFor
            assertTrue(RoleImplicationLinker.isLinkerFor(linker))

            assertFalse(RoleImplicationLinker.isLinkerFor(roleTypeLinker1!!))
            assertFalse(RoleImplicationLinker.isLinkerFor(classTypeLinker!!))
        } ?: run {
            fail("RoleImplicationLinker object is null. Test setup failed")
        }
    }

    @Test
    fun canStartTest() {
        assertTrue(RoleImplicationLinker.canStart(roleTypeLinker1!!))
        assertTrue(RoleImplicationLinker.canStart(roleTypeLinker2!!))
        assertFalse(RoleImplicationLinker.canStart(classTypeLinker!!))
    }

    @Test
    fun canCreateTest() {
        assertTrue(RoleImplicationLinker.canCreate(roleTypeLinker1!!, roleTypeLinker2!!))
        assertFalse(RoleImplicationLinker.canCreate(classTypeLinker!!, roleTypeLinker2!!))
        assertFalse(RoleImplicationLinker.canCreate(roleTypeLinker1!!, classTypeLinker!!))
    }

    @Test
    fun createLinkerTest() {
        roleImplicationLinker1?.let { _ ->
            assertNotNull(RoleImplicationLinker.createLinker(
                RoleImplicationLinker.createModel(roleType1!!.id, roleType2!!.id),
                connectionManagerLinker!!))

            assertFailsWith(IllegalArgumentException::class,
                "Illegal argument exception was not thrown",
                block = {
                    RoleImplicationLinker.createLinker(
                        RoleProhibitionLinker.createModel(roleType1!!.id, roleType2!!.id),
                        connectionManagerLinker!!)
                }
            )
        } ?: run {
            fail("RoleImplicationLinker object is null. Test setup failed")
        }
    }

    @Test
    fun swapTest() {
        roleImplicationLinker1?.let { linker ->
            assertTrue(linker.canSwap(RoleImplicationLinker.info), "Cannot swap symmetrical connection!")
            assertEquals(roleType1!!.id, linker.sourceIdProperty.value, "Unexpected initial source id!")
            assertEquals(roleType2!!.id, linker.targetIdProperty.value, "Unexpected initial target id!")
            linker.swap();
            assertEquals(roleType2!!.id, linker.sourceIdProperty.value, "Unexpected source id after swap!")
            assertEquals(roleType1!!.id, linker.targetIdProperty.value, "Unexpected target id after swap!")
        } ?: run {
            fail("RoleImplicationLinker object is null. Test setup failed")
        }
    }

    @Test
    fun deleteTest() {
        roleImplicationLinker1?.let { linker ->
            assertTrue(connectionManagerLinker!!.connections.contains(linker),
            "Linker not found in the connectionManagerLinker")
            linker.delete()
            assertFalse(connectionManagerLinker!!.connections.contains(linker),
                "Deleted linker found in the connectionManagerLinker")
        } ?: run {
            fail("RoleImplicationLinker object is null. Test setup failed")
        }
    }
}
