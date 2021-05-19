package io.framed.model

import io.framed.framework.model.ModelElement
import kotlin.test.*

/**
 * Unit tests for the implication role constraint model class.
 *
 * This tests setup relies on the companion object variable of the ModelElement for the id counting.
 * It alters the value of the global counter to the value 0 before each test.
 * This side effect has to be recognized by other tests.
 *
 * @see Implication
 * @author David Oberacker
 */
class ImplicationTest {
    private var implication1 : Implication? = null
    private var implication2 : Implication? = null
    private var implication3 : Implication? = null

    @BeforeTest
    fun setUpTest() {
        ModelElement.lastId = 0

        implication1 = Implication()
        implication2 = Implication(99, 76)
        implication3 = Implication()
    }

    @AfterTest
    fun tearDownTest() {
        implication1 = null
        implication2 = null
        implication3 = null
    }

    @Test
    fun constructorTest() {
        implication1?.let { it: Implication ->
            assertEquals( "", it.name,
                "The initial default name does not match")
            assertEquals( 0, it.sourceId,
                "The initial source id does not match")
            assertEquals( 0, it.targetId,
                "The initial target id does not match")
        } ?: run {
            fail("Implication1 object is null. Test setup failed")
        }

        implication2?.let { it: Implication ->
            assertEquals( 99, it.sourceId,
                "The explicitly set source id does not match")
            assertEquals( 76, it.targetId,
                "The explicitly set source id does not match")
        } ?: run {
            fail("Implication2 object is null. Test setup failed")
        }

        implication3?.let { it: Implication ->
            assertEquals( 2, it.sourceId,
                "The counted up source id does not match")
            assertEquals( 2, it.targetId,
                "The counted up target id does not match")
        } ?: run {
            fail("Implication3 object is null. Test setup failed")
        }
    }

    @Test
    fun copyTest() {
        implication1?.let { it: Implication ->
            val inheritance4 = it.copy()

            assertEquals(it.name, inheritance4.name,
                "The copied named does not match")
            assertNotEquals(it.targetId, inheritance4.targetId,
                "The copied target id does match the original id")
            assertNotEquals(it.sourceId, inheritance4.sourceId,
                "The copied source id does match the original id")

            assertEquals(3, inheritance4.sourceId,
                "The counted up source id does not match")
            assertEquals(3, inheritance4.targetId,
                "The counted up target id does not match")
        } ?: run {
            fail("Implication1 object is null. Test setup failed")
        }
    }
}
