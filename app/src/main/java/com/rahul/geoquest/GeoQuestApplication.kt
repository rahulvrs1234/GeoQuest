package com.rahul.geoquest

import android.app.Application
import com.rahul.geoquest.di.appModule
import com.rahul.geoquest.util.NotificationHelper
import com.rahul.geoquest.worker.WorkScheduler
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class GeoQuestApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@GeoQuestApplication)
            modules(appModule)
        }

        NotificationHelper.createChannel(this)
        WorkScheduler(this).scheduleCountryRefresh()
    }
}

