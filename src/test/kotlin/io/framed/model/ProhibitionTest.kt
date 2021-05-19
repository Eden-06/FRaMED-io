package io.framed.model

import io.framed.framework.model.ModelElement
import kotlin.test.*

/**
 * Unit tests for the Prohibition model connection.
 *
 * This tests setup relies on the companion object variable of the ModelElement for the id counting.
 * This can result in failures in test execution.
 *
 * @see Prohibition
 * @author David Oberacker
 */
class ProhibitionTest {

    private var prohibition1 : Prohibition? = null
    private var prohibition2 : Prohibition? = null
    private var prohibition3 : Prohibition? = null

    @BeforeTest
    fun setUpTest() {
        ModelElement.lastId = 0

        prohibition1 = Prohibition()
        prohibition2 = Prohibition(10, 100)
        prohibition3 = Prohibition()
    }

    @AfterTest
    fun tearDownTest() {
        prohibition1 = null
        prohibition2 = null
        prohibition3 = null
    }

    @Test
    fun constructorTest() {
        prohibition1?.let { pro: Prohibition ->
            assertEquals( "", pro.name,
                "The initial default name does not match")
            assertEquals( 0, pro.sourceId,
                "The initial source id does not match")
            assertEquals( 0, pro.targetId,
                "The initial target id does not match")
        } ?: run {
            fail("Prohibition1 object is null. Test setup failed")
        }

        prohibition2?.let { pro: Prohibition ->
            assertEquals( 10, pro.sourceId,
                "The explicitly set source id does not match")
            assertEquals( 100, pro.targetId,
                "The explicitly set source id does not match")
        } ?: run {
            fail("Prohibition2 object is null. Test setup failed")
        }

        prohibition3?.let { pro: Prohibition ->
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

        prohibition1?.let { pro: Prohibition ->
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
