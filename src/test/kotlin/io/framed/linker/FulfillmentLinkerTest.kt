package io.framed.linker

import io.framed.model.*
import io.framed.util.TestMatchers
import org.w3c.dom.get
import kotlin.test.*

class FulfillmentLinkerTest {

    private var connectionManagerLinker: ConnectionManagerLinker? = null

    //ModelLinkers
    private var packageElement: Package? = null
    private var packageLinker: PackageLinker? = null

    private var packageElement2: Package? = null
    private var packageLinker2: PackageLinker? = null

    private var compartmentElement: Compartment? = null
    private var compartmentLinker: CompartmentLinker? = null

    private var sceneElement: Scene? = null
    private var sceneLinker: SceneLinker? = null

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

    // Fulfillments
    private var fulfillment: Fulfillment? = null
    private var fulfillmentLinker: FulfillmentLinker? = null

    @BeforeTest
    fun setUpTest() {
        connectionManagerLinker = ConnectionManagerLinker(Connections())

        packageElement = Package()
        packageLinker = PackageLinker(
            packageElement!!,
            connectionManagerLinker!!)

        packageElement2 = Package()
        packageLinker2 = PackageLinker(
            packageElement2!!,
            connectionManagerLinker!!)

        compartmentElement = Compartment()
        compartmentLinker = packageLinker!!.add(compartmentElement!!) as CompartmentLinker

        sceneElement = Scene()
        sceneLinker = packageLinker!!.add(sceneElement!!) as SceneLinker

        roleType1 = RoleType()
        roleType1!!.name = "Role1"
        roleTypeLinker1 = compartmentLinker!!.add(roleType1!!) as RoleTypeLinker

        roleGroup1 = RoleGroup()
        roleGroup1!!.name = "RoleGroup1"
        roleGroupLinker1 = compartmentLinker!!.add(roleGroup1!!) as RoleGroupLinker

        roleType2 = RoleType()
        roleType2!!.name = "Role2"
        roleTypeLinker2 = roleGroupLinker1!!.add(roleType2!!) as RoleTypeLinker

        roleType3 = RoleType()
        roleType3!!.name = "Role3"
        roleTypeLinker3 = sceneLinker!!.add(roleType3!!) as RoleTypeLinker

        classType = Class()
        classTypeLinker = packageLinker!!.add(classType!!) as ClassLinker

        fulfillment = FulfillmentLinker.createModel(classTypeLinker!!.id, roleTypeLinker2!!.id) as Fulfillment

        fulfillmentLinker = connectionManagerLinker!!
            .createConnection(classTypeLinker!!.id, roleTypeLinker2!!.id, FulfillmentLinker.info) as FulfillmentLinker

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

        sceneElement = null
        sceneLinker = null

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

            assertNotNull(linker.pictogram)
            assertNotNull(linker.sidebar)
            assertNotNull(linker.contextMenu)

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
