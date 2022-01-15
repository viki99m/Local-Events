package org.wit.localevents.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import org.wit.localevents.databinding.CardEventBinding
import org.wit.localevents.models.EventModel
import org.wit.localevents.models.Location
import timber.log.Timber
import java.time.format.DateTimeFormatter

interface EventListener {
    fun onEventClick(event: EventModel)
    fun onButtonLocationClick(location: Location)
}

class EventAdapter constructor(
    private var events: List<EventModel>,
    private val listener: EventListener
) :
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

    class MainHolder(private val binding: CardEventBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(event: EventModel, listener: EventListener) {
            val location = event.location
            val dateformat = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm")
            binding.eventName.text = event.name
            binding.eventDescription.text = event.description
            binding.showCosts.text = event.costs.toString() + "€"
            binding.showDate.text = dateformat.format(event.date)
            Timber.i("${event.image}")
            Picasso.get().load(event.image).resize(200, 200).into(binding.imageIcon)
            binding.root.setOnClickListener { listener.onEventClick(event) }
            binding.showLocation.setOnClickListener { listener.onButtonLocationClick(location) }
        }
    }
}