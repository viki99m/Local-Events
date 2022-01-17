package org.wit.localevents.models

import android.content.Context
import com.google.firebase.database.*
import timber.log.Timber.i
import java.util.*


class UserFireStore(val context: Context, val reference: DatabaseReference) : UserStore {

    var users = mutableListOf<User>()

    private fun generateRandomId(): Long {
        return Random().nextLong()
    }

    override fun findAll(): List<User> {
        return users
    }

    override fun create(user: User):Long {
        val founduser: User? = users.find { p -> p.email == user.email }
        if (founduser == null) {
            user.id = generateRandomId()
            val key = reference.child("users").push().key
            key?.let {
                user.fbId = key
                reference.child("users").child(key).setValue(user)
            }
            logAll()
            return user.id
        } else {
            return 0
        }
    }

    override fun update(user: User): Boolean {
        val founduser: User? = users.find { p -> p.fbId == user.fbId }
        if (founduser != null) {
            val foundusermap = mapOf(
                // email can't update
                "darkmodeOn" to user.darkmodeOn,
                "password" to user.password,
                "username" to user.username
            )
            reference.child("users").child(founduser.fbId).updateChildren(foundusermap)
            logAll()
            return true
        } else {
            return false
        }

    }

    override fun delete(user: User) {
        reference.child("users").child(user.fbId).removeValue()
    }

    override fun checkData(user: User): Long {
        val founduser: User? = users.find { p -> p.username == user.username }
        if (founduser != null) {
            if (founduser.password == user.password&& founduser.email == user.email) {
                return founduser.id
            }
        }
        return 0
    }

    override fun findUserbyID(id: Long): User? {
        val founduser: User? = users.find { p -> p.id == id}
        return founduser
    }

    override fun mailExists(mail: String): Boolean {
        val founduser: User? = users.find { p -> p.email == mail }
        return founduser != null
    }
    override fun findUserbyMail (mail:String): User? {
        val founduser: User? = users.find { p -> p.email == mail}
        return founduser
    }

    private fun logAll() {
        i("All Users")
        users.forEach { i("$it" ) }
        i("${users.count()}")
    }

}
