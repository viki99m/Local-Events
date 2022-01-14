package org.wit.localevents.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toolbar
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import org.wit.localevents.R
import org.wit.localevents.adapter.EventListener
import org.wit.localevents.databinding.ActivityEventListBinding
import org.wit.localevents.main.MainApp
import org.wit.localevents.models.EventModel
import java.util.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView
import org.wit.localevents.adapter.EventAdapter
import org.wit.localevents.models.Location
import timber.log.Timber.i


class EventListActivity : AppCompatActivity(), EventListener {

    lateinit var app: MainApp
    private lateinit var binding: ActivityEventListBinding
    private lateinit var refreshIntentLauncher: ActivityResultLauncher<Intent>
    lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    private var mymenu: Int = R.menu.menu_my_events
    lateinit var toogle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        app = application as MainApp


        setSupportActionBar(findViewById(R.id.topAppBar))
        var toolbar = findViewById<MaterialToolbar>(R.id.topAppBar)
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager

        if (intent.hasExtra("event_overview")) {
           toolbar.title = "Home"
            mymenu = R.menu.menu_main
            loadEvents()
        }
        if (intent.hasExtra("my_events")) {
            toolbar.title = "My Events"
            mymenu = R.menu.menu_my_events
            loadUserEvent()
        }

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
        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.item_profile-> {
                    val launcherIntent = Intent(this, ProfileActivity::class.java)
                    startActivity(launcherIntent)
                    i("klick")
                }
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
        binding

        registerRefreshCallback()
        registerMapCallback()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(mymenu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (toogle.onOptionsItemSelected(item)) {
            return true
        }
        when(item.itemId){
            R.id.item_add -> {
                val launcherIntent = Intent(this, EventActivity::class.java)
                refreshIntentLauncher.launch(launcherIntent)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onEventClick(event: EventModel) {
        val launcherIntent = Intent(this, EventActivity::class.java)
        launcherIntent.putExtra("event_edit", event)
        refreshIntentLauncher.launch(launcherIntent)
    }

    override fun onButtonLocationClick(location: Location) {
        val launcherIntent = Intent(this,MapActivity::class.java)
            .putExtra("location", location)
        mapIntentLauncher.launch(launcherIntent)
    }

    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {
                loadEvents() }
    }

   private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { loadEvents() }
    }

    private fun loadEvents() {
        showEvents(app.events.findAll())

    }
    private fun loadUserEvent(){
        showEvents(app.events.findAllwithUser(app.currentUser))
    }

    fun showEvents (events: List<EventModel>) {
        binding.recyclerView.adapter = EventAdapter(events, this)
        binding.recyclerView.adapter?.notifyDataSetChanged()
        i("${binding.recyclerView.adapter}")
    }
}