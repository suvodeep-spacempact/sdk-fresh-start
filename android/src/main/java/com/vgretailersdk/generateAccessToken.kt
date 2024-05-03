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
import com.vgretailersdk.AuthenticationError
import com.android.volley.toolbox.RequestFuture

class GenerateAccessToken(refreshtoken: String, reactContext: ReactApplicationContext){
    var refreshtoken: String = refreshtoken
    var reactContext: ReactApplicationContext = reactContext
    // fun refreshAccessToken(callback: (String?, AuthenticationError?) -> Unit){
    //     val context = reactContext
    //     val queue = Volley.newRequestQueue(context)
    //     val stringRequestone = object : StringRequest(
    //         Method.POST, SDKConfig.baseurl+"/user/refreshAccessToken",
    //         Response.Listener { response ->
    //             val dataType = response::class.java.simpleName
    //             val jsonResponse = JSONObject(response)
    //             SDKConfig.refreshtoken = jsonResponse.getString("refreshToken")
    //             SDKConfig.accesstoken = jsonResponse.getString("accessToken")
    //             Log.d("car",js)
    //             callback(SDKConfig.accesstoken, null)
    //         },
    //     Response.ErrorListener { error ->
    //         Log.e("car", "Error occurred inside generateaccess token file:", error)
    //         callback(null, AuthenticationError("Access Token Error"))
    //     }) {
    //         override fun getBody(): ByteArray {
    //             //val jsonBody = Gson().toJson(requestBodyone)
    //             val jsonBody2 = JsonObject().apply{
    //                 addProperty("refreshToken",SDKConfig.refreshtoken)
    //             }
    //             return jsonBody2.toString().toByteArray()
    //         }
    //         override fun getBodyContentType(): String {
    //             return "application/json"
    //         }
    //     }
    //     stringRequestone.retryPolicy = DefaultRetryPolicy(
    //     50000,
    //     DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
    //     DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
    // )
    // queue.add(stringRequestone)
    // }
    fun refreshAccessToken(){
        val context = reactContext
        val queue = Volley.newRequestQueue(context)
        val future = RequestFuture.newFuture<String>()
        val stringRequestone = object : StringRequest(
            Method.POST, 
            SDKConfig.baseurl+"/user/refreshAccessToken",
            Response.Listener { response ->
                val dataType = response::class.java.simpleName
                val jsonResponse = JSONObject(response)
                SDKConfig.refreshtoken = jsonResponse.getString("refreshToken")
                SDKConfig.accesstoken = jsonResponse.getString("accessToken")
                Log.d("car","inside response of refreshaccesstoken")
                //callback(SDKConfig.accesstoken, null)
                future.onResponse(response)
            },
        Response.ErrorListener { error ->
            // Log.e("car", "Error occurred inside generateaccess token file:", error)
            // callback(null, AuthenticationError("Access Token Error"))
            Log.d("car","inside error listener of refreshaccess token")
            //throw AuthenticationError("Access Token Error");
            future.onErrorResponse(error)
        }) {
            override fun getBody(): ByteArray {
                //val jsonBody = Gson().toJson(requestBodyone)
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
        val response = future.get() // This will block until the request completes
        // Handle the response if needed
        //return SDKConfig.accesstoken
    } catch (e: Exception) {
        // Handle exception
        Log.e("car", "Error occurred during refreshAccessToken try catch", e)
        //return null
    }
    }
}