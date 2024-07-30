package org.lemb.tasktrack

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level
import org.lemb.tasktrack.di.initKoin

class TasksTrackApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidLogger(Level.ERROR)
            androidContext(this@TasksTrackApp)
        }
    }
}