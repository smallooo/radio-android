import org.jetbrains.kotlin.kapt3.base.Kapt.kapt

/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
plugins {
    id("nowinandroid.android.library")
    id("nowinandroid.android.library.compose")
    id("nowinandroid.android.library.jacoco")
    id("nowinandroid.spotless")
    kotlin("kapt")
    id("kotlin-parcelize")
    id("kotlinx-serialization")
}

dependencies {
    implementation(project(":core-common"))
    implementation(project(":common-compose"))
    implementation(project(":core-model"))
    implementation(project(":core-playback"))
    implementation(project(":core-datastore"))
    implementation(project(":i18n"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.coil.kt)
    implementation(libs.coil.kt.compose)
    implementation(libs.kotlinx.datetime)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    implementation(libs.coil.kt)
    implementation(libs.coil.kt.compose)
    implementation(libs.coil.kt.compose.base)
    implementation(libs.accompanist.placeholder)

    implementation(libs.androidx.lifecycle.runtimeKtx)


    // TODO : Remove these dependency once we upgrade to Android Studio Dolphin b/228889042
    // These dependencies are currently necessary to render Compose previews
    debugImplementation(libs.androidx.customview.poolingcontainer)
    debugImplementation(libs.androidx.lifecycle.viewModelCompose)
    debugImplementation(libs.androidx.savedstate.ktx)

    api(libs.androidx.compose.foundation)
    api(libs.androidx.compose.foundation.layout)
    api(libs.androidx.compose.material.iconsExtended)
    api(libs.androidx.compose.material.material)
    api(libs.androidx.compose.material3)
    debugApi(libs.androidx.compose.ui.tooling)
    api(libs.androidx.compose.ui.tooling.preview)
    api(libs.androidx.compose.ui.util)
    api(libs.androidx.compose.runtime)
    api(libs.androidx.compose.runtime.livedata)




    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3")

    implementation(libs.accompanist.systemuicontroller)

    implementation(libs.androidx.palette)

   // implementation(libs.lottie)
    implementation(libs.lottie.compose)
}