package org.wit.localevents.models


import timber.log.Timber.i
import java.time.LocalDateTime

var lastIdEvent = 0L

internal fun getIdEvent(): Long {
    return lastIdEvent++
}

class EventMemStore : EventStore {

    val events = ArrayList<EventModel>()

    override fun findAll(): List<EventModel> {
        return events
    }

    override fun create(event: EventModel) {
        event.id = getIdEvent()
        events.add(event)
        logAll()
    }

    override fun update(event: EventModel) {
        var foundevent: EventModel? = events.find { p -> p.id == event.id }
        if (foundevent != null) {
            foundevent.name = event.name
            foundevent.description = event.description
            foundevent.organizer = event.organizer
            foundevent.costs = event.costs
            foundevent.date = event.date
            foundevent.image = event.image
            foundevent.location = event.location
            logAll()
        }
    }

    override fun delete(event: EventModel) {
        events.remove(event)
    }

    override fun findAllwithUser(user: User): List<EventModel> {
        val eventswithUser = ArrayList<EventModel>()
        events.forEach { eventModel ->
            if (eventModel.userid == user.id) {
                eventswithUser.add(eventModel)
            }
        }
        return eventswithUser
    }

    override fun findbyId(id: Long): EventModel? {
        var foundevent: EventModel? = events.find { p -> p.id == id }
        return foundevent
    }

    override fun findcurrentEvents(): List<EventModel> {
        val currentEvents = ArrayList<EventModel>()
        events.forEach { eventModel ->
            if (!eventModel.date.isBefore(LocalDateTime.now())) {
                currentEvents.add(eventModel)
            }
        }
        return currentEvents
    }

    override fun findoldEvents(): List<EventModel> {
        val oldEvents = ArrayList<EventModel>()
        events.forEach { eventModel ->
            if (eventModel.date.isBefore(LocalDateTime.now())) {
                oldEvents.add(eventModel)
            }
        }
        return oldEvents
    }


    private fun logAll() {
        events.forEach { i("$it") }
    }
}