package org.wit.localevents.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import timber.log.Timber
import timber.log.Timber.i

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant(Timber.DebugTree())
        i("Local Events started")
    }
}