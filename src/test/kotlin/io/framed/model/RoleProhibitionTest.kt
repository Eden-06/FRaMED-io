package io.framed.model

import io.framed.framework.model.ModelElement
import kotlin.test.*

/**
 * Unit tests for the Prohibition model connection.
 *
 * This tests setup relies on the companion object variable of the ModelElement for the id counting.
 * This can result in failures in test execution.
 *
 * @see RoleProhibition
 * @author David Oberacker
 */
class RoleProhibitionTest {

    private var roleProhibition1 : RoleProhibition? = null
    private var roleProhibition2 : RoleProhibition? = null
    private var roleProhibition3 : RoleProhibition? = null

    @BeforeTest
    fun setUpTest() {
        ModelElement.lastId = 0

        roleProhibition1 = RoleProhibition()
        roleProhibition2 = RoleProhibition(10, 100)
        roleProhibition3 = RoleProhibition()
    }

    @AfterTest
    fun tearDownTest() {
        roleProhibition1 = null
        roleProhibition2 = null
        roleProhibition3 = null
    }

    @Test
    fun constructorTest() {
        roleProhibition1?.let { pro: RoleProhibition ->
            assertEquals( "", pro.name,
                "The initial default name does not match")
            assertEquals( 0, pro.sourceId,
                "The initial source id does not match")
            assertEquals( 0, pro.targetId,
                "The initial target id does not match")
        } ?: run {
            fail("Prohibition1 object is null. Test setup failed")
        }

        roleProhibition2?.let { pro: RoleProhibition ->
            assertEquals( 10, pro.sourceId,
                "The explicitly set source id does not match")
            assertEquals( 100, pro.targetId,
                "The explicitly set source id does not match")
        } ?: run {
            fail("Prohibition2 object is null. Test setup failed")
        }

        roleProhibition3?.let { pro: RoleProhibition ->
            assertEquals( 2, pro.sourceId,
                "The counted up source id does not match")
            assertEquals( 2, pro.targetId,
                "The counted up target id does not match")
        } ?: run {
            fail("Prohibition3 object is null. Test setup failed")
        }
    }

    @Test
    fun copyTest() {

        roleProhibition1?.let { pro: RoleProhibition ->
            val prohibition4 = pro.copy()

            assertEquals(pro.name, prohibition4.name,
                "The copied named does not match")
            assertNotEquals(pro.targetId, prohibition4.targetId,
                "The copied target id does match the original id")
            assertNotEquals(pro.sourceId, prohibition4.sourceId,
                "The copied source id does match the original id")

            assertEquals(3, prohibition4.sourceId,
                "The counted up source id does not match")
            assertEquals(3, prohibition4.targetId,
                "The counted up target id does not match")
        } ?: run {
            fail("Prohibition1 object is null. Test setup failed")
        }
    }
}
