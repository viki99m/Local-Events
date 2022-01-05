package org.wit.localevents.models

import com.google.android.material.snackbar.Snackbar
import org.wit.localevents.R
import org.wit.localevents.models.User
import org.wit.localevents.models.UserStore
import timber.log.Timber



var lastIdUser = 0L

internal fun getIdUser(): Long {
    return lastIdUser++
}
class UserMemStore : UserStore {
    
        val users = ArrayList<User>()

        override fun findAll(): List<User> {
            return users
        }

        override fun create(user: User) {
            user.id = getIdUser()
            users.add(user)
            logAll()
        }

        override fun updateUsername(user: User) {
            var founduser: User? = users.find { p -> p.id == user.id }
            if (founduser != null) {
                founduser.username = user.username
            }
        }

    override fun updatePassword(user: User) {
        var founduser: User? = users.find{p -> p.id ==user.id}
        if(founduser!=null){
            founduser.password= user.password
        }
    }

        override fun delete(user: User) {
            users.remove(user)
        }
        private fun logAll() {
            users.forEach { Timber.i("$it") }
        }

    override fun checkData(user: User): Boolean {
        var founduser: User ?= users.find{p -> p.username == user.username}
        if (founduser!= null){
            return founduser.password == user.password
        }else{
            return false
        }
    }

}
