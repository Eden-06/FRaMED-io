plugins {
    kotlin("js") version "1.3.71"
    kotlin("plugin.serialization") version "1.3.71"
    id("com.github.node-gradle.node") version "2.2.3"
    id("org.kravemir.gradle.sass") version "1.2.4"
}

apply(from = "version-file.gradle")

repositories {
    jcenter()
    mavenCentral()
}

dependencies {
    // Kotlin std
    implementation(kotlin("stdlib-js"))

    // Serialization library
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-js:0.20.0")

    // Observable library with EventHandler, Properties and ObservableLists
    implementation("de.westermann:KObserve-js:0.9.3")

    testImplementation(kotlin("test-js"))
}

// Config sass build
sass {
    create("main") {
        srcDir = file("$projectDir/src/main/resources/public/stylesheets")
        outDir = file("$buildDir/processedResources/Js/main/public/stylesheets/")

        exclude = "**/*.css"
    }
}

// Config node js environment
node {
    download = true
    workDir = file("${project.projectDir}/build/node")
    npmWorkDir = file("${project.projectDir}/web")
    nodeModulesDir = file("${project.projectDir}/web")
}

// Sass depends on processResources
tasks.named("mainSass") {
    dependsOn("processResources")
}

// jsJar depends on mainSass and versionFile
// Remove duplicate entries
val jsJar = tasks.named<Jar>("JsJar") {
    dependsOn("mainSass", "versionFile")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    from(Callable { configurations["runtimeClasspath"].map { if (it.isDirectory) it else zipTree(it) } })
}

// Copy content from jsJar to website
tasks.create<Sync>("jsSync") {
    dependsOn("JsJar")

    from(Callable { zipTree(jsJar.get().archiveFile) })
    into("${projectDir}/web/website")
}

// Copy content from jsJar to website
tasks.create<Delete>("cleanWebsite") {
    dependsOn("jsSync")

    delete(fileTree("${projectDir}/web/website") {
        include("**/*.kjsm", "**/*.class", "**/*.kn*", "**/*.kotlin_*", "META-INF/", "linkdata/module")
    })

    doLast {
        val emptyDirs = mutableListOf<File>()

        fileTree("${projectDir}/web/website").visit {
            if (file.isDirectory) {
                val children = fileTree(file).filter { it.isFile }.files
                if (children.isEmpty()) {
                    emptyDirs += file
                }
            }
        }

        emptyDirs.asReversed().forEach { it.delete() }
    }
}

// Add task jsSync to build goal
tasks.named("build") {
    dependsOn("cleanWebsite")
}

// Start dev server
tasks.create<com.moowork.gradle.node.task.NodeTask>("run") {
    dependsOn("build", "npmInstall")
    script = file("web/index.js")
}

// Extend delete task to cleanup website
tasks.named("clean") {
    doFirst {
        delete("web/website")
        delete("web/node_modules")
        delete("out")
    }
}

kotlin.target.browser {
    testTask {
        useKarma {
            useFirefox()
        }
    }
}
