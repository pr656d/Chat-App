package com.androidev.learn.smack.Services

import android.content.Context
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.androidev.learn.smack.Model.Channel
import com.androidev.learn.smack.Utilities.URL_GET_CHANNELS
import org.json.JSONException

object MessageService {

    val channels = ArrayList<Channel>()

    fun getChannels(context: Context, complete: (Boolean) -> Unit) {

        val channelsRequest = object : JsonArrayRequest(Method.GET, URL_GET_CHANNELS, null, Response.Listener { response ->

            try {
                for (x in 0 until response.length()) {
                    val channel = response.getJSONObject(x)
//                    val name = channel.getString("name")
//                    val chanDesc = channel.getString("description")
//                    val chanid = channel.getString("_id")
//                    val newChannel = Channel(name, chanDesc, chanid)
//                    short form of above 4 lines
                    val newChannel = Channel(
                            channel.getString("name"),
                            channel.getString("description"),
                            channel.getString("_id")
                            )
                    this.channels.add(newChannel)
                }
                complete(true)
            } catch (e: JSONException) {
                Log.d("JSON:", "EXC: ${e.message}")
            }
        }, Response.ErrorListener { error ->
            Log.d("MessageService:", "couldn't retrieve channels")
            complete(false)
        }) {

            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Authorization", "Bearer ${AuthService.authToken}")
                return headers
            }
        }
        Volley.newRequestQueue(context).add(channelsRequest)
    }
}