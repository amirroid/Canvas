import org.gradle.internal.extensions.stdlib.capitalized
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.ksp)
    alias(libs.plugins.androidxRoom)
    alias(libs.plugins.kotlinxSerialization)
    id("kotlin-parcelize")
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
            compilations.all {
                compileTaskProvider.configure {
                    compilerOptions {
                        freeCompilerArgs.addAll(
                            "-P",
                            "plugin:org.jetbrains.kotlin.parcelize:additionalAnnotation=ir.amirroid.canvas.utils.annotations.CommonParcelize"
                        )
                    }
                }
            }
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
            linkerOpts.add("-lsqlite3")
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.materialIconsExtended)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)

            // Kotlin Inject
            implementation(libs.inject.kotlin.inject.runtime)

            // Kotlin Inject Anvil
            implementation(libs.anvil.runtime)
            implementation(libs.anvil.runtime.optional)

            // Circuit
            implementation(libs.circuit.foundation)
            implementation(libs.circuit.codegen.annotations)
            implementation(libs.circuitx.gesture.navigation)
            implementation(libs.circuit.overlay)
            implementation(libs.circuitx.overlay)


            // Room
            implementation(libs.androidx.room.runtime)
            implementation(libs.androidx.sqlite.bundled)

            // Kotlinx
            implementation(libs.kotlinx.collections.immutable)
            implementation(libs.kotlinx.serialization.json)

            // MaterialKolor
            implementation(libs.material.kolor)


            // File Picker (To use directory picker)
            implementation(libs.filekit.dialogs.compose)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "ir.amirroid.canvas"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "ir.amirroid.canvas"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

ksp {
    arg("circuit.codegen.mode", "kotlin_inject_anvil")
    arg(
        "kotlin-inject-anvil-contributing-annotations",
        "com.slack.circuit.codegen.annotations.CircuitInject"
    )
}

dependencies {
    debugImplementation(compose.uiTooling)
    kspCommonMainMetadata(libs.kotlin.inject.compiler.ksp)
    kspCommonMainMetadata(libs.circuit.codegen)
    kspCommonMainMetadata(libs.anvil.compiler)
}

room {
    schemaDirectory("${projectDir.resolve("schemas")}")
}

addKspDependencyForAllTargets(libs.kotlin.inject.compiler.ksp)
addKspDependencyForAllTargets(libs.circuit.codegen)
addKspDependencyForAllTargets(libs.anvil.compiler)
addKspDependencyForAllTargets(libs.androidx.room.compiler)

// source: https://github.com/chrisbanes/tivi/tree/main
private fun Project.addKspDependencyForAllTargets(
    dependencyNotation: Any,
) {
    val kmpExtension = extensions.getByType<KotlinMultiplatformExtension>()
    dependencies {
        kmpExtension.targets
            .asSequence()
            .filter { target ->
                // Don't add KSP for common target, only final platforms
                target.platformType != KotlinPlatformType.common
            }
            .forEach { target ->
                add(
                    "ksp${target.targetName.capitalized()}",
                    dependencyNotation,
                )
            }
    }
}