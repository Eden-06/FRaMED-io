package io.framed.linker

import io.framed.model.*
import io.framed.util.TestBase
import io.framed.util.TestMatchers
import io.framed.util.TestMatchers.Companion.contextMenuMatcher
import org.w3c.dom.get
import kotlin.test.*

class RoleGroupLinkerTest : TestBase() {

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
            assertEquals(compartmentLinker!!, linker.parent,
                "RoleGroup parent does not match!")
            assertEquals( 1, linker.shapeLinkers.size,
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
            assertNotNull(roleTypeLinker2,
                "RoleType $roleType2 could not be added to the RoleGroup $roleGroup1")
            assertEquals(1, linker.shapeLinkers.size,
                "$roleGroup1 children count is not the expected value!")
            //FIXME; Shape linkers do not implement equals.
            //assertContains(linker.shapeLinkers, roleTypeLinker2!!)
            assertTrue(linker.shapeLinkers.any { it.id ==  roleTypeLinker2!!.id })
            sceneLinker!!.remove(roleTypeLinker3!!)

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
            sceneLinker!!.remove(roleTypeLinker3!!)
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
                    TestMatchers.listWrappedTextContentMatcher("Auto layout"),
                    TestMatchers.listWrappedTextContentMatcher("Reset zoom"),
                    TestMatchers.listWrappedTextContentMatcher("Reset pan")
                ),
                TestMatchers.sidebarSectionMatcher(
                    "Preview",
                    TestMatchers.checkBoxMatcher("Flat preview"),
                    TestMatchers.listWrappedTextContentMatcher("Auto layout")
                ),
                TestMatchers.sidebarSectionMatcher(
                    "Layout",
                    TestMatchers.listWrappedTextContentMatcher("Auto size"),
                    TestMatchers.checkBoxMatcher("Complete view"),
                ),
                TestMatchers.sidebarSectionMatcher(
                    "Advanced",
                    TestMatchers.inputFieldMatcher("Identifier"),
                    TestMatchers.inputFieldMatcher("Position"),
                    TestMatchers.inputFieldMatcher("Size"),
                    TestMatchers.checkBoxMatcher("Autosize"),
                    TestMatchers.listWrappedTextContentMatcher("Log pictogram"),
                )
            )(linker.sidebar.html)
        } ?: run {
            fail("RoleGroupLinker object is null. Test setup failed")
        }
    }
}
