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

    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.lifecycle.viewModelCompose)
    implementation(libs.androidx.lifecycle.runtimeKtx)
    implementation(libs.hilt.android)

}