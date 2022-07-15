import org.jetbrains.dokka.utilities.cast

plugins {
    id("nowinandroid.android.library")
    id("nowinandroid.android.library.jacoco")
    kotlin("kapt")
    id("kotlin-parcelize")
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
    implementation(project(":core-model"))
    implementation(project(":common-compose"))
    implementation(project(":core-ui"))
    implementation(project(":core-data"))
    implementation(project(":core-database"))
    implementation(project(":core-navigation"))
    testImplementation(project(":core-testing"))

    implementation(libs.hilt.android)
    implementation(libs.hilt.compiler)
    kapt(libs.hilt.compiler)

    implementation(libs.androidx.hilt.navigation.compose)

    implementation(libs.androidx.compose.material.iconsExtended)

    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.material.material)
    implementation(libs.androidx.lifecycle.viewmodelKtx)
    implementation(libs.androidx.constraint.layout)

    implementation(libs.androidx.media)

    implementation(libs.androidx.palette)

   // implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.serialization.json)

//    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.21")
//    implementation("org.jetbrains.kotlin:kotlin-serialization:1.6.21")

    implementation(libs.accompanist.pager)
    implementation(libs.accompanist.insets)

    implementation(libs.androidx.compose.ui.util)

}