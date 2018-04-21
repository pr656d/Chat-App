package com.androidev.learn.smack.Services

import android.graphics.Color
import com.androidev.learn.smack.Controller.App
import java.util.*

object UserDataService {

    var id = ""
    var email = ""
    var name = ""
    var avatarName = ""
    var avatarColor = ""

    fun logout() {
        id = ""
        email = ""
        name = ""
        avatarName = ""
        avatarColor = ""
        App.prefs.authToken = ""
        App.prefs.userEmail = ""
        App.prefs.isLoggedIn = false
        MessageService.clearMessages()
        MessageService.clearChannels()
    }

    fun returnAvatarColor(components: String): Int {
//        [0.8, 0.09019607843137255, 0.596078431372549, 1]  prototype of values we are slicing

        val strippedColor = components
                .replace("[","")
                .replace("]", "")
                .replace(",","")

//        0.8 0.09019607843137255 0.596078431372549 1    after this operation it looks like this

        var r = 0
        var g = 0
        var b = 0

        val scanner = Scanner(strippedColor)
        if (scanner.hasNext()) {
            r = (scanner.nextDouble() * 255).toInt()
            g = (scanner.nextDouble() * 255).toInt()
            b = (scanner.nextDouble() * 255).toInt()
        }

//        now r,g,b have values respectively

        return Color.rgb(r,g,b)
    }

}