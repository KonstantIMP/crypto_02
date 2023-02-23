package org.kimp.crypto2

import android.app.Application
import com.google.android.material.color.DynamicColors
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class CryptoApplication: Application() {

    override fun onCreate() {
        Timber.plant(Timber.DebugTree())

        if (DynamicColors.isDynamicColorAvailable()) {
            DynamicColors.applyToActivitiesIfAvailable(this)
            Timber.tag(APP_TAG).i("Enabled dynamic colors")
        }

        super.onCreate()
    }


    companion object ApplicationDefines {
        const val APP_TAG = "CRYPTO"
    }
}
