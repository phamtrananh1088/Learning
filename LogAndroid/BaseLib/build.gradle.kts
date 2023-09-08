plugins {
    alias(libs.plugins.androidLib)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.googleKsp)
}

android {
    defaultConfig {
        minSdk = 21
    }
    namespace = "jp.co.toukei.log.lib"

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompilerVersion.get()
    }
}

dependencies {
    implementation(project(":Annotation"))
    ksp(project(":Annotation"))
    testImplementation(libs.junit)
    implementation(libs.androidx.startup.runtime)

    api(libs.rxjava)
    implementation(libs.rxandroid)
    api(libs.okhttp)
    api(libs.retrofit2)
    api(libs.retrofit2.adapter.rxjava3)
    api(libs.moshi)
    api(libs.glide)
    ksp(libs.glide.ksp)
    api(libs.play.services.location)
    api(libs.exoplayer)
    api(libs.exoplayer.ui)
    api(libs.kotlinx.coroutines.rx3)
    api(libs.kotlinx.coroutines.android)

    api(libs.room.runtime)
    api(libs.room.ktx)
    api(libs.room.rxjava3)
    api(libs.paging.rxjava3)
    api(libs.paging.runtime.ktx)
    api(libs.material)
    api(libs.camera.lifecycle)
    api(libs.camera.camera2)
    api(libs.camera.view)

    api(libs.androidx.lifecycle.livedata.ktx)
    api(libs.androidx.lifecycle.reactivestreams.ktx)
    api(libs.androidx.lifecycle.viewmodel.ktx)
    api(libs.androidx.lifecycle.runtime.ktx)
    api(libs.androidx.lifecycle.common.java8)

    api(libs.androidx.runtime.rxjava3)
    api(libs.androidx.runtime.livedata)
    api(libs.androidx.ui.graphics)
    api(libs.androidx.animation)
    api(libs.androidx.activity.compose)
    api(libs.androidx.material3)
    api(libs.androidx.navigation.compose)
    api(libs.androidx.core)
    api(libs.androidx.core.ktx)
    api(libs.androidx.appcompat)
    debugApi(libs.androidx.ui.tooling)
    debugApi(libs.androidx.ui.tooling.preview)

    api(libs.paging.compose)
    api(libs.glideCompose)
    api(libs.destinations.animations.core)
    api(libs.destinations.core)
    api(libs.accompanist.permissions)
    api(libs.accompanist.systemuicontroller)
    api(libs.material.icons.extended) //runtimeOnly
    api(libs.sheets)
    api(libs.resaca)
    api(libs.composetooltip)
    api(libs.ui.util)
    api(libs.splitties.views.dsl.material)
}
