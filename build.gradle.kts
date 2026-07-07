import nl.littlerobots.vcu.plugin.resolver.VersionSelectors
import java.util.Properties

// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    dependencies {
        classpath(libs.kotlin.gradle.plugin)
    }
}

plugins {
    idea
    alias(libs.plugins.versionCatalogUpdate)
    alias(libs.plugins.android.library) apply false
}
val buildEnv by lazy {
    @Suppress("UNCHECKED_CAST")
    Properties().apply {
        rootProject.file("buildEnv.properties").reader().use(::load)
    } as Map<String, *>
}

val artifactGroup: String = extra["artifactGroup"] as String
val artifactVersion: String = extra["artifactVersion"] as String

allprojects {
    group = artifactGroup
    version = artifactVersion
    ext {
        buildEnv.forEach { (key, value) ->
            set(key, value)
        }
    }
}

idea {
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}

versionCatalogUpdate {
    sortByKey = true
    versionSelector(VersionSelectors.PREFER_STABLE)
}

@Suppress("unused")
tasks {
    wrapper {
        gradleVersion = "latest"
    }
    register("updateWrapperAndCatalog") {
        dependsOn(wrapper, "versionCatalogUpdate")
    }
    register<Delete>("clean") {
        delete(file("build"), file(".m2-project"))
    }
}