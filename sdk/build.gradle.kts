import com.github.jk1.license.filter.LicenseBundleNormalizer
import com.github.jk1.license.render.JsonReportRenderer
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

class EnvOrLocal {
    private val localProperties by lazy {
        @Suppress("UNCHECKED_CAST")
        Properties().apply {
            rootDir.resolve("local.properties")
                .takeIf(File::exists)
                ?.reader()
                ?.use(::load)
        } as Map<String, String>
    }

    operator fun get(
        envKey: String,
        localKey: String = envKey.replace('_', '.').lowercase()
    ): String? {
        return providers.environmentVariable(envKey).orNull ?: localProperties[localKey]
    }
}

val envOrLocal = EnvOrLocal()

plugins {
    `maven-publish`
//    jacoco
    alias(libs.plugins.android.library)
    alias(libs.plugins.com.github.jk1.license.report)
}

val artifactGroup: String = extra["artifactGroup"] as String
val artifactName: String = extra["artifactName"] as String
val artifactVersion: String = extra["artifactVersion"] as String
val androidMinSdk: String = extra["androidMinSdk"] as String
val androidCompileSdk: String = extra["androidCompileSdk"] as String
val androidTestTargetSdk: String = extra["androidTestTargetSdk"] as String
val androidBuildTools: String = extra["androidBuildTools"] as String
val jvmToolchain: String = extra["jvmToolchain"] as String

android {
    namespace = "de.gematik.ti.erp.app"
    buildToolsVersion = androidBuildTools

    compileSdk {
        version = release(androidCompileSdk.toInt())
    }

    defaultConfig {
        minSdk = androidMinSdk.toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        debug {
            isDefault = true
            enableUnitTestCoverage = true
            isMinifyEnabled = false
        }
        release {
            isDefault = false
            enableUnitTestCoverage = true
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        buildConfig = true
    }

    testOptions {
        targetSdk = androidTestTargetSdk.toInt()
    }

    publishing {
        multipleVariants("all") {
            allVariants()
            withJavadocJar()
            withSourcesJar()
        }
    }
}

kotlin {
    jvmToolchain(jvmToolchain.toInt())
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_11)
    }
}

dependencies {
    // Android Ecosystem
    implementation(libs.androidx.core.ktx)
    // Kotlin Ecosystem
    api(libs.kotlin.stdlib.jdk8)
    // Bouncycastle
    implementation(libs.bouncycastle.asn1)
    implementation(libs.kotlinx.serialization.core)
}

licenseReport {
    // configurations = com.github.jk1.license.LicenseReportExtension.ALL
    configurations = arrayOf("releaseRuntimeClasspath")

    val outputDirFromEnv = envOrLocal["GRADLE_LICENSE_REPORT_OUT"]
    outputDir = if (outputDirFromEnv.isNullOrBlank()) {
        layout.buildDirectory.get().dir("reports").file("dependency-license").toString()
    } else {
        outputDirFromEnv
    }
    allowedLicensesFile = File("$rootDir/config/allowed-licenses.json")
    excludeBoms = true
    renderers = arrayOf(
        JsonReportRenderer(
            "license-details.json",
            false
        )
    )
    filters = arrayOf(
        LicenseBundleNormalizer(
            "$rootDir/config/license-normalizer-bundle.json",
            true
        )
    )
}

publishing {

    val baseConfig: (MavenPublication).() -> Unit = {
        groupId = artifactGroup
        artifactId = artifactName
        afterEvaluate {
            from(components["all"])
        }
    }

    publications {
        create("release", MavenPublication::class.java) {
            baseConfig()
            version = artifactVersion
        }
        create("snapshot", MavenPublication::class.java) {
            baseConfig()
            version = "$artifactVersion-SNAPSHOT"
        }
    }

    repositories {
        maven(rootProject.file(".m2-project")) {
            name = "projectMaven"
        }

        val artifactoryUrl = envOrLocal["ARTIFACTORY_URL"]
        val artifactorySnapshotUrl = envOrLocal["ARTIFACTORY_SNAPSHOT_URL"]
        val artifactoryUser = envOrLocal["ARTIFACTORY_USER"]
        val artifactoryToken = envOrLocal["ARTIFACTORY_TOKEN"]

        if (artifactoryUrl != null) {
            maven {
                name = "artifactoryRelease"
                url = uri(artifactoryUrl)
                credentials {
                    username = artifactoryUser
                    password = artifactoryToken
                }
            }
        }

        if (artifactorySnapshotUrl != null) {
            maven {
                name = "artifactorySnapshot"
                url = uri(artifactorySnapshotUrl)
                credentials {
                    username = artifactoryUser
                    password = artifactoryToken
                }
            }
        }

    }
}
