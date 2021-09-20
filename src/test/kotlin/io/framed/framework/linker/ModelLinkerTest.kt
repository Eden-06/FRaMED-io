package io.framed.framework.linker

import io.framed.linker.*
import io.framed.model.*
import io.framed.util.TestBase
import kotlin.test.*

class ModelLinkerTest : TestBase() {

    @BeforeTest
    override fun setUpTest() {
        super.setUpTest()
    }

    @AfterTest
    override fun tearDownTest() {
        super.tearDownTest()
    }

    @Test
    fun canDropShape() {
        roleGroupLinker1?.let { linker ->
            assertNotNull(roleTypeLinker2)
            assertTrue(compartmentLinker!!.canDropShape(roleTypeLinker2!!.id, linker.id),
                "RoleTypes can be dropped in RoleGroups!")

            assertFalse(packageLinker2!!.canDropShape(roleTypeLinker2!!.id, linker.id),
                "Drag and drop between non-related elements is not possible.")

        } ?: run {
            fail("RoleGroupLinker object is null. Test setup failed")
        }
    }

    @Test
    fun getChildLinkerByIdTest() {
        compartmentLinker?.let { linker ->
            assertNotNull(linker.getChildLinkerById(roleType1!!.id),
                "RoleType1 is a child of the Compartment")
            assertEquals(roleTypeLinker1!!, linker.getChildLinkerById(roleType1!!.id)!!,
                "RoleTypeLinker1 does not match the retrieved linker")
            assertNotNull(linker.getLinkerById(roleType1!!.id),
                "RoleTypeLinker1 is a direct child of the Compartment")

            assertNotNull(linker.getChildLinkerById(roleType2!!.id))
            assertEquals(roleTypeLinker2!!, linker.getChildLinkerById(roleType2!!.id)!!)
            assertNull(linker.getLinkerById(roleType2!!.id))

            assertNull(linker.getChildLinkerById(roleType3!!.id),
                "RoleType3 is not a child of the Compartment")

            roleTypeLinker3 = roleGroupLinker1!!.add(roleType3!!) as RoleTypeLinker

            assertNotNull(linker.getChildLinkerById(roleType3!!.id))
            assertEquals(roleTypeLinker3!!, linker.getChildLinkerById(roleType3!!.id)!!)
            assertNull(linker.getLinkerById(roleType3!!.id))

            assertNull(linker.getChildLinkerById(0))
            assertNull(linker.getChildLinkerById(255))

        } ?: run {
            fail("CompartmentLinker object is null. Test setup failed")
        }
    }
}
