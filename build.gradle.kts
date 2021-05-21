import java.text.SimpleDateFormat
import java.util.TimeZone
import java.util.Date

plugins {
    kotlin("js") version "1.5.0"
    kotlin("plugin.serialization") version "1.5.0"
    id("com.gorylenko.gradle-git-properties") version "2.2.4"
}

@Suppress("LeakingThis")
open class NodeExec : AbstractExecTask<NodeExec>(NodeExec::class.java) {

    private val nodeJs = org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootPlugin.apply(project.rootProject)

    private val e by lazy {
        nodeJs.requireConfigured().nodeExecutable
    }

    override fun exec() {
        executable = e
        super.exec()
    }

    init {
        dependsOn(nodeJs.npmInstallTaskProvider, "kotlinNpmInstall", "kotlinNodeJsSetup")
    }
}

repositories {
    mavenCentral()
}

kotlin {
    js(IR) {
        browser()
        binaries.executable()

        compilations.all {
            kotlinOptions {
                moduleKind = "commonjs"
            }
        }
    }
}

dependencies {
    // Kotlin std
    implementation(kotlin("stdlib-js"))

    // Serialization library
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.1")

    implementation(devNpm("sass", "1.33.0"))
}

gitProperties {
    extProperty = "gitProps"
    dateFormat = "yyyy-MM-dd HH:mm:ss z"
    dateFormatTimeZone = "UTC"
}

val generateGitProperties = tasks.named<com.gorylenko.GenerateGitPropertiesTask>("generateGitProperties") {
    outputs.upToDateWhen { false }
}

val generateBuildInformation = tasks.create("generateBuildInformation") {
    dependsOn("generateGitProperties")
    val webpackFile = File("$projectDir/webpack.config.d/build.js")

    doLast {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss z")
        format.timeZone = TimeZone.getTimeZone("UTC")
        val buildTime = format.format(Date())

        @Suppress("UNCHECKED_CAST") val git = project.ext["gitProps"] as Map<String, String>

        webpackFile.writeText(
            """
            const webpack = require("webpack")

            const definePlugin = new webpack.DefinePlugin(
               {
                  VCS_BRANCH: "\"${git["git.branch"]}\"",
                  VCS_COMMIT_HASH: "\"${git["git.commit.id.abbrev"]}\"",
                  VCS_COMMIT_MESSAGE: "\"${git["git.commit.message.short"]}\"",
                  VCS_COMMIT_TIME: "\"${git["git.commit.time"]}\"",
                  VCS_TAGS: "\"${git["git.tags"]}\"",
                  VCS_LAST_TAG: "\"${git["git.closest.tag"] ?: ""}\"",
                  VCS_LAST_TAG_DIFF: "\"${git["git.closest.tag.commit.count"]}\"",
                  VCS_DIRTY: "\"${git["git.dirty"]}\"",
                  VCS_COMMIT_COUNT: "\"${git["git.total.commit.count"]}\""
               }
            )

            config.plugins.push(definePlugin)

        """.trimIndent()
        )
    }

    outputs.files(webpackFile)
}

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
tasks.create<NodeExec>("compileSass") {
    dependsOn("processResources")

    args(
        "$buildDir/js/node_modules/sass/sass.js",
        "$projectDir/src/main/resources/public/stylesheets/style.scss",
        "$buildDir/processedResources/js/main/public/stylesheets/style.css"
    )

    outputs.cacheIf { true }
    inputs.dir(file("$projectDir/src/main/resources/public/stylesheets"))
        .withPropertyName("stylesheets")
        .withPathSensitivity(PathSensitivity.RELATIVE)

    outputs.file("$buildDir/processedResources/js/main/public/stylesheets/style.css")
        .withPropertyName("style")
}

tasks.named("browserDevelopmentExecutableDistributeResources") {
    dependsOn("compileSass")
}

tasks.named("browserProductionExecutableDistributeResources") {
    dependsOn("compileSass")
}

tasks.named("productionExecutableCompileSync") {
    dependsOn("compileSass")
}

tasks.named("jsJar") {
    dependsOn("compileSass")
}

tasks.named("browserDevelopmentRun") {
    dependsOn("compileSass")
}

tasks.named("browserDevelopmentWebpack") {
    dependsOn(generateBuildInformation)
}

tasks.named("browserProductionWebpack") {
    dependsOn(generateBuildInformation)
}

tasks.create<Sync>("dist") {
    dependsOn("browserProductionWebpack", "jsJar")

    val file =
        tasks.named("browserProductionWebpack")
            .get().outputs.files.files.first { it.name == "${project.name}.js" }
    val sourceMap = file.resolveSibling("${project.name}.js.map")
    from(
        file,
        sourceMap,
        Callable { zipTree(tasks.get("jsJar").outputs.files.first()) }
    )

    exclude(
        "${project.name}-js/**",
        "${project.name}-js.js",
        "${project.name}-js.js.map",
        "${project.name}-js.meta.js",
        "package.json",
        "META-INF/**",
        "**/*.scss",
        "default/**"
    )

    into("${projectDir}/dist/")
}

tasks.create<Delete>("cleanDist") {
    delete("dist")
}

tasks.named("clean") {
    dependsOn("cleanDist")
}
