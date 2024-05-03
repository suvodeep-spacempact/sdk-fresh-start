package com.vgretailersdk
import android.util.Log
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.android.volley.toolbox.Volley
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.google.gson.Gson
import org.json.JSONObject
import com.google.gson.JsonObject
import com.android.volley.VolleyError
import com.vgretailersdk.RegenerateAccessTokenError
import com.android.volley.toolbox.RequestFuture
import com.facebook.react.bridge.Promise

class GenerateAccessToken(refreshtoken: String, reactContext: ReactApplicationContext){
    var refreshtoken: String = refreshtoken
    var reactContext: ReactApplicationContext = reactContext
    fun refreshAccessToken(){
        Log.d("cat","inside refresh token function")
        val context = reactContext
        val queue = Volley.newRequestQueue(context)
        val future = RequestFuture.newFuture<String>()
        val stringRequestone = object : StringRequest(
            Method.POST, 
            SDKConfig.baseurl+"/user/refreshAccessToken",
            Response.Listener { response ->
                Log.d("cat","inside response block")
                val dataType = response::class.java.simpleName
                val jsonResponse = JSONObject(response)
                SDKConfig.refreshtoken = jsonResponse.getString("refreshToken")
                SDKConfig.accesstoken = jsonResponse.getString("accessToken")
                future.onResponse(response)
            },
        Response.ErrorListener { error ->
            Log.d("cat","inside error block")
            val gson = Gson().toJson(error.networkResponse)
            future.onErrorResponse(error)
        }) {
            override fun getBody(): ByteArray {
                val jsonBody2 = JsonObject().apply{
                    addProperty("refreshToken",SDKConfig.refreshtoken)
                }
                return jsonBody2.toString().toByteArray()
            }
            override fun getBodyContentType(): String {
                return "application/json"
            }
        }
        stringRequestone.retryPolicy = DefaultRetryPolicy(
        50000,
        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
    )
    queue.add(stringRequestone)
    try {
        Log.d("cat","inside future block response")
        val response = future.get()
        Log.d("cat","response inside future block response is $response")
    } catch (e: Exception) {
        Log.d("cat","inside catch block of future")
        throw RegenerateAccessTokenError("Error in generate Access token function")
    }
    }
}