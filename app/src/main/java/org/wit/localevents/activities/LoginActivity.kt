package org.wit.localevents.activities

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import org.wit.localevents.R
import org.wit.localevents.databinding.ActivityLoginBinding
import org.wit.localevents.main.MainApp
import org.wit.localevents.models.User
import timber.log.Timber.i


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    lateinit var app: MainApp
    private lateinit var mAuth :FirebaseAuth
    private lateinit var googleSignInClient :GoogleSignInClient
    var user = User()
    companion object{
        const val RC_SIGN_IN = 120
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        i("Login Activity starts")
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        app = application as MainApp
        binding.toolbarLogin.title = title
        setSupportActionBar(binding.toolbarLogin)
        mAuth = FirebaseAuth.getInstance()
        val authUser = mAuth.currentUser

        // if the user already signed in with Google
        // wait two seconds to make sure events has the data from Firebase
        val handler = Handler()
        handler.postDelayed({
            if(authUser!= null ){
                i("Login successful")
                var currentuser : User? = app.users.findUserbyMail(authUser.email!!)
                app.currentUser = currentuser?.copy()!!
                //set darkmode
                if (app.currentUser.darkmodeOn) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
                // skip Login Screen
                val launcherIntent = Intent(this, EventListActivity::class.java)
                launcherIntent.putExtra("event_overview", true)
                startActivity(launcherIntent)
            }
        }, 2000)


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)


        binding.btnSignin.setOnClickListener {
            user.username = binding.username.editText?.text.toString()
            user.password = binding.userPassword.editText?.text.toString()
            user.email = binding.email.editText?.text.toString()
            if (user.username.isEmpty()) {
                Snackbar.make(it, R.string.hint_enter_username, Snackbar.LENGTH_LONG)
                    .show()
            } else if (user.password.isEmpty()) {
                Snackbar.make(it, R.string.hint_enter_password, Snackbar.LENGTH_LONG).show()
            }else if (user.email.isEmpty()){
                Snackbar.make(it, R.string.hint_enter_email, Snackbar.LENGTH_LONG).show()
            }
            else {
                val mailcheck = app.users.mailExists(user.email)
                if (mailcheck){
                    Snackbar.make(it, R.string.hint_email_exsits, Snackbar.LENGTH_LONG).show()
                }
                else {
                    app.users.create(user.copy())
                    Snackbar.make(it, R.string.hint_new_user, Snackbar.LENGTH_LONG).show()
                }

            }
            setResult(RESULT_OK)
        }
        binding.btnLogin.setOnClickListener {
            user.username = binding.username.editText?.text.toString()
            user.password = binding.userPassword.editText?.text.toString()
            user.email = binding.email.editText?.text.toString()
            if (user.username.isEmpty()) {
                Snackbar.make(it, R.string.hint_enter_username, Snackbar.LENGTH_LONG)
                    .show()
            } else if (user.password.isEmpty()) {
                Snackbar.make(it, R.string.hint_enter_password, Snackbar.LENGTH_LONG).show()
            } else {
                val check = app.users.checkData(user.copy())
                if (check == 0L) {
                    Snackbar.make(it, R.string.hint_wrong_user_data, Snackbar.LENGTH_LONG)
                        .show()
                } else {
                    i("Login successful")
                    user = app.users.findUserbyID(check)!!
                    app.currentUser = user.copy()

                    // set Darkmode
                    if (app.currentUser.darkmodeOn) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    }
                    setResult(RESULT_OK)
                    val launcherIntent = Intent(this, EventListActivity::class.java)
                    launcherIntent.putExtra("event_overview", true)
                    startActivity(launcherIntent)
                }
            }


        }
        binding.btnLoginGoogle.setOnClickListener {
            signIn()
        }
    }
   private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val exception = task.exception
            if(task.isSuccessful){
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)!!
                    Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    // Google Sign In failed, update UI appropriately
                    Log.w(TAG, "Google sign in failed", e)
                }
            }else{
                Log.w("Google sign in failed", exception.toString())

            }

        }
    }
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    user.username= mAuth.currentUser?.displayName!!
                    user.email=mAuth.currentUser?.email!!
                    app.users.create(user.copy())
                    user = app.users.findUserbyMail(mAuth.currentUser?.email!!)!!
                    app.currentUser = user.copy()
                    if (app.currentUser.darkmodeOn) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    }
                    setResult(RESULT_OK)
                    val launcherIntent = Intent(this, EventListActivity::class.java)
                    launcherIntent.putExtra("event_overview", true)
                    startActivity(launcherIntent)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                }
            }
    }

}