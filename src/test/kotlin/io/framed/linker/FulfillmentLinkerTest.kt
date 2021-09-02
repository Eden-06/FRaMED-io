package io.framed.linker

import io.framed.model.*
import io.framed.util.TestBase
import io.framed.util.TestMatchers
import org.w3c.dom.get
import kotlin.test.*

class FulfillmentLinkerTest : TestBase() {

    // Fulfillments
    var fulfillment: Fulfillment? = null
    var fulfillmentLinker: FulfillmentLinker? = null

    @BeforeTest
    override fun setUpTest() {
        super.setUpTest()

        fulfillment = FulfillmentLinker.createModel(classTypeLinker!!.id, roleTypeLinker2!!.id) as Fulfillment

        fulfillmentLinker = connectionManagerLinker!!
            .createConnection(classTypeLinker!!.id, roleTypeLinker2!!.id, FulfillmentLinker.info) as FulfillmentLinker

    }

    @AfterTest
    override fun tearDownTest() {
        super.tearDownTest()

        fulfillment = null
        fulfillmentLinker = null
    }

    @Test
    fun constructorTest() {
        fulfillmentLinker?.let { linker ->
            assertEquals( classType!!.id, linker.sourceIdProperty.get(),
                "The initial source id does not match")
            assertEquals( roleType2!!.id, linker.targetIdProperty.get(),
                "The initial target id does not match")

            assertNotNull(linker.pictogram,
                "Fulfillment pictogram is null!")
            assertNotNull(linker.sidebar,
                "Fulfillment sidebar is null!")
            assertNotNull(linker.contextMenu,
                "Fulfillment contex menu is null!")

        } ?: run {
            fail("FulfillmentLinker object is null. Test setup failed")
        }
    }

    @Test
    fun contextMenuTest() {
        fulfillmentLinker?.let { linker ->
            assertEquals(1, linker.contextMenu.html.children.length, "Context menu is missing child elements.")

            linker.contextMenu.html.children[0]?.let { contextMenuListView ->
                TestMatchers.contextMenuMatcher(
                    "Fulfillment: ",
                    "Delete"
                )(contextMenuListView)
            }
        } ?: run {
            fail("FulfillmentLinker object is null. Test setup failed")
        }
    }

    @Test
    fun sidebarTest() {
        fulfillmentLinker?.let { linker ->
            TestMatchers.sidebarMatcher(
                "Fulfillment",
                TestMatchers.sidebarSectionMatcher(
                    "General",
                    TestMatchers.inputFieldMatcher("Name")
                )
            )(linker.sidebar.html)
        } ?: run {
            fail("FulfillmentLinker object is null. Test setup failed")
        }
    }

    @Test
    fun isLinkerForTest() {
        fulfillmentLinker?.let { linker ->
            // Check isLinkerFor
            assertTrue(FulfillmentLinker.isLinkerFor(linker))
            assertFalse(FulfillmentLinker.isLinkerFor(roleTypeLinker1!!))
            assertFalse(FulfillmentLinker.isLinkerFor(classTypeLinker!!))
        } ?: run {
            fail("FulfillmentLinker object is null. Test setup failed")
        }
    }

    @Test
    fun canStartTest() {
        assertTrue(FulfillmentLinker.canStart(roleTypeLinker1!!))
        assertFalse(FulfillmentLinker.canStart(sceneLinker!!))
        assertTrue(FulfillmentLinker.canStart(classTypeLinker!!))
        assertTrue(FulfillmentLinker.canStart(compartmentLinker!!))
        assertFalse(FulfillmentLinker.canStart(roleGroupLinker1!!))
    }

    @Test
    fun canCreateTest() {


        assertTrue(FulfillmentLinker.canCreate(classTypeLinker!!, roleTypeLinker1!!),
            "Fulfillment relations between a class and a RoleType in a Compartment should be possible!")
        assertTrue(FulfillmentLinker.canCreate(classTypeLinker!!, roleTypeLinker2!!),
            "Fulfillment relations between a class and a RoleType in a RoleGroup should be possible!")
        assertFalse(FulfillmentLinker.canCreate(roleTypeLinker1!!, classTypeLinker!!),
            "Fulfillments cannot be created from a RoleType to a rigid type.")
        assertFalse(FulfillmentLinker.canCreate(roleTypeLinker1!!, sceneLinker!!),
            "Fulfillments cannot be created from a RoleType to a rigid type.")

        assertTrue(FulfillmentLinker.canCreate(compartmentLinker!!, roleTypeLinker1!!),
            "Fulfillments can be created from a Compartment to a RoleType child")
        assertFalse(FulfillmentLinker.canCreate(roleTypeLinker1!!, compartmentLinker!!),
            "Fulfillments cannot be created from a RoleType to a rigid type")

        assertTrue(FulfillmentLinker.canCreate(roleTypeLinker1!!, roleTypeLinker2!!),
                "Fulfillments can be created between RoleTypes that are in the same compartment")
    }

    @Test
    fun createLinkerTest() {
        fulfillmentLinker?.let { _ ->
            assertNotNull(FulfillmentLinker.createLinker(
                FulfillmentLinker.createModel(classType!!.id, roleType2!!.id),
                connectionManagerLinker!!))

            assertFailsWith(IllegalArgumentException::class,
                "Illegal argument exception was not thrown",
                block = {
                    FulfillmentLinker.createLinker(
                        RoleImplicationLinker.createModel(roleType1!!.id, roleType2!!.id),
                        connectionManagerLinker!!)
                }
            )
        } ?: run {
            fail("FulfillmentLinker object is null. Test setup failed")
        }
    }

    @Test
    fun swapTest() {
        fulfillmentLinker?.let { linker ->
            assertFalse(linker.canSwap(FulfillmentLinker.info), "Cannot swap symmetrical connection!")
            assertEquals(classType!!.id, linker.sourceIdProperty.value, "Unexpected initial source id!")
            assertEquals(roleType2!!.id, linker.targetIdProperty.value, "Unexpected initial target id!")
        } ?: run {
            fail("FulfillmentLinker object is null. Test setup failed")
        }
    }

    @Test
    fun deleteTest() {
        fulfillmentLinker?.let { linker ->
            assertTrue(connectionManagerLinker!!.connections.contains(linker),
                "Linker not found in the connectionManagerLinker")
            linker.delete()
            assertFalse(connectionManagerLinker!!.connections.contains(linker),
                "Deleted linker found in the connectionManagerLinker")
        } ?: run {
            fail("FulfillmentLinker object is null. Test setup failed")
        }
    }
}
