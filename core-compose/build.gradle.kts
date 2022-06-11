plugins {
    id("nowinandroid.android.library")
    id("nowinandroid.android.library.jacoco")
    kotlin("kapt")
    id("nowinandroid.spotless")
}



dependencies {

    implementation(project(":core-playback"))


    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.hilt.android)
    implementation("androidx.compose.material:material:1.1.1")
    kapt(libs.hilt.compiler)

    implementation(libs.timber)

    implementation(libs.androidx.lifecycle.compiler)
    implementation(libs.androidx.lifecycle.extensions)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.lifecycle.runtimeKtx)

    testImplementation(project(":core-testing"))
}