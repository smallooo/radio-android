plugins {
    id("nowinandroid.android.library")
    id("nowinandroid.android.library.jacoco")
    kotlin("kapt")
    id("nowinandroid.spotless")
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


//    implementation(libs.kotlinx.coroutines.android)
//    implementation(libs.hilt.android)
    implementation("androidx.compose.material:material:1.1.1")
//    kapt(libs.hilt.compiler)
//
//    implementation(libs.timber)
//
//    implementation(libs.androidx.lifecycle.compiler)
//    implementation(libs.androidx.lifecycle.extensions)
//    implementation(libs.androidx.lifecycle.runtime)
//    implementation(libs.androidx.lifecycle.runtimeKtx)
//
//    testImplementation(project(":core-testing"))



    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.lifecycle.viewModelCompose)
    implementation(libs.androidx.lifecycle.runtimeKtx)
    implementation(libs.hilt.android)



}