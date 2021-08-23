package io.framed.linker

import io.framed.model.*
import io.framed.util.TestMatchers
import io.framed.util.TestMatchers.Companion.contextMenuMatcher
import org.w3c.dom.get
import kotlin.test.*

class RoleGroupLinkerTest {
    private var connectionManagerLinker: ConnectionManagerLinker? = null

    //ModelLinkers
    private var packageElement: Package? = null
    private var packageLinker: PackageLinker? = null

    private var compartmentElement: Compartment? = null
    private var compartmentLinker: CompartmentLinker? = null

    private var roleGroup1: RoleGroup? = null
    private var roleGroupLinker1: RoleGroupLinker? = null

    // RoleTypes
    private var roleType1: RoleType? = null
    private var roleTypeLinker1: RoleTypeLinker? = null

    private var roleType2: RoleType? = null
    private var roleTypeLinker2: RoleTypeLinker? = null

    private var roleType3: RoleType? = null
    private var roleTypeLinker3: RoleTypeLinker? = null

    // ClassType
    private var classType: Class? = null
    private var classTypeLinker: ClassLinker? = null

    @BeforeTest
    fun setUpTest() {
        connectionManagerLinker = ConnectionManagerLinker(Connections())

        packageElement = Package()
        packageLinker = PackageLinker(
            packageElement!!,
            connectionManagerLinker!!)

        compartmentElement = Compartment()
        compartmentLinker = packageLinker!!.add(compartmentElement!!) as CompartmentLinker

        roleType1 = RoleType()
        roleType1!!.name = "Role1"
        roleTypeLinker1 = compartmentLinker!!.add(roleType1!!) as RoleTypeLinker

        roleType2 = RoleType()
        roleType2!!.name = "Role2"

        roleGroup1 = RoleGroup()
        roleGroup1!!.name = "RoleGroup1"
        roleGroupLinker1 = compartmentLinker!!.add(roleGroup1!!) as RoleGroupLinker

        roleType3 = RoleType()
        roleType3!!.name = "Role3"

        classType = Class()
        classTypeLinker = packageLinker!!.add(classType!!) as ClassLinker
    }

    @AfterTest
    fun tearDownTest() {
        roleGroup1 = null
        roleGroupLinker1 = null

        connectionManagerLinker = null

        compartmentElement = null
        compartmentLinker = null

        packageElement = null
        packageLinker = null

        roleType1 = null
        roleTypeLinker1 = null

        roleType2 = null
        roleTypeLinker2 = null

        roleType3 = null
        roleTypeLinker3 = null

        classType = null
        classTypeLinker = null
    }

    @Test
    fun constructorTest() {
        roleGroupLinker1?.let { linker ->
            assertNotNull(linker.autoLayoutBox)
            assertNotNull(linker.borderBox)
            assertNotNull(linker.borderShapes)
            assertNotNull(linker.container)
            assertNotNull(linker.preview)
            assertNotNull(linker.shapeLinkers)
            assertNotNull(linker.pictogram)
            assertNotNull(linker.sidebar)
            assertNotNull(linker.contextMenu)
            assertNotNull(linker.parent)
            assertEquals(compartmentLinker!!, linker.parent, "RoleGroup parent does not match!")
            assertEquals( 0, linker.shapeLinkers.size,
                "RoleGroup children count after construction is too high!")
            assertEquals("RoleGroup1", linker.name)

        } ?: run {
            fail("RoleGroupLinker object is null. Test setup failed")
        }
    }

    @Test
    fun canCreateInTest() {
        roleGroupLinker1?.let {
            assertFalse(RoleGroupLinker.canCreateIn(packageElement!!),
                "A RoleGroup cannot be created in Packages")
            assertTrue(RoleGroupLinker.canCreateIn(compartmentElement!!),
                "A RoleGroup can be created in Compartments")
            assertTrue(RoleGroupLinker.canCreateIn(Scene()),
                "A RoleGroup can be created in Scenes")
            assertTrue(RoleGroupLinker.canCreateIn(roleGroup1!!),
                "A RoleGroup can be created in RoleGroups")
        } ?: run {
            fail("RoleGroupLinker object is null. Test setup failed")
        }
    }

