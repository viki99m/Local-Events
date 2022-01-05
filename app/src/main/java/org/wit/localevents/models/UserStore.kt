package org.wit.localevents.models

interface UserStore {
    fun findAll(): List<User>
    fun create(user: User)
    fun updateUsername(user: User)
    fun updatePassword(user:User)
    fun delete(user: User)
    fun checkData(user: User):Boolean
}