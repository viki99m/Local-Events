package org.wit.localevents.models

interface EventStore {
    fun findAll(): List<EventModel>
    fun create(event: EventModel)
    fun update(event: EventModel)
    fun delete(event: EventModel)
    fun findAllwithUser(user: User): List<EventModel>
    fun findbyId(id:Long):EventModel?
}