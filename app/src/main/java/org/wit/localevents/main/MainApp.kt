package org.wit.localevents.main


import android.app.Application
import org.wit.localevents.models.*
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

    lateinit var events : EventJSONStore
    lateinit var users : UserJSONStore
    var currentUser = User()


    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        events = EventJSONStore(applicationContext)
        users = UserJSONStore(applicationContext)
        i("Local Events started")
    }
}