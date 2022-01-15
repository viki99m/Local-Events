package org.wit.localevents.models


import android.content.Context
import android.net.Uri
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import org.wit.localevents.helpers.*
import timber.log.Timber
import java.lang.reflect.Type
import java.util.*

const val JSON_FILE = "users.json"
val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
    .registerTypeAdapter(Uri::class.java, UriParser())
    .create()
val listType: Type = object : TypeToken<ArrayList<User>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class UserJSONStore(private val context: Context) : UserStore {

    var users = mutableListOf<User>()

    init {
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }
    private fun serialize() {
        val jsonString = gsonBuilder.toJson(users, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        users = gsonBuilder.fromJson(jsonString, listType)
    }

    private fun logAll() {
        users.forEach { Timber.i("$it") }
    }

    override fun findAll(): MutableList<User> {
        return users
    }

    override fun create(user: User): Long {
        var founduser: User? = users.find { p -> p.username == user.username }
        if (founduser == null) {
            user.id = generateRandomId()
            users.add(user)
            logAll()
            serialize()
            return user.id
        } else {
            return 0
        }
    }

    override fun update(user: User): Boolean {
        var founduser: User? = users.find { p -> p.id == user.id }
        if (founduser != null) {
            founduser.username = user.username
            founduser.password = user.password
            serialize()
            return true
        } else {
            return false
        }
    }

    override fun delete(user: User) {
        users.remove(user)
        serialize()
    }

    override fun checkData(user: User): Long {
        var founduser: User? = users.find { p -> p.username == user.username }
        if (founduser != null) {
            if (founduser.password == user.password) {
                return founduser.id
            }
        }
        return 0
    }

    override fun usernameExists(user: User): Boolean {
        var founduser: User? = users.find { p -> p.username == user.username }
        return founduser != null
    }
}

class UriParser : JsonDeserializer<Uri>,JsonSerializer<Uri> {
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