package org.wit.localevents.models

import android.location.Location
import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.sql.Timestamp

@Parcelize
data class EventModel(
    var id: Long = 0,
    var name: String = "",
    var description: String = "",
    var costs: Float= 0.0F,
    var date: Timestamp = Timestamp(0),
    var organizer: User = User(),
    var location: Location = Location(""),
    var image: Uri = Uri.EMPTY
):Parcelable
// evtl. category


