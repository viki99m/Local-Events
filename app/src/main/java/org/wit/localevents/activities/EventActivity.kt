package org.wit.localevents.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import org.wit.localevents.R
import org.wit.localevents.databinding.ActivityEventBinding
import org.wit.localevents.helpers.showImagePicker
import org.wit.localevents.main.MainApp
import org.wit.localevents.models.EventModel
import timber.log.Timber
import timber.log.Timber.i

class EventActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEventBinding
    var event = EventModel()
    lateinit var app: MainApp
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var edit = false
        binding = ActivityEventBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = "Local Events"
        //setSupportActionBar(binding.toolbarAdd)
        app = application as MainApp


        if (intent.hasExtra("event_edit")) {
            edit = true
            event = intent.extras?.getParcelable("event_edit")!!
            binding.eventName.setText(event.name)
            binding.eventDescription.setText(event.description)
          //  binding.eventCosts.se(event.costs)
            binding.eventOrganizer.setText(event.organizer)
            //binding.eventDate.setDate
            //binding.eventTime.setOnTimeChangedListener(binding.eventTime)
            // Location
            binding.buttonAddEvent.setText(R.string.button_event_add)
            Picasso.get()
                .load(event.image)
                .into(binding.eventImage)
        }

        binding.buttonAddEvent.setOnClickListener() {
            event.name= binding.eventName.text.toString()
            event.organizer= binding.eventOrganizer.text.toString()
            event.description = binding.eventDescription.text.toString()
           // event.costs= binding.eventCosts.text.toString()

            if (event.name.isEmpty()) {
                Snackbar.make(it,R.string.hint_enter_event_name, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                if (edit) {
                    app.events.update(event.copy())
                } else {
                    app.events.create(event.copy())
                }
            }
            setResult(RESULT_OK)
            finish()
        }
        binding.eventChooseImage.setOnClickListener {
            i("Select image")
            showImagePicker(imageIntentLauncher)
        }
        registerImagePickerCallback()
    }

   override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_event, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> { finish() }
        }
        return super.onOptionsItemSelected(item)
    }
    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Result ${result.data!!.data}")
                            event.image = result.data!!.data!!
                            Picasso.get()
                                .load(event.image)
                                .into(binding.eventImage)
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }
}