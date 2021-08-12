package io.framed.linker

import io.framed.framework.linker.ModelLinker
import io.framed.framework.linker.ShapeLinker
import io.framed.model.*
import org.w3c.dom.get
import kotlin.test.*

class RoleGroupLinkerTest {
    private var roleGroup1: RoleGroup? = null
    private var roleGroupLinker1: RoleGroupLinker? = null

    private var connectionManagerLinker: ConnectionManagerLinker? = null

    private var compartmentElement: Compartment? = null
    private var compartmentLinker: CompartmentLinker? = null

    private var packageElement: Package? = null
    private var packageLinker: PackageLinker? = null

    private var roleType1: RoleType? = null
    private var roleTypeLinker1: ShapeLinker<*, *>? = null

    private var roleType2: RoleType? = null
    private var roleTypeLinker2: ShapeLinker<*, *>? = null

    private var roleType3: RoleType? = null
    private var roleTypeLinker3: ShapeLinker<*, *>? = null

    private var classType: Class? = null
    private var classTypeLinker: ClassLinker? = null

    @BeforeTest
    fun setUpTest() {
        connectionManagerLinker = ConnectionManagerLinker(Connections())

        packageElement = Package()
        packageLinker = PackageLinker(packageElement!!, connectionManagerLinker!!)

        compartmentElement = Compartment()
        compartmentLinker = CompartmentLinker(compartmentElement!!, connectionManagerLinker!!, packageLinker!!)

        roleType1 = RoleType()
        roleType1!!.name = "Role1"
        roleTypeLinker1 = compartmentLinker!!.add(roleType1!!)

        roleType2 = RoleType()
        roleType2!!.name = "Role2"

        roleType3 = RoleType()
        roleType3!!.name = "Role3"

        roleGroup1 = RoleGroup()
        roleGroup1!!.name = "RoleGroup1"
        roleGroupLinker1 = RoleGroupLinker(roleGroup1!!, connectionManagerLinker!!, compartmentLinker!!)

        classType = Class()
        classTypeLinker = ClassLinker(classType!!, compartmentLinker!!)
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
            roleTypeLinker2 = linker.add(roleType2!!)
            assertNotNull(roleTypeLinker2,
                "RoleType $roleType2 could not be added to the RoleGroup $roleGroup1")
            assertEquals(1, linker.shapeLinkers.size,
                "$roleGroup1 children count is not the expected value!")
            //FIXME; Shape linkers do not implement equals.
            //assertContains(linker.shapeLinkers, roleTypeLinker2!!)
            assertTrue(linker.shapeLinkers.any { it.id ==  roleTypeLinker2!!.id })

            roleTypeLinker3 = linker.add(roleType3!!)
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
            roleTypeLinker2 = linker.add(roleType2!!)
            roleTypeLinker3 = linker.add(roleType3!!)

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
                assertEquals(4, contextMenuListView.children.length,
                    "Context menu list view is missing child elements")

                // Title entry
                contextMenuListView.children[0]?.let { titleView ->
                    assertEquals("RoleGroup: RoleGroup1", titleView.textContent!!)
                }

                // Add RoleType Buttons
                contextMenuListView.children[1]?.let { addRoleTypeButtonsListView ->
                    assertEquals(
                        2,
                        addRoleTypeButtonsListView.children.length,
                        "Context menu 'Add Role Type' button view is missing child elements."
                    )
                    // Add RoleType Button Icon
                    addRoleTypeButtonsListView.children[0]?.let { addRoleTypeButtonIconView ->
                        assertNotNull(addRoleTypeButtonIconView)
                    }

                    // Add RoleType Button Text
                    addRoleTypeButtonsListView.children[1]?.let { addRoleTypeButtonTextView ->
                        assertEquals("Create Role type", addRoleTypeButtonTextView.textContent!!)
                    }
                }

                // Add RoleGroup Buttons
                contextMenuListView.children[2]?.let { addRoleGroupButtonsListView ->
                    assertEquals(
                        2,
                        addRoleGroupButtonsListView.children.length,
                        "Context menu 'Add Role Group' button view is missing child elements."
                    )
                    // Add RoleGroup Button Icon
                    addRoleGroupButtonsListView.children[0]?.let { addRoleGroupButtonIconView ->
                        assertNotNull(addRoleGroupButtonIconView)
                    }

                    // Add RoleGroup Button Text
                    addRoleGroupButtonsListView.children[1]?.let { addRoleGroupButtonTextView ->
                        assertEquals("Create RoleGroup", addRoleGroupButtonTextView.textContent!!)
                    }
                }

                contextMenuListView.children[3]?.let { buttonsListView ->
                    assertEquals(
                        2,
                        buttonsListView.children.length,
                        "Context menu delete button view is missing child elements."
                    )
                    // Delete Button Icon
                    buttonsListView.children[0]?.let { deleteButtonIconView ->
                        assertNotNull(deleteButtonIconView)
                    }

                    // Delete Button Text
                    buttonsListView.children[1]?.let { deleteButtonTextView ->
                        assertEquals("Delete", deleteButtonTextView.textContent!!)
                    }
                }
            }
        } ?: run {
            fail("RoleGroupLinker object is null. Test setup failed")
        }
    }

    @Test
    fun sidebarTest() {
        roleGroupLinker1?.let { linker ->
            // Test HTML Children
            assertEquals(6, linker.sidebar.html.children.length,
                "Unexpected amount of sidebar children!")

            // Test Title entry
            linker.sidebar.html.children[0]?.let { titleListView ->
                assertEquals(1, titleListView.children.length)
                titleListView.children[0]?.let { title ->
                    assertEquals("Role Group", title.textContent!!)
                }
            }

            // First Group Element
            linker.sidebar.html.children[1]?.let { firstGroupView ->
                assertEquals(3, firstGroupView.children.length,
                    "Unexpected amount of children in the first sidebar group!")

                // General Title Header
                firstGroupView.children[0]?.let { titleListView ->
                    assertEquals(2, titleListView.children.length)
                    titleListView.children[0]?.let { titleView ->
                        assertEquals("General", titleView.textContent)
                    }
                }
                // Name Selection Element
                firstGroupView.children[1]?.let { nameListView ->
                    assertEquals(2, nameListView.children.length)
                    nameListView.children[0]?.let { titleView ->
                        assertEquals("Name", titleView.textContent)
                    }
                }
                // Cardinality Selection Element
                firstGroupView.children[2]?.let { cardinalityListView ->
                    assertEquals(2, cardinalityListView.children.length)
                    cardinalityListView.children[0]?.let { titleView ->
                        assertEquals("Cardinality", titleView.textContent)
                    }
                }
            }

            // Second Group Element
            linker.sidebar.html.children[2]?.let { secondGroupView ->
                assertEquals(4, secondGroupView.children.length,
                    "Unexpected amount of children in the second sidebar group!")
            }

            // Third Group Element
            linker.sidebar.html.children[3]?.let { thirdGroupView ->
                assertEquals(3, thirdGroupView.children.length,
                    "Unexpected amount of children in the third sidebar group!")
            }

        } ?: run {
            fail("RoleGroupLinker object is null. Test setup failed")
        }
    }
}
