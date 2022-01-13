package org.wit.localevents.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.snackbar.Snackbar
import org.wit.localevents.R
import org.wit.localevents.databinding.ActivityLoginBinding
import org.wit.localevents.main.MainApp
import org.wit.localevents.models.User
import timber.log.Timber
import timber.log.Timber.i


class LoginActivity : AppCompatActivity(){
    private lateinit var binding: ActivityLoginBinding
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>
    lateinit var app: MainApp
    var user = User()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        i("Login Activity starts")
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        app = application as MainApp
        binding.toolbarLogin.title = title
        setSupportActionBar(binding.toolbarLogin)


        binding.btnSignin.setOnClickListener {
            user.username = binding.username.text.toString()
            user.password= binding.userPassword.text.toString()
            if (user.username.isEmpty()) {
                Snackbar.make(it, R.string.hint_enter_username, Snackbar.LENGTH_LONG)
                    .show()
            }else if(user.password.isEmpty()) {
                Snackbar.make(it, R.string.hint_enter_password,Snackbar.LENGTH_LONG).show()
            }else {
                    var check = app.users.usernameExists(user.copy())
                    if (check){
                        Snackbar.make(it, R.string.hint_username_exist,Snackbar.LENGTH_LONG).show()
                    }else{
                        app.users.create(user.copy())
                        Snackbar.make(it, R.string.hint_new_user,Snackbar.LENGTH_LONG).show()
                    }

            }
            setResult(RESULT_OK)

        }
        binding.btnLogin.setOnClickListener {
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
                    i("Login successful")
                    app.currentUser = user.copy()
                    if(app.currentUser.darkmodeOn){
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    }
                    setResult(RESULT_OK)
                    val launcherIntent= Intent(this,EventListActivity::class.java)
                    launcherIntent.putExtra("event_overview",true)
                    startActivity(launcherIntent)
                }
            }



        }
    }

}