package io.framed.model

import io.framed.framework.model.ModelElement
import kotlin.test.*

class EquivalenceTest {
    private var equivalence1: Equivalence? = null
    private var equivalence2: Equivalence? = null
    private var equivalence3: Equivalence? = null

    @BeforeTest
    fun setUpTest() {
        ModelElement.lastId = 0

        equivalence1 = Equivalence()
        equivalence2 = Equivalence(68, 44)
        equivalence3 = Equivalence()
    }

    @AfterTest
    fun tearDownTest() {
        equivalence1 = null
        equivalence2 = null
        equivalence3 = null
    }

    @Test
    fun constructorTest() {
        equivalence1?.let { eqv: Equivalence ->
            assertEquals( "", eqv.name,
                "The initial default name does not match")
            assertEquals( 0, eqv.sourceId,
                "The initial source id does not match")
            assertEquals( 0, eqv.targetId,
                "The initial target id does not match")
        } ?: run {
            fail("Equivalence1 object is null. Test setup failed")
        }

        equivalence2?.let { eqv: Equivalence ->
            assertEquals( 68, eqv.sourceId,
                "The explicitly set source id does not match")
            assertEquals( 44, eqv.targetId,
                "The explicitly set source id does not match")
        } ?: run {
            fail("Equivalence2 object is null. Test setup failed")
        }

        equivalence3?.let { eqv: Equivalence ->
            assertEquals( 2, eqv.sourceId,
                "The counted up source id does not match")
            assertEquals( 2, eqv.targetId,
                "The counted up target id does not match")
        } ?: run {
            fail("Equivalence3 object is null. Test setup failed")
        }
    }

    @Test
    fun copyTest() {

        equivalence1?.let { eqv: Equivalence ->
            val equivalence4 = eqv.copy()

            assertEquals(eqv.name, equivalence4.name,
                "The copied named does not match")
            assertNotEquals(eqv.targetId, equivalence4.targetId,
                "The copied target id does match the original id")
            assertNotEquals(eqv.sourceId, equivalence4.sourceId,
                "The copied source id does match the original id")

            assertEquals(3, equivalence4.sourceId,
                "The counted up source id does not match")
            assertEquals(3, equivalence4.targetId,
                "The counted up target id does not match")
        } ?: run {
            fail("Equivalence1 object is null. Test setup failed")
        }
    }
}
