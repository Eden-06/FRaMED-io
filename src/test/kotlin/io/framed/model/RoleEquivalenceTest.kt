package io.framed.model

import io.framed.framework.model.ModelElement
import kotlin.test.*

class RoleEquivalenceTest {
    private var roleEquivalence1: RoleEquivalence? = null
    private var roleEquivalence2: RoleEquivalence? = null
    private var roleEquivalence3: RoleEquivalence? = null

    @BeforeTest
    fun setUpTest() {
        ModelElement.lastId = 0

        roleEquivalence1 = RoleEquivalence()
        roleEquivalence2 = RoleEquivalence(68, 44)
        roleEquivalence3 = RoleEquivalence()
    }

    @AfterTest
    fun tearDownTest() {
        roleEquivalence1 = null
        roleEquivalence2 = null
        roleEquivalence3 = null
    }

    @Test
    fun constructorTest() {
        roleEquivalence1?.let { eqv: RoleEquivalence ->
            assertEquals( "", eqv.name,
                "The initial default name does not match")
            assertEquals( 0, eqv.sourceId,
                "The initial source id does not match")
            assertEquals( 0, eqv.targetId,
                "The initial target id does not match")
        } ?: run {
            fail("Equivalence1 object is null. Test setup failed")
        }

        roleEquivalence2?.let { eqv: RoleEquivalence ->
            assertEquals( 68, eqv.sourceId,
                "The explicitly set source id does not match")
            assertEquals( 44, eqv.targetId,
                "The explicitly set source id does not match")
        } ?: run {
            fail("Equivalence2 object is null. Test setup failed")
        }

        roleEquivalence3?.let { eqv: RoleEquivalence ->
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

        roleEquivalence1?.let { eqv: RoleEquivalence ->
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
