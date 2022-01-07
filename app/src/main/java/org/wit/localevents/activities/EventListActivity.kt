package org.wit.localevents.activities

import android.content.Intent
import android.os.Bundle
import android.util.Xml
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import org.wit.localevents.R
import org.wit.localevents.adapter.EventAdapter
import org.wit.localevents.adapter.EventListener
import org.wit.localevents.databinding.ActivityEventListBinding
import org.wit.localevents.main.MainApp
import org.wit.localevents.models.EventModel
import java.util.*


class EventListActivity : AppCompatActivity(), EventListener {

    lateinit var app: MainApp
    private lateinit var binding: ActivityEventListBinding
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>
    private var mymenu: Int = R.menu.menu_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        if(intent.hasExtra("event_overview" )){
            binding.toolbar.title = "Local Event"
            mymenu= R.menu.menu_main
            loadEvents()
        }
        if(intent.hasExtra("my_events" )){
            binding.toolbar.title = "My Events"
            mymenu = R.menu.menu_my_events
            loadUserEvent()
        }
        setSupportActionBar(binding.toolbar)

        registerRefreshCallback()
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(mymenu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            /* R.id.item_add -> {
                 val launcherIntent = Intent(this, EventActivity::class.java)
                 refreshIntentLauncher.launch(launcherIntent)
             }
             R.id.item_map -> {
                 val launcherIntent = Intent(this, EventMapsActivity::class.java)
                 mapIntentLauncher.launch(launcherIntent)
             }*/
            R.id.item_overview ->{
                val launcherIntent= Intent(this,EventListActivity::class.java)
                launcherIntent.putExtra("event_overview",true)
                refreshIntentLauncher.launch(launcherIntent)
            }
            R.id.item_myEvents->{
                val launcherIntent= Intent(this,EventListActivity::class.java)
                launcherIntent.putExtra("my_events",true)
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

    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { loadEvents() }
    }

   /*private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {  }
    }

    */

    private fun loadEvents() {
        showEvents(app.events.findAll())
    }
    private fun loadUserEvent(){
        showEvents(app.events.findAllwithUser(app.currentUser))
    }

    fun showEvents (events: List<EventModel>) {
        binding.recyclerView.adapter = EventAdapter(events, this)
        binding.recyclerView.adapter?.notifyDataSetChanged()
    }
}