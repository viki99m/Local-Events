package org.wit.localevents.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.wit.localevents.databinding.ActivityLoginBinding
import timber.log.Timber


class LoginActivity : AppCompatActivity(){
    private lateinit var binding: ActivityLoginBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant(Timber.DebugTree())
        Timber.i("Login Activity starts")
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}