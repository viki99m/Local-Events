package org.wit.localevents.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(var id: Long = 0,
                var username: String = "",
                var password: String = "",
                var darkmodeOn: Boolean= false): Parcelable
