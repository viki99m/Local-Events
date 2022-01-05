package org.wit.localevents.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import org.wit.localevents.R
import org.wit.localevents.databinding.ActivityLoginBinding
import org.wit.localevents.main.MainApp
import org.wit.localevents.models.User
import timber.log.Timber



class LoginActivity : AppCompatActivity(){
    private lateinit var binding: ActivityLoginBinding
    lateinit var app: MainApp
    var user = User()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant(Timber.DebugTree())
        Timber.i("Login Activity starts")
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        app = application as MainApp

        binding.btnSignin.setOnClickListener() {
            user.username = binding.username.text.toString()
            user.password= binding.userPassword.text.toString()
            if (user.username.isEmpty()) {
                Snackbar.make(it, R.string.hint_enter_username, Snackbar.LENGTH_LONG)
                    .show()
            }else if(user.password.isEmpty()) {
                Snackbar.make(it, R.string.hint_enter_password,Snackbar.LENGTH_LONG)
            }else {
                //if (edit) {
                //    app.events.update(event.copy())
                //} else {
                    app.users.create(user.copy())
                //}
            }
            setResult(RESULT_OK)

        }
        binding.btnLogin.setOnClickListener() {
            user.username = binding.username.text.toString()
            user.password= binding.userPassword.text.toString()
            if (user.username.isEmpty()) {
                Snackbar.make(it, R.string.hint_enter_username, Snackbar.LENGTH_LONG)
                    .show()
            }else if(user.password.isEmpty()) {
                Snackbar.make(it, R.string.hint_enter_password,Snackbar.LENGTH_LONG)
            }else {
               var check= app.users.checkData(user.copy())
                if(!check){
                    Snackbar.make(it, R.string.hint_wrong_user_data, Snackbar.LENGTH_LONG)
                        .show()
                }else{
                    // to next activity
                }
            }
            setResult(RESULT_OK)

        }

    }

}