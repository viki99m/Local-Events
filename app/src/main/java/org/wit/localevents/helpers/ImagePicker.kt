package org.wit.localevents.helpers

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import org.wit.localevents.R


fun showImagePicker(intentLauncher : ActivityResultLauncher<Intent>) {
    var chooseFile = Intent(Intent.ACTION_OPEN_DOCUMENT)
    chooseFile.type = "image/*"
    chooseFile = Intent.createChooser(chooseFile, R.string.button_event_image.toString())
    intentLauncher.launch(chooseFile)
}