    @Test
    fun addTest() {
        roleGroupLinker1?.let { linker ->
            roleTypeLinker2 = linker.add(roleType2!!) as RoleTypeLinker?
            assertNotNull(roleTypeLinker2,
                "RoleType $roleType2 could not be added to the RoleGroup $roleGroup1")
            assertEquals(1, linker.shapeLinkers.size,
                "$roleGroup1 children count is not the expected value!")
            //FIXME; Shape linkers do not implement equals.
            //assertContains(linker.shapeLinkers, roleTypeLinker2!!)
            assertTrue(linker.shapeLinkers.any { it.id ==  roleTypeLinker2!!.id })

            roleTypeLinker3 = linker.add(roleType3!!) as RoleTypeLinker?
            assertNotNull(roleTypeLinker3,
                "RoleType $roleType3 could not be added to the RoleGroup $roleGroup1")
            assertEquals(2, linker.shapeLinkers.size,
                "$roleGroup1 children count is not the expected value!")
            //FIXME; Shape linkers do not implement equals.
            //assertContains(linker.shapeLinkers, roleTypeLinker3!!)
            assertTrue(linker.shapeLinkers.any { it.id ==  roleTypeLinker3!!.id })

        } ?: run {
            fail("RoleGroupLinker object is null. Test setup failed")
        }
    }

    @Test
    fun removeTest() {
        roleGroupLinker1?.let { linker ->
            roleTypeLinker2 = linker.add(roleType2!!) as RoleTypeLinker?
            roleTypeLinker3 = linker.add(roleType3!!) as RoleTypeLinker?

            assertEquals(2, linker.shapeLinkers.size,
                "$roleGroup1 children count is not the expected value!")
            //FIXME; Shape linkers do not implement equals.
            //assertContains(linker.shapeLinkers, roleTypeLinker2!!)
            //assertContains(linker.shapeLinkers, roleTypeLinker3!!)
            assertTrue(linker.shapeLinkers.any { it.id ==  roleTypeLinker2!!.id })
            assertTrue(linker.shapeLinkers.any { it.id ==  roleTypeLinker3!!.id })

            linker.remove(roleTypeLinker2!!)

            assertEquals(1, linker.shapeLinkers.size,
                "$roleGroup1 children count is not the expected value!")
            //FIXME; Shape linkers do not implement equals.
            //assertContains(linker.shapeLinkers, roleTypeLinker2!!)
            assertFalse(linker.shapeLinkers.any { it.id ==  roleTypeLinker2!!.id })

            linker.remove(roleTypeLinker3!!)

            assertEquals(0, linker.shapeLinkers.size,
                "$roleGroup1 children count is not the expected value!")
            //FIXME; Shape linkers do not implement equals.
            //assertContains(linker.shapeLinkers, roleTypeLinker3!!)
            assertFalse(linker.shapeLinkers.any { it.id ==  roleTypeLinker3!!.id })

        } ?: run {
            fail("RoleGroupLinker object is null. Test setup failed")
        }
    }

    @Test
    fun contextMenuTest() {
        roleGroupLinker1?.let { linker ->
            assertEquals(1, linker.contextMenu.html.children.length,
                "Context menu is missing child elements.")

            linker.contextMenu.html.children[0]?.let { contextMenuListView ->
                contextMenuMatcher("RoleGroup: RoleGroup1",
                    "Create Role type", "Create RoleGroup", "Delete")(contextMenuListView)
            }
        } ?: run {
            fail("RoleGroupLinker object is null. Test setup failed")
        }
    }

    @Test
    fun sidebarTest() {
        roleGroupLinker1?.let { linker ->
            TestMatchers.sidebarMatcher(
                "Role Group",
                TestMatchers.sidebarSectionMatcher(
                    "General",
                    TestMatchers.inputFieldMatcher("Name"),
                    TestMatchers.inputFieldMatcher("Cardinality")
                ),
                TestMatchers.sidebarSectionMatcher(
                    "Actions",
                    TestMatchers.listLabelMatcher("Auto layout"),
                    TestMatchers.listLabelMatcher("Reset zoom"),
                    TestMatchers.listLabelMatcher("Reset pan")
                ),
                TestMatchers.sidebarSectionMatcher(
                    "Preview",
                    TestMatchers.checkBoxMatcher("Flat preview"),
                    TestMatchers.listLabelMatcher("Auto layout")
                ),
                TestMatchers.sidebarSectionMatcher(
                    "Layout",
                    TestMatchers.listLabelMatcher("Auto size"),
                    TestMatchers.checkBoxMatcher("Complete view"),
                ),
                TestMatchers.sidebarSectionMatcher(
                    "Advanced",
                    TestMatchers.inputFieldMatcher("Identifier"),
                    TestMatchers.inputFieldMatcher("Position"),
                    TestMatchers.inputFieldMatcher("Size"),
                    TestMatchers.checkBoxMatcher("Autosize"),
                    TestMatchers.listLabelMatcher("Log pictogram"),
                )
            )(linker.sidebar.html)
        } ?: run {
            fail("RoleGroupLinker object is null. Test setup failed")
        }
    }
}
