package org.wit.localevents.models

import android.content.Context
import android.net.Uri
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import org.wit.localevents.helpers.*
import timber.log.Timber.i
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

const val JSON_FILE_EVENT = "events.json"
val gesonBuilderEvent: Gson = GsonBuilder().setPrettyPrinting()
    .registerTypeAdapter(Uri::class.java, UriParserEvent()).registerTypeAdapter(LocalDateTime::class.java,DateParser())
    .create()
val listTypeEvent: Type = object : TypeToken<ArrayList<EventModel>>() {}.type

fun generateRandomEventId(): Long {
    return Random().nextLong()
}

class EventJSONStore(private val context: Context) : EventStore {

    var events = mutableListOf<EventModel>()

    init {
        if (exists(context, JSON_FILE_EVENT)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<EventModel> {
        logAll()
        return events
    }

    override fun create(event: EventModel) {
        event.id = generateRandomEventId()
        events.add(event)
        i("${event.image}")
        serialize()
    }


    override fun update(event: EventModel) {
        val foundevent: EventModel? = events.find { p -> p.id == event.id }
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
        serialize()
    }

    override fun delete(event: EventModel) {
        events.remove(event)
        serialize()
    }

    override fun findAllwithUser(user: User): MutableList<EventModel> {
        val eventswithUser = mutableListOf<EventModel>()
        events.forEach { eventModel ->
            if (eventModel.userid == user.id) {
                eventswithUser.add(eventModel)
            }
        }
        return eventswithUser
    }

    override fun findbyId(id: Long): EventModel? {
        val foundevent: EventModel? = events.find { p -> p.id == id }
        return foundevent
    }

    override fun findcurrentEvents(): MutableList<EventModel> {
        val currentEvents = mutableListOf<EventModel>()
        events.forEach { eventModel ->
            if (!eventModel.date.isBefore(LocalDateTime.now())) {
                currentEvents.add(eventModel)
            }
        }
        return currentEvents
    }

    override fun findoldEvents(): MutableList<EventModel> {
        val oldEvents = mutableListOf<EventModel>()
        events.forEach { eventModel ->
            if (eventModel.date.isBefore(LocalDateTime.now())) {
                oldEvents.add(eventModel)
            }
        }
        return oldEvents
    }

    private fun serialize() {
        val jsonString = gesonBuilderEvent.toJson(events, listTypeEvent)
        write(context, JSON_FILE_EVENT, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE_EVENT)
        events = gesonBuilderEvent.fromJson(jsonString, listTypeEvent)
    }

    private fun logAll() {
        events.forEach { i("$it") }
    }
}

class UriParserEvent : JsonDeserializer<Uri>,JsonSerializer<Uri> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Uri {
        return Uri.parse(json?.asString)
    }

    override fun serialize(
        src: Uri?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src.toString())
    }
}
class DateParser : JsonDeserializer<LocalDateTime>,JsonSerializer<LocalDateTime>{
    private val formatter: DateTimeFormatter =
        DateTimeFormatter.ofPattern("d::MMM::uuuu HH::mm::ss")

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): LocalDateTime {
        return LocalDateTime.parse(json?.asString,formatter.withLocale(Locale.ENGLISH))
    }

    override fun serialize(
        src: LocalDateTime?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(formatter.format(src))
    }


}