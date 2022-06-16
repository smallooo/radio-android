plugins {
    id("nowinandroid.android.library")
    id("nowinandroid.android.library.jacoco")
    kotlin("kapt")
    id("kotlinx-serialization")
    id("dagger.hilt.android.plugin")
    id("nowinandroid.spotless")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.2.0-beta01"
    }

}


dependencies {
    implementation(project(":core-playback"))
    implementation(project(":common-compose"))

    testImplementation(project(":core-testing"))

//    implementation(libs.kotlinx.coroutines.android)
//    implementation(libs.kotlinx.serialization.json)
//    implementation(libs.kotlinx.datetime)
//    implementation(libs.okhttp.logging)
//    implementation(libs.retrofit.core)
//    implementation(libs.retrofit.kotlin.serialization)
    implementation(libs.hilt.android)
    implementation(libs.hilt.compiler)
    kapt(libs.hilt.compiler)
    //implementation(libs.androidx.compose.ui.util)
    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.material.material)
    implementation(libs.androidx.lifecycle.viewmodelKtx)
//    implementation(libs.retrofit.converter)
//    implementation(libs.google.gson)
//
//    implementation(libs.androidx.hilt.navigation.compose)
//
    implementation(libs.androidx.media)
//    implementation(libs.google.exoPlayer)
//    implementation(libs.google.exoPlayer.okhttp)
}