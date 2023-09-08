plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("com.google.gms.google-services")
//    id("com.getkeepsafe.dexcount")
}

fun stringProperty(key: String): String {
    return property(key) as? String ?: throw IllegalStateException(key)
}

android {
    this.defaultConfig {
        versionCode = 68
        versionName = "1.0.55"
        minSdk = 23
        resourceConfigurations += "ja"
    }
    namespace = "jp.co.toukei.log.trustar"

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
        getByName("debug") {
            isMinifyEnabled = false
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
        }
    }

    flavorDimensions += "api"
    productFlavors {
        val host = "https://www.trustar-system.com"
        // for reverse proxy: `adb reverse tcp:8080 tcp:80`

        create("api-dev") {
            dimension = "api"
            val appCenterKey = "e543548f-cf6c-443c-b561-ee9336d7b1a5"
            buildConfigField("String", "baseUrl", "\"$host/trustar-api-dev/\"")
            buildConfigField("String", "messageBaseUrl", "\"$host/message-api_dev/\"")
            buildConfigField("boolean", "isProduct", "false")
            buildConfigField("String", "appCenterKey", "\"$appCenterKey\"")
        }
        create("api-test") {
            dimension = "api"
            val appCenterKey = "e543548f-cf6c-443c-b561-ee9336d7b1a5"
            buildConfigField("String", "baseUrl", "\"$host/trustar-api-test/\"")
            buildConfigField("String", "messageBaseUrl", "\"$host/message-api_test/\"")
            buildConfigField("boolean", "isProduct", "false")
            buildConfigField("String", "appCenterKey", "\"$appCenterKey\"")
        }
        create("api-product") {
            dimension = "api"
            val appCenterKey = "84c51ad2-d4e6-459e-8821-02ad7b44d962"
            buildConfigField("String", "baseUrl", "\"$host/trustar-api/\"")
            buildConfigField("String", "messageBaseUrl", "\"$host/message-api/\"")
            buildConfigField("boolean", "isProduct", "true")
            buildConfigField("String", "appCenterKey", "\"$appCenterKey\"")
        }
    }
    packagingOptions.resources.excludes += listOf(
        "/META-INF/*.version",
        "/kotlin-tooling-metadata.json",
        "/DebugProbesKt.bin",
        "/okhttp3/internal/publicsuffix/*"  // remove if use cookie.
    )
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = rootProject.extra.get("composeVersion") as String
    }

}

repositories {
    maven("https://jitpack.io")
}

dependencies {
    implementation(project(":BaseLib"))
    implementation(project(":Annotation"))
    kapt(project(":Annotation"))

    //https://jitpack.io/#douglasjunior/android-simple-tooltip
    implementation("com.github.douglasjunior:android-simple-tooltip:1.1.0")
    //https://jitpack.io/#martin-stone/hsv-alpha-color-picker-android
    implementation("com.github.martin-stone:hsv-alpha-color-picker-android:3.0.1")
    //https://jitpack.io/#stfalcon-studio/StfalconImageViewer
    implementation("com.github.stfalcon-studio:StfalconImageViewer:1.0.1")
    implementation("com.google.firebase:firebase-messaging:23.1.1")

    // https://mvnrepository.com/artifact/com.microsoft.appcenter/appcenter
    val appcenter = "5.0.0"
    implementation("com.microsoft.appcenter:appcenter-distribute:$appcenter")
    implementation("com.microsoft.appcenter:appcenter-analytics:$appcenter")
    implementation("com.microsoft.appcenter:appcenter-crashes:$appcenter")

    val datetimePicker = "2.2.7"
    implementation("com.github.florent37:singledateandtimepicker:$datetimePicker")

    val glide = "4.14.2"
    kapt("com.github.bumptech.glide:compiler:$glide")
    val room = "2.4.3"
    kapt("androidx.room:room-compiler:$room")
    val lifecycle = "2.5.1"
    kapt("androidx.lifecycle:lifecycle-compiler:$lifecycle")
}
