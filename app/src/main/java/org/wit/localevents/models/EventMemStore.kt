package org.wit.localevents.models


import timber.log.Timber.i

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

class EventMemStore : EventStore {

    val events = ArrayList<EventModel>()

    override fun findAll(): List<EventModel> {
        return events
    }

    override fun create(event: EventModel) {
        event.id = getId()
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
    private fun logAll() {
        events.forEach { i("$it") }
    }
}