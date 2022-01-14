package org.wit.localevents.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.navigation.NavigationView
import com.squareup.picasso.Picasso
import org.wit.localevents.R
import org.wit.localevents.databinding.ActivityEventMapsBinding
import org.wit.localevents.main.MainApp
import org.wit.localevents.models.EventModel
import timber.log.Timber
import java.text.DecimalFormat


class EventMapsActivity : AppCompatActivity(),GoogleMap.OnMarkerClickListener {
    private lateinit var binding: ActivityEventMapsBinding
    lateinit var map: GoogleMap
    lateinit var app: MainApp
    lateinit var toogle: ActionBarDrawerToggle
    lateinit var currentEvent: EventModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = application as MainApp
        binding = ActivityEventMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //binding.toolbar.title = title
        setSupportActionBar(binding.maptoolbar)
        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync {
            map = it
            configureMap()
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
                    Timber.i("klick")
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
                R.id.item_map->{
                    val launcherIntent = Intent(this,EventMapsActivity::class.java)
                    startActivity(launcherIntent)
                }
                R.id.item_old_events->{
                    val launcherIntent = Intent(this, EventListActivity::class.java)
                    launcherIntent.putExtra("old_events", true)
                    startActivity(launcherIntent)
                }
            }
            true
        }
    }

    fun configureMap() {
        map.setOnMarkerClickListener(this)
        map.uiSettings.setZoomControlsEnabled(true)
        app.events.findcurrentEvents().forEach { it ->
            val loc = LatLng(it.location.lat, it.location.lng)
            val options = MarkerOptions().title(it.name).position(loc)
            currentEvent = it
            map.addMarker(options).tag = it.id
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, it.location.zoom))
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (toogle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)

    }


    override fun onMarkerClick(marker: Marker): Boolean {
        val format = DecimalFormat("00")
        currentEvent= app.events.findbyId(marker.tag as Long)!!
        binding.currentName.text = marker.title
        binding.currentDescription.text = currentEvent.description
        binding.currentCosts.text=currentEvent.costs.toString() + "€"
        binding.currentDate.text= "${currentEvent.date.year} - ${currentEvent.date.month} - ${currentEvent.date.dayOfMonth}, ${format.format(currentEvent.date.hour)}:${format.format(currentEvent.date.minute)}"
        Picasso.get().load(currentEvent.image).resize(200,200).into(binding.currentImage)
        return false
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }
}
