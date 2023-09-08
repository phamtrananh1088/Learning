plugins {
    alias(libs.plugins.androidApp) apply false
    alias(libs.plugins.androidLib) apply false
    alias(libs.plugins.kotlinAndroid) apply false
    alias(libs.plugins.googleKsp) apply false
    alias(libs.plugins.googleServices) apply false
    alias(libs.plugins.dexcount) apply false
}

subprojects {
    afterEvaluate {
        if (hasProperty("android")) {
            extensions.configure<com.android.build.gradle.BaseExtension>("android") {
                compileSdkVersion(34)
                defaultConfig {
                    targetSdk = 34
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                }
                compileOptions {
                    isCoreLibraryDesugaringEnabled = true
                    sourceCompatibility = JavaVersion.VERSION_17
                    targetCompatibility = JavaVersion.VERSION_17
                }

                dependencies.add("coreLibraryDesugaring", libs.desugar.jdk.libs)
            }
        }
    }
}
