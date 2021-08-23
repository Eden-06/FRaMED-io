package io.framed.linker

import io.framed.framework.linker.ConnectionLinker
import io.framed.framework.linker.ShapeLinker
import io.framed.model.*
import io.framed.util.TestMatchers
import io.framed.util.TestMatchers.Companion.contextMenuMatcher
import org.w3c.dom.get
import kotlin.test.*

class RoleEquivalenceLinkerTest {
    private var roleEquivalenceLinker1: ConnectionLinker<*>? = null
    private var connectionManagerLinker: ConnectionManagerLinker? = null

    private var packageElement: Package? = null
    private var packageLinker: PackageLinker? = null

    private var sceneElement: Scene? = null
    private var sceneLinker: SceneLinker? = null

    private var roleType1: RoleType? = null
    private var roleTypeLinker1: ShapeLinker<*,*>? = null

    private var roleType2: RoleType? = null
    private var roleTypeLinker2: ShapeLinker<*,*>? = null

    private var classType: Class? = null
    private var classTypeLinker: ClassLinker? = null

    @BeforeTest
    fun setUpTest() {
        connectionManagerLinker = ConnectionManagerLinker(Connections())

        packageElement = Package()
        packageLinker = PackageLinker(packageElement!!, connectionManagerLinker!!)

        sceneElement = Scene()
        sceneLinker = SceneLinker(sceneElement!!, connectionManagerLinker!!, packageLinker!!)

        roleType1 = RoleType()
        roleType1!!.name = "Role1"
        roleTypeLinker1 = sceneLinker!!.add(roleType1!!)

        roleType2 = RoleType()
        roleType2!!.name = "Role2"
        roleTypeLinker2 = sceneLinker!!.add(roleType2!!)

        roleEquivalenceLinker1 = connectionManagerLinker!!
            .createConnection(roleType1!!.id,
                roleType2!!.id,
                RoleEquivalenceLinker.info)

        classType = Class()
        classTypeLinker = ClassLinker(classType!!, sceneLinker!!)
    }

    @AfterTest
    fun tearDownTest() {
        roleEquivalenceLinker1 = null
        connectionManagerLinker = null

        packageElement = null
        packageLinker = null

        sceneElement = null
        sceneLinker = null

        roleType1 = null
        roleTypeLinker1 = null

        roleType2 = null
        roleTypeLinker2 = null

        classType = null
        classTypeLinker = null
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
