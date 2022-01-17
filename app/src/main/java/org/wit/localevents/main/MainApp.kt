package org.wit.localevents.main


import android.app.Application
import com.google.firebase.database.*
import org.wit.localevents.models.*
import timber.log.Timber
import timber.log.Timber.i
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.*

class MainApp : Application() {

    lateinit var events: EventJSONStore
    lateinit var users: UserFireStore
    lateinit var database: FirebaseDatabase
    lateinit var reference: DatabaseReference
    var currentUser = User()


    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        database = FirebaseDatabase.getInstance()
        reference = database.getReference("local-events-337415-default-rtdb")
        events = EventJSONStore(applicationContext)
        users = UserFireStore(applicationContext, reference)

        fetchdata()

        i("Local Events started")
    }

    fun fetchdata() {
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                users.users.clear()
                for (data in snapshot.child("users").children) {
                    var user = data.getValue(User::class.java)
                    users.users.add(user as User)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                i("cancel%s", error.toString())
            }

        })
    }
}