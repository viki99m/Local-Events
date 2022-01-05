package org.wit.localevents.models

import android.location.Location
import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.sql.Timestamp

@Parcelize
data class EventModel(var id: Long = 0,
                      var name: String="",
                      var description: String ="",
                      var date: Timestamp,
                      var organizer: User,
                      var location: Location,
                      var image: Uri = Uri.EMPTY,
                      var costs: Float ):Parcelable
// evtl. category


