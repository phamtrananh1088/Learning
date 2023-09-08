@file:Suppress("UnstableApiUsage")

plugins {
    alias(libs.plugins.androidApp)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.googleKsp)
    alias(libs.plugins.googleServices)
}

android {
    defaultConfig {
        versionCode = 78
        versionName = "1.0.55"
        minSdk = 23
        resourceConfigurations += "ja"

        ksp {
            arg("room.generateKotlin", "true")
        }
    }
    namespace = "jp.co.toukei.log.trustar"

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
            buildConfigField("boolean", "isDebug", "false")
        }
        debug {
            isMinifyEnabled = false
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
            buildConfigField("boolean", "isDebug", "true")
        }
    }

    flavorDimensions += "api"
    productFlavors {

        fun com.android.build.api.dsl.VariantDimension.buildConfig(prefix: String) {
            val appCenterKey = property("$prefix.app_center_key")!!
            val api = property("$prefix.api")!!
            val apiMsg = property("$prefix.api_msg")!!
            val msgApiKey = property("$prefix.msg_api_key")!!
            buildConfigField("String", "baseUrl", "\"$api\"")
            buildConfigField("String", "messageBaseUrl", "\"$apiMsg\"")
            buildConfigField("String", "appCenterKey", "\"$appCenterKey\"")
            buildConfigField("String", "msgApiKey", "\"$msgApiKey\"")
        }
        create("api-dev") {
            dimension = "api"
            buildConfig("dev")
            buildConfigField("boolean", "isProduct", "false")
        }
        create("api-test") {
            dimension = "api"
            buildConfig("test")
            buildConfigField("boolean", "isProduct", "false")
        }
        create("api-product") {
            dimension = "api"
            buildConfig("product")
            buildConfigField("boolean", "isProduct", "true")
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
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompilerVersion.get()
    }

    kotlinOptions {
        freeCompilerArgs += arrayOf(
            "-Xcontext-receivers",
            "-P",
            "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=${buildDir}",
            "-P",
            "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=${buildDir}"

        )
    }
}

dependencies {
    implementation(project(":BaseLib"))
    implementation(project(":Annotation"))
    ksp(project(":Annotation"))

    implementation(libs.firebase.messaging)

    releaseImplementation(libs.appcenter.distribute)
    releaseImplementation(libs.appcenter.analytics)
    releaseImplementation(libs.appcenter.crashes)

    ksp(libs.glide.ksp)
    ksp(libs.room.compiler)
    ksp(libs.destinations)
//    debugImplementation(libs.okhttp.interceptor)

    //remove..................
    //https://jitpack.io/#douglasjunior/android-simple-tooltip
    implementation("com.github.douglasjunior:android-simple-tooltip:1.1.0")
    //https://jitpack.io/#stfalcon-studio/StfalconImageViewer
    implementation("com.github.stfalcon-studio:StfalconImageViewer:1.0.1")
    // https://mvnrepository.com/artifact/androidx.recyclerview/recyclerview
    val recyclerView = "1.3.1"
    implementation("androidx.recyclerview:recyclerview:$recyclerView")
    // https://mvnrepository.com/artifact/androidx.recyclerview/recyclerview-selection
    val recyclerViewSelection = "1.2.0-alpha01"
    implementation("androidx.recyclerview:recyclerview-selection:$recyclerViewSelection")
}
