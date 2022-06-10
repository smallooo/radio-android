plugins {
    id("nowinandroid.android.library")
    id("nowinandroid.android.library.jacoco")
    kotlin("kapt")
    id("kotlinx-serialization")
    id("dagger.hilt.android.plugin")
    id("nowinandroid.spotless")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

dependencies {
    implementation(project(":core-common"))
    implementation(project(":core-model"))
    implementation(project(":core-compose"))
    implementation(project(":core-playback"))

    testImplementation(project(":core-testing"))

    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.datetime)
    implementation(libs.okhttp.logging)
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.kotlin.serialization)
    implementation(libs.hilt.android)
    implementation(libs.hilt.compiler)
    kapt(libs.hilt.compiler)
    implementation(libs.retrofit.converter)
    implementation(libs.google.gson)

    implementation(libs.androidx.hilt.navigation.compose)


//    implementation(libs.hilt.compiler)
//    implementation(libs.hilt.android)
//    implementation(libs.hilt.ext.compiler)
//    implementation(libs.hilt.ext.work)


}