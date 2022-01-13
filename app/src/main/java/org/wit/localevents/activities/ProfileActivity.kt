package org.wit.localevents.activities

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatDelegate
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import org.wit.localevents.R
import org.wit.localevents.databinding.ActivityProfileBinding
import org.wit.localevents.main.MainApp
import timber.log.Timber.i

class ProfileActivity : AppCompatActivity() {

    lateinit var toogle: ActionBarDrawerToggle
    private lateinit var binding: ActivityProfileBinding
    lateinit var app: MainApp
    var kindofButton:Int = 0 // 0 = Change Button 1= Save button


    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityProfileBinding.inflate(layoutInflater)
            setContentView(binding.root)
            setSupportActionBar(findViewById(R.id.toolbarProfile))
            app = application as MainApp

            val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
            val navView: NavigationView = findViewById(R.id.nav_view)
            toogle = ActionBarDrawerToggle(
                this,
                drawerLayout,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
            )
            drawerLayout.addDrawerListener(toogle)
            toogle.syncState()
            supportActionBar?.setDisplayHomeAsUpEnabled(true)

            setButtontoChangeUserdata()

            binding.switchDarkmode.setOnClickListener {
                if(binding.switchDarkmode.isChecked){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    app.currentUser.darkmodeOn = true

                }else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    app.currentUser.darkmodeOn = false
                }
            }

            navView.setNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.item_home -> {
                        val launcherIntent = Intent(this, EventListActivity::class.java)
                        launcherIntent.putExtra("event_overview", true)
                        startActivity(launcherIntent)
                    }
                    R.id.item_myEvents -> {
                        val launcherIntent = Intent(this, EventListActivity::class.java)
                        launcherIntent.putExtra("my_events", true)
                        startActivity(launcherIntent)
                    }
                    R.id.item_profile-> {
                        val launcherIntent = Intent(this, ProfileActivity::class.java)
                        startActivity(launcherIntent)
                    }
                    R.id.item_logout -> {
                        app.currentUser.id= 0
                        app.currentUser.username=""
                        app.currentUser.password=""
                        app.currentUser.darkmodeOn=false
                        val launcherIntent = Intent(this, LoginActivity::class.java)
                        startActivity(launcherIntent)
                    }

                }
                true
            }

            binding.buttonEditUser.setOnClickListener{
                if(kindofButton==1){
                    var user = app.currentUser
                    user.username= binding.editUsername.editText?.text.toString()
                    user.password= binding.editPassword.editText?.text.toString()
                    if (user.username.isEmpty()) {
                        Snackbar.make(it, R.string.hint_enter_username, Snackbar.LENGTH_LONG)
                            .show()
                    }else if(user.password.isEmpty()) {
                        Snackbar.make(it, R.string.hint_enter_password, Snackbar.LENGTH_LONG).show()
                    }else if(app.users.usernameExists(user.copy()) && !app.currentUser.username.equals(user.username)){
                        Snackbar.make(it, R.string.hint_username_exist,Snackbar.LENGTH_LONG).show()

                    }else {
                        app.users.update(user.copy())
                        app.currentUser.username = user.username
                        app.currentUser.password = user.password
                        binding.buttonEditUser.setText(R.string.button_edit_user)
                        setButtontoChangeUserdata()

                    }

                }else{
                    setButtontoSave()
                }

            }
        }

        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            if (toogle.onOptionsItemSelected(item)) {
                return true
            }
            return super.onOptionsItemSelected(item)
        }

        private fun setButtontoChangeUserdata(): Boolean{
            kindofButton=0
            binding.buttonEditUser.setText(R.string.button_edit_user)
            binding.editUsername.editText?.setText(app.currentUser.username)
            binding.editPassword.editText?.setText(app.currentUser.password)
            binding.editUsername.isEnabled= false
            binding.editPassword.isEnabled= false

            return true
        }
        private fun setButtontoSave():Boolean{
            kindofButton=1

            binding.buttonEditUser.setText(R.string.button_save_changes)
            binding.editUsername.isEnabled= true
            binding.editPassword.isEnabled= true

        return true
        }


}