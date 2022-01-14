package org.wit.localevents.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
data class EventModel(
    var id: Long = 0,
    var name: String = "",
    var description: String = "",
    var costs: Int = 0,
    var date: LocalDateTime = LocalDateTime.of(0, 1, 1, 1, 1, 1),
    var organizer: String = "",
    var location: Location = Location(),
    var image: Uri = Uri.EMPTY,
    var userid: Long = 0
) : Parcelable
// evtl. category

@Parcelize
data class Location(
    var lat: Double = 0.0,
    var lng: Double = 0.0,
    var zoom: Float = 0f
) : Parcelable

