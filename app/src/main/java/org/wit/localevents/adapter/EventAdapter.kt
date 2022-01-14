package org.wit.localevents.adapter


import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import org.wit.localevents.activities.EventListActivity
import org.wit.localevents.activities.MapActivity
import org.wit.localevents.databinding.CardEventBinding
import org.wit.localevents.models.EventModel
import org.wit.localevents.models.Location
import java.text.DateFormat
import java.time.format.DateTimeFormatter

interface EventListener {
    fun onEventClick(event: EventModel)
    fun onButtonLocationClick(location: Location)
}
class EventAdapter constructor(private var events: List<EventModel>,
                                   private val listener: EventListener) :
    RecyclerView.Adapter<EventAdapter.MainHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardEventBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val event = events[holder.adapterPosition]
        holder.bind(event, listener)
    }

    override fun getItemCount(): Int = events.size

    class MainHolder(private val binding : CardEventBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(event: EventModel, listener: EventListener) {

            val dateformat = DateTimeFormatter.ofPattern("dd.MM.YYYY, HH:mm")
            binding.eventName.text = event.name
            binding.eventDescription.text = event.description
            binding.showCosts.text = event.costs.toString() + "€"
            binding.showDate.text= dateformat.format(event.date)
            Picasso.get().load(event.image).resize(200,200).into(binding.imageIcon)
            val location = event.location
            binding.root.setOnClickListener { listener.onEventClick(event) }
            binding.showLocation.setOnClickListener { listener.onButtonLocationClick(location) }
        }
    }
}