package org.wit.localevents.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(var id: Long = 0,
                var fbId:String ="",
                var username: String = "",
                var password: String = "",
                var email: String = "",
                var darkmodeOn: Boolean= false): Parcelable
