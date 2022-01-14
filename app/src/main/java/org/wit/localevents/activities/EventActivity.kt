package org.wit.localevents.activities

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import org.wit.localevents.R
import org.wit.localevents.databinding.ActivityEventBinding
import org.wit.localevents.helpers.showImagePicker
import org.wit.localevents.main.MainApp
import org.wit.localevents.models.EventModel
import org.wit.localevents.models.Location

import timber.log.Timber.i
import java.text.DecimalFormat
import java.time.LocalDateTime
import java.util.*

class EventActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener {
    private lateinit var binding: ActivityEventBinding
    var event = EventModel()
    lateinit var app: MainApp
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    var location = Location(49.01992541291023, 12.098539934552333, 15f)
    var edit = false

    var costs = 0
    var dayofMonth= 0
    var month = 0
    var year= 0
    var minute =0
    var hour=0

    var saveDayofMonth= 1
    var saveMonth = 1
    var saveYear= 0
    var saveMinute =0
    var saveHour=0

    val format = DecimalFormat("00")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventBinding.inflate(layoutInflater)
        val numberPicker= binding.eventCosts
        setContentView(binding.root)
        binding.toolbarAdd.title = "Local Events"
        setSupportActionBar(binding.toolbarAdd)
        app = application as MainApp
        numberPicker.minValue=0
        numberPicker.maxValue=1000
        numberPicker.setOnValueChangedListener { picker, oldVal, newVal ->
            costs= newVal
        }


        if (intent.hasExtra("event_edit")) {
            edit = true
            event = intent.extras?.getParcelable("event_edit")!!
            binding.eventName.setText(event.name)
            binding.eventDescription.setText(event.description)
            costs=event.costs
            numberPicker.value = event.costs
            binding.eventOrganizer.setText(event.organizer)
            binding.showDate.text = "${event.date.year} - ${event.date.month} - ${event.date.dayOfMonth}, ${format.format(event.date.hour)}:${format.format(event.date.minute)}"
            saveYear= event.date.year
            saveMonth= event.date.monthValue
            saveDayofMonth= event.date.dayOfMonth
            saveHour=event.date.hour
            saveMinute=event.date.minute
            location= event.location
            binding.buttonAddEvent.setText(R.string.button_save_changes)
            if (event.image != Uri.EMPTY){
                Picasso.get()
                    .load(event.image)
                    .into(binding.eventImage)
            }

        }

        binding.buttonAddEvent.setOnClickListener {
            event.name= binding.eventName.text.toString()
            event.organizer= binding.eventOrganizer.text.toString()
            event.description = binding.eventDescription.text.toString()
            event.costs= costs
            event.location= location
            event.date = LocalDateTime.of(saveYear,saveMonth,saveDayofMonth,saveHour,saveMinute)
            event.userid= app.currentUser.id
            i("${app.currentUser.id}")

            if (event.name.isEmpty()) {
                Snackbar.make(it,R.string.hint_enter_event_name, Snackbar.LENGTH_LONG)
                    .show()
            }else if (event.organizer.isEmpty()){
                Snackbar.make(it,R.string.hint_enter_organizer, Snackbar.LENGTH_LONG)
                    .show()
            }
            else if (event.date.year==0){
                Snackbar.make(it,R.string.hint_enter_date, Snackbar.LENGTH_LONG)
                    .show()
            }else if(event.date.isBefore(LocalDateTime.now())){
                Snackbar.make(it,R.string.hint_wrong_date,Snackbar.LENGTH_LONG).show()
            }
            else {
                if (edit) {
                    app.events.update(event.copy())
                } else {
                    app.events.create(event.copy())
                }
                setResult(RESULT_OK)
                finish()
            }

        }
        binding.eventChooseImage.setOnClickListener {
            i("Select image")
            showImagePicker(imageIntentLauncher)
        }
        binding.eventChooseLocation.setOnClickListener {
            val launcherIntent = Intent(this, MapActivity::class.java)
                .putExtra("location", location)
            mapIntentLauncher.launch(launcherIntent)
        }

        registerImagePickerCallback()
        registerMapCallback()
        pickDate()
    }

   override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_event, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> { finish() }
            R.id.item_delete -> {
                if (event != null) {
                    app.events.delete(event)
                    finish()
                }
            }
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

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        saveDayofMonth= dayOfMonth
        saveMonth= month+1
        saveYear=year

        getDateTimeCalandar()

        TimePickerDialog(this,this,hour,minute,true).show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        saveHour= hourOfDay
        saveMinute = minute

        binding.showDate.text = "$saveYear - $saveMonth - $saveDayofMonth, ${format.format(saveHour)} : ${format.format(saveMinute)}"

    }
    private fun getDateTimeCalandar(){
        val cal = Calendar.getInstance()
        dayofMonth = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
        hour = cal.get(Calendar.HOUR)
        minute= cal.get(Calendar.MINUTE)


    }
    private fun pickDate(){
       binding.buttonDate.setOnClickListener{
           if (intent.hasExtra("event_edit")) {
               year = event.date.year
               dayofMonth= event.date.dayOfMonth
               month = event.date.monthValue
               hour= event.date.hour
               minute= event.date.minute


           }else{
               getDateTimeCalandar()
           }


           DatePickerDialog(this,this,year,month,dayofMonth).show()
       }
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Location ${result.data.toString()}")
                            location = result.data!!.extras?.getParcelable("location")!!
                            i("Location == $location")
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }
}