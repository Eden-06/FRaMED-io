package io.framed.framework.model

import io.framed.model.RoleEquivalence
import io.framed.model.RoleImplication
import io.framed.model.RoleProhibition
import kotlin.test.*

class ModelElementTest {
    private var roleEquivalence: ModelElement? = null
    private var roleImplication: ModelElement? = null
    private var roleProhibition: ModelElement? = null

    @BeforeTest
    fun setUpTest() {
        ModelElement.lastId = 0

        roleEquivalence = RoleEquivalence()
        roleImplication = RoleImplication(68, 44)
        roleProhibition = RoleProhibition()
    }

    @AfterTest
    fun tearDownTest() {
        roleEquivalence = null
        roleImplication = null
        roleProhibition = null
    }

    @Test
    fun constructorTest() {
        roleEquivalence?.let { eqv: ModelElement ->
            assertEquals( 0, eqv.id,
                "The initial id does not match")
        } ?: run {
            fail("RoleEquivalence object is null. Test setup failed")
        }

        roleImplication?.let { eqv: ModelElement ->
            assertEquals( 1, eqv.id,
                "The counted up id does not match")
        } ?: run {
            fail("RoleImplication object is null. Test setup failed")
        }

        roleProhibition?.let { eqv: ModelElement ->
            assertEquals( 2, eqv.id,
                "The counted up id does not match")
        } ?: run {
            fail("RoleProhibition object is null. Test setup failed")
        }
    }

    @Test
    fun copyTest() {

        roleEquivalence?.let { eqv: ModelElement ->
            val equivalence4 = eqv.copy()

            assertNotEquals(eqv.id, equivalence4.id,
                "The copied id does match the original id")

            assertEquals(3, equivalence4.id,
                "The counted up id does not match")
        } ?: run {
            fail("RoleEquivalence object is null. Test setup failed")
        }
    }
}
