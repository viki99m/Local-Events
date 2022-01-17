package org.wit.localevents.models

import timber.log.Timber


var lastIdUser = 1L

internal fun getIdUser(): Long {
    return lastIdUser++
}

class UserMemStore : UserStore {

    val users = ArrayList<User>()

    override fun findAll(): List<User> {
        return users
    }

    override fun create(user: User): Long {
        val founduser: User? = users.find { p -> p.email == user.email }
        if (founduser == null) {
            user.id = getIdUser()
            users.add(user)
            logAll()
            return user.id
        } else {
            return 0
        }
    }

    override fun update(user: User): Boolean {
        val founduser: User? = users.find { p -> p.id == user.id }
        if (founduser != null) {
            founduser.username = user.username
            founduser.password = user.password
            founduser.darkmodeOn= user.darkmodeOn
            return true
        } else {
            return false
        }
    }

    override fun delete(user: User) {
        users.remove(user)
    }

    private fun logAll() {
        users.forEach { Timber.i("$it") }
    }

    override fun checkData(user: User): Long {
        val founduser: User? = users.find { p -> p.username == user.username }
        if (founduser != null) {
            if (founduser.password == user.password && founduser.email == user.email) {
                return founduser.id
            }
        }
        return 0
    }

    override fun findUserbyID(id: Long): User? {
        val founduser: User? = users.find { p -> p.id == id }
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
}
