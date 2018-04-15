package com.androidev.learn.smack.Services

import android.content.Context
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.androidev.learn.smack.Utilities.URL_CREATE_USER
import com.androidev.learn.smack.Utilities.URL_LOGIN
import com.androidev.learn.smack.Utilities.URL_REGISTER
import org.json.JSONException
import org.json.JSONObject

object AuthService {
    var isLoggedIn = false
    var userEmail = ""
    var authToken = ""

    fun registerRequest(context: Context, email: String, password: String, complete: (Boolean) -> Unit) {
        val jsonRequestBody = JSONObject().
                put("email", email).
                put("password", password).
                toString()

        val registerRequest = object : StringRequest(Method.POST, URL_REGISTER, Response.Listener { response ->
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
                return jsonRequestBody.toByteArray()
            }
        }
        Volley.newRequestQueue(context).add(registerRequest)
    }

    fun loginUser(context: Context, email: String, password: String, complete: (Boolean) -> Unit) {
        val jsonRequestBody = JSONObject().
                put("email", email).
                put("password", password).
                toString()

        val loginRequest = object : JsonObjectRequest(Method.POST, URL_LOGIN, null, Response.Listener { response ->
            // This is where we parse the json object

            try {
                userEmail = response.getString("user")
                authToken = response.getString("token")
                isLoggedIn = true
                complete(true)
            } catch (e: JSONException) {
                Log.d("loginRequest:", "EXC: ${e.message}")
                complete(false)
            }
        }, Response.ErrorListener { error ->
            // This is where we deal with errors
            Log.d("loginRequest:", "ERROR: $error")
            complete(false)
        }) {

            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return jsonRequestBody.toByteArray()
            }
        }
        Volley.newRequestQueue(context).add(loginRequest)
    }

    fun createUser(context: Context, name: String, email: String, avatarName: String, avatarColor: String, complete: (Boolean) -> Unit) {
        val jsonRequestBody = JSONObject().
                put("name", name).
                put("email", email).
                put("avatarName", avatarName).
                put("avatarColor", avatarColor).
                toString()

        val createRequest = object : JsonObjectRequest(Method.POST, URL_CREATE_USER, null, Response.Listener { response ->
            try {
                UserDataService.name = response.getString("name")
                UserDataService.email = response.getString("email")
                UserDataService.avatarName = response.getString("avatarName")
                UserDataService.avatarColor = response.getString("avatarColor")
                UserDataService.id = response.getString("_id")
                complete(true)
            } catch (e: JSONException) {
                Log.d("JSON", "EXC: ${e.message}")
                complete(false)
            }
        }, Response.ErrorListener {error ->
            Log.d("loginRequest:", "couldn't add user: $error")
            complete(false)
        }) {
            override fun getBody(): ByteArray {
                return jsonRequestBody.toByteArray()
            }

            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = hashMapOf<String, String>()
                headers.put("Authorization", "Bearer $authToken")
                return headers
            }
        }
        Volley.newRequestQueue(context).add(createRequest)
    }
}