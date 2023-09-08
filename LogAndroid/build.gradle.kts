import com.android.build.gradle.BaseExtension

buildscript {
    repositories {
        mavenCentral()
        google()
    }

    dependencies {
        classpath(kotlin("gradle-plugin", "1.7.21"))
        classpath("com.android.tools.build:gradle:7.3.1")
        classpath("com.getkeepsafe.dexcount:dexcount-gradle-plugin:3.1.0")
        classpath("com.google.gms:google-services:4.3.14")
    }
}

allprojects {
    repositories {
        mavenCentral()
        google()
    }
}

subprojects {
    afterEvaluate {
        if (hasProperty("android")) {
            extensions.configure<BaseExtension>("android") {
                compileSdkVersion(33)
                defaultConfig {
                    targetSdk = 33
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                }
                compileOptions {
                    isCoreLibraryDesugaringEnabled = true
                    sourceCompatibility = JavaVersion.VERSION_1_8
                    targetCompatibility = JavaVersion.VERSION_1_8
                }
                dependencies.add(
                    "coreLibraryDesugaring",
                    "com.android.tools:desugar_jdk_libs:1.2.0"
                )
                buildTypes {
                    getByName("debug") {
                        buildConfigField("boolean", "isDebug", "true")
                    }
                    getByName("release") {
                        buildConfigField("boolean", "isDebug", "false")
                    }
                }
            }
        }
    }
}
