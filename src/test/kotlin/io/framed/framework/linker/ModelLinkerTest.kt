package io.framed.framework.linker

import io.framed.linker.*
import io.framed.model.*
import kotlin.test.*

class ModelLinkerTest {

    private var connectionManagerLinker: ConnectionManagerLinker? = null

    //ModelLinkers
    private var packageElement: Package? = null
    private var packageLinker: PackageLinker? = null

    private var compartmentElement: Compartment? = null
    private var compartmentLinker: CompartmentLinker? = null

    private var roleGroup1: RoleGroup? = null
    private var roleGroupLinker1: ShapeLinker<*, *>? = null

    // RoleTypes
    private var roleType1: RoleType? = null
    private var roleTypeLinker1: ShapeLinker<*, *>? = null

    private var roleType2: RoleType? = null
    private var roleTypeLinker2: ShapeLinker<*, *>? = null

    private var roleType3: RoleType? = null
    private var roleTypeLinker3: ShapeLinker<*, *>? = null

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
        compartmentLinker = CompartmentLinker(
            compartmentElement!!,
            connectionManagerLinker!!,
            packageLinker!!)

        roleType1 = RoleType()
        roleType1!!.name = "Role1"
        roleTypeLinker1 = compartmentLinker!!.add(roleType1!!)

        roleType2 = RoleType()
        roleType2!!.name = "Role2"
        roleTypeLinker2 = compartmentLinker!!.add(roleType2!!)

        roleType3 = RoleType()
        roleType3!!.name = "Role3"

        roleGroup1 = RoleGroup()
        roleGroup1!!.name = "RoleGroup1"

        roleGroupLinker1 = compartmentLinker!!.add(roleGroup1!!)

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
    fun canDropShape() {
        roleGroupLinker1?.let { linker ->
            assertNotNull(roleTypeLinker2)
            assertTrue(compartmentLinker!!.canDropShape(roleTypeLinker2!!.id, linker.id),
                "RoleTypes can be dropped in RoleGroups!")
            assertFalse(packageLinker!!.canDropShape(roleTypeLinker2!!.id, linker.id),
                "Drag and drop between non-related elements is not possible.")

        } ?: run {
            fail("RoleGroupLinker object is null. Test setup failed")
        }
    }

    @Test
    fun getChildLinkerByIdTest() {
        compartmentLinker?.let { linker ->
            assertNotNull(linker.getChildLinkerById(roleType1!!.id))
            assertEquals(roleTypeLinker1!!, linker.getChildLinkerById(roleType1!!.id)!!)
            assertNotNull(linker.getLinkerById(roleType1!!.id))

            assertNotNull(linker.getChildLinkerById(roleType2!!.id))
            assertEquals(roleTypeLinker2!!, linker.getChildLinkerById(roleType2!!.id)!!)
            assertNotNull(linker.getLinkerById(roleType2!!.id))

            assertNull(linker.getChildLinkerById(roleType3!!.id))

            roleTypeLinker3 = roleGroupLinker1!!.add(roleType3!!)

            assertNotNull(linker.getChildLinkerById(roleType3!!.id))
            assertEquals(roleTypeLinker3!!, linker.getChildLinkerById(roleType3!!.id)!!)
            assertNull(linker.getLinkerById(roleType3!!.id))

            assertNull(linker.getChildLinkerById(0))
            assertNull(linker.getChildLinkerById(255))

        } ?: run {
            fail("CompartmentLinkr object is null. Test setup failed")
        }
    }
}
