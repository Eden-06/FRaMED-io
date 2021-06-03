package io.framed.framework.model

import io.framed.model.RoleEquivalence
import io.framed.model.RoleImplication
import io.framed.model.RoleProhibition
import kotlin.test.*

class ModelConnectionTest {

    private var roleEquivalence: ModelConnection? = null
    private var roleImplication: ModelConnection? = null
    private var roleProhibition: ModelConnection? = null

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
        roleEquivalence?.let { eqv: ModelConnection ->
            assertEquals( 0, eqv.sourceId,
                "The initial source id does not match")
            assertEquals( 0, eqv.targetId,
                "The initial target id does not match")
        } ?: run {
            fail("RoleEquivalence object is null. Test setup failed")
        }

        roleImplication?.let { eqv: ModelConnection ->
            assertEquals( 68, eqv.sourceId,
                "The explicitly set source id does not match")
            assertEquals( 44, eqv.targetId,
                "The explicitly set source id does not match")
        } ?: run {
            fail("RoleImplication object is null. Test setup failed")
        }

        roleProhibition?.let { eqv: ModelConnection ->
            assertEquals( 2, eqv.sourceId,
                "The counted up source id does not match")
            assertEquals( 2, eqv.targetId,
                "The counted up target id does not match")
        } ?: run {
            fail("RoleProhibition object is null. Test setup failed")
        }
    }

    @Test
    fun copyTest() {

        roleEquivalence?.let { eqv: ModelConnection ->
            val equivalence4 = eqv.copy()

            assertNotEquals(eqv.targetId, equivalence4.targetId,
                "The copied target id does match the original id")
            assertNotEquals(eqv.sourceId, equivalence4.sourceId,
                "The copied source id does match the original id")

            assertEquals(3, equivalence4.sourceId,
                "The counted up source id does not match")
            assertEquals(3, equivalence4.targetId,
                "The counted up target id does not match")
        } ?: run {
            fail("RoleEquivalence object is null. Test setup failed")
        }
    }

}
