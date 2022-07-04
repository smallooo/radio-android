package com.dmhsh.samples.apps.nowinandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.core.view.WindowCompat
import com.dmhsh.samples.apps.nowinandroid.ui.RadioApp
import dagger.hilt.android.AndroidEntryPoint


@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

       // val mId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        //val configuration = RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList(mId)).build()
        //MobileAds.initialize(this){}
        //MobileAds.setRequestConfiguration(configuration)

//        MobileAds.initialize(
//            this
//        ) { }

        // Show the app open ad.




        setContent {
            RadioApp(
                calculateWindowSizeClass(this)
            )
        }
    }
}
