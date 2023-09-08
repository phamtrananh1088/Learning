plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
}

android {
    this.defaultConfig {
        minSdk = 21
    }
    namespace = "jp.co.toukei.log.lib"
}

dependencies {
    implementation(project(":Annotation"))
    kapt(project(":Annotation"))
    testImplementation("junit:junit:4.13.2")

    // https://mvnrepository.com/artifact/io.reactivex.rxjava3/rxjava
    val rxJava = "3.1.5"
    api("io.reactivex.rxjava3:rxjava:$rxJava")
    // https://mvnrepository.com/artifact/io.reactivex.rxjava3/rxandroid
    val rxAndroid = "3.0.2"
    implementation("io.reactivex.rxjava3:rxandroid:$rxAndroid")
    // https://mvnrepository.com/artifact/com.squareup.okhttp3/okhttp
    val okHttp3 = "4.10.0"
    api("com.squareup.okhttp3:okhttp:$okHttp3")
    // https://mvnrepository.com/artifact/com.squareup.retrofit2/retrofit
    val retrofit = "2.9.0"
    api("com.squareup.retrofit2:retrofit:$retrofit")
    api("com.squareup.retrofit2:adapter-rxjava3:$retrofit")
    // https://mvnrepository.com/artifact/com.squareup.moshi/moshi
    val moshi = "1.14.0"
    api("com.squareup.moshi:moshi:$moshi")
    // https://mvnrepository.com/artifact/com.github.bumptech.glide/glide
    val glide = "4.14.2"
    api("com.github.bumptech.glide:glide:$glide")
    kapt("com.github.bumptech.glide:compiler:$glide")
    // https://mvnrepository.com/artifact/com.google.android.flexbox/flexbox
    val flexBox = "3.0.0"
    api("com.google.android.flexbox:flexbox:$flexBox")
    // https://mvnrepository.com/artifact/com.google.android.gms/play-services-location
    val location = "21.0.1"
    api("com.google.android.gms:play-services-location:$location")
    // https://mvnrepository.com/artifact/com.google.android.exoplayer/exoplayer
    val exoplayer = "2.18.2"
    api("com.google.android.exoplayer:exoplayer:$exoplayer")
    api("com.google.android.exoplayer:exoplayer-ui:$exoplayer")
    // https://mvnrepository.com/artifact/com.louiscad.splitties/splitties-views
    val splitties = "3.0.0"
    api("com.louiscad.splitties:splitties-views-dsl-material:$splitties")
    // https://mvnrepository.com/artifact/org.jetbrains.kotlinx/kotlinx-coroutines-android
    val coroutines = "1.6.4"
    api("org.jetbrains.kotlinx:kotlinx-coroutines-rx3:$coroutines")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutines")

    // https://mvnrepository.com/artifact/androidx.activity/activity
    val activity = "1.6.1"
    api("androidx.activity:activity:$activity")
    // https://mvnrepository.com/artifact/androidx.appcompat/appcompat
    val appCompat = "1.5.1"
    api("androidx.appcompat:appcompat:$appCompat")
    // https://mvnrepository.com/artifact/androidx.core/core-ktx
    val core = "1.9.0"
    api("androidx.core:core:$core")
    api("androidx.core:core-ktx:$core")
    // https://mvnrepository.com/artifact/androidx.room/room-ktx
    val room = "2.4.3"
    api("androidx.room:room-runtime:$room")
    api("androidx.room:room-ktx:$room")
    api("androidx.room:room-rxjava3:$room")
    // https://mvnrepository.com/artifact/androidx.paging/paging-runtime-ktx
    val paging = "3.1.1"
    api("androidx.paging:paging-rxjava3:$paging")
    api("androidx.paging:paging-runtime-ktx:$paging")
    // https://mvnrepository.com/artifact/com.google.android.material/material
    val materialComponents = "1.7.0"
    api("com.google.android.material:material:$materialComponents")
    // https://mvnrepository.com/artifact/androidx.camera/camera-camera2
    val camerax = "1.2.0-beta02"
    api("androidx.camera:camera-lifecycle:$camerax")
    api("androidx.camera:camera-camera2:$camerax")
    // https://mvnrepository.com/artifact/androidx.camera/camera-view
    val cameraView = "1.2.0-beta02"
    api("androidx.camera:camera-view:$cameraView")
    // https://mvnrepository.com/artifact/androidx.lifecycle/lifecycle-common
    val lifecycle = "2.5.1"
    api("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle")
    api("androidx.lifecycle:lifecycle-reactivestreams-ktx:$lifecycle")
    api("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle")
    api("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle")
    // https://mvnrepository.com/artifact/androidx.swiperefreshlayout/swiperefreshlayout
    val swiperefreshlayout = "1.2.0-alpha01"
    api("androidx.swiperefreshlayout:swiperefreshlayout:$swiperefreshlayout")
    // https://mvnrepository.com/artifact/androidx.recyclerview/recyclerview
    val recyclerView = "1.2.1"
    api("androidx.recyclerview:recyclerview:$recyclerView")
    // https://mvnrepository.com/artifact/androidx.recyclerview/recyclerview-selection
    val recyclerViewSelection = "1.2.0-alpha01"
    api("androidx.recyclerview:recyclerview-selection:$recyclerViewSelection")

    val compose = "1.4.0-alpha02"
    rootProject.extra["composeVersion"] = compose
    api("androidx.compose.runtime:runtime-rxjava3:$compose")
    api("androidx.compose.runtime:runtime-livedata:$compose")
    api("androidx.compose.material:material:$compose")
    api("androidx.compose.animation:animation:$compose")
    api("androidx.compose.ui:ui-tooling-preview:$compose")
    api("androidx.paging:paging-compose:1.0.0-alpha17")
    api("com.github.bumptech.glide:compose:1.0.0-alpha.1")

    api("com.github.skgmn:composetooltip:0.2.0")
}
