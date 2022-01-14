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
        var founduser: User? = users.find { p -> p.username == user.username }
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
        var founduser: User? = users.find { p -> p.id == user.id }
        if (founduser != null) {
            founduser.username = user.username
            founduser.password = user.password
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
