package org.wit.localevents.main


import android.app.Application
import org.wit.localevents.models.EventMemStore
import org.wit.localevents.models.User
import org.wit.localevents.models.UserMemStore
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

    val events = EventMemStore()
    val users = UserMemStore()
    var currentUser = User()


    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        i("Local Events started")
    }
}