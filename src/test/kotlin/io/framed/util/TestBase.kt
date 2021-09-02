package io.framed.util

import io.framed.linker.*
import io.framed.model.*
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

/**
 * Class setting up a test environment that can be used with other tests to avoid redundant initalizations.
 * This class sets up a model with the following structure:
 * ```
 * Package1
 * | Compartment
 * | | RoleType1
 * | | RoleGroup1
 * | \ \ RoleType2
 * | Scene
 * | \ RoleType3
 * \ Class
 * Package2
 * ```
 * Classes using this for test setup should override the [setUpTest] and [tearDownTest]
 * functions to initialize their specific properties but call the super implementation
 * to ensure that all properties are initialized.
 * The overridden methods should have the [BeforeTest] or [AfterTest] annotations.
 *
 * @author David Oberacker (david.oberacker@student.kit.edu)
 */
open class TestBase {

    var connectionManagerLinker: ConnectionManagerLinker? = null

    //ModelLinkers
    var packageElement: Package? = null
    var packageLinker: PackageLinker? = null

    var packageElement2: Package? = null
    var packageLinker2: PackageLinker? = null

    var compartmentElement: Compartment? = null
    var compartmentLinker: CompartmentLinker? = null

    var sceneElement: Scene? = null
    var sceneLinker: SceneLinker? = null

    var roleGroup1: RoleGroup? = null
    var roleGroupLinker1: RoleGroupLinker? = null

    // RoleTypes
    var roleType1: RoleType? = null
    var roleTypeLinker1: RoleTypeLinker? = null

    var roleType2: RoleType? = null
    var roleTypeLinker2: RoleTypeLinker? = null

    var roleType3: RoleType? = null
    var roleTypeLinker3: RoleTypeLinker? = null

    // ClassType
    var classType: Class? = null
    var classTypeLinker: ClassLinker? = null


    @BeforeTest
    open fun setUpTest() {
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


    }

    @AfterTest
    open fun tearDownTest() {
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

    }

}
