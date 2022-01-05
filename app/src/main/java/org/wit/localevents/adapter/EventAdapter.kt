package org.wit.localevents.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.wit.localevents.databinding.CardEventBinding
import org.wit.localevents.models.EventModel

interface EventListener {
    fun onEventClick(event: EventModel)
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
            binding.eventName.text = event.name
            binding.eventDescription.text = event.description
            binding.root.setOnClickListener { listener.onEventClick(event) }
        }
    }
}