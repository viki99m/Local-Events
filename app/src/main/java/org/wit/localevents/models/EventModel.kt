package org.wit.localevents.models

import android.location.Location
import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.sql.Timestamp
import java.time.LocalDateTime

@Parcelize
data class EventModel(
    var id: Long = 0,
    var name: String = "",
    var description: String = "",
    var costs: Int= 0,
    var date: LocalDateTime= LocalDateTime.now(),
    var organizer: String = "",
    var location: Location = Location(""),
    var image: Uri = Uri.EMPTY
):Parcelable
// evtl. category


