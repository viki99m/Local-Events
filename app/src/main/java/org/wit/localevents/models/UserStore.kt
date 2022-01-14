package org.wit.localevents.models

interface UserStore {
    fun findAll(): List<User>
    fun create(user: User):Long
    fun update(user:User):Boolean
    fun delete(user: User)
    fun checkData(user: User):Long
    fun usernameExists(user:User): Boolean
}