package com.androidev.learn.smack.Services

import android.content.Context
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.androidev.learn.smack.Utilities.URL_REGISTER
import org.json.JSONObject

object AuthService {
    fun registerRequest(context: Context, email: String, password: String, complete: (Boolean) -> Unit) {
        val jsonBody = JSONObject().
                put("email", email).
                put("password", password).toString()

        val registerRequest = object: StringRequest(Method.POST, URL_REGISTER, Response.Listener { response ->
            Log.d("registerRequest:", "response: $response")
            complete(true)
        }, Response.ErrorListener {error ->
            Log.d("registerRequest", "couldn't register user: $error")
            complete(false)
        }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return jsonBody.toByteArray()
            }
        }
        Volley.newRequestQueue(context).add(registerRequest)
    }
}