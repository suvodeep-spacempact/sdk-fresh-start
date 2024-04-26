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

class GenerateAccessToken(refreshtoken: String, reactContext: ReactApplicationContext){
    // var refreshtoken : String = ""
    // var reactContext: ReactApplicationContext
    var refreshtoken: String = refreshtoken
    var reactContext: ReactApplicationContext = reactContext
    // constructor(refreshtoken : String,reactContext: ReactApplicationContext) {
    //     this.refreshtoken = refreshtoken
    //     this.reactContext = reactContext
    // }

    fun refreshAccessToken(callback: (String?, VolleyError?) -> Unit){
        Log.d("TAG", "inside refresh token functionnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn")
        val context = reactContext
        val queue = Volley.newRequestQueue(context)
        val stringRequestone = object : StringRequest(
            Method.POST, SDKConfig.baseurl+"/user/refreshAccessToken",
            Response.Listener { response ->
            Log.d("TAG", "generate accessssssssss tokennnnnnnnnnnnnnn Response is: $response")
            Log.d("TAG", "Line after response")
            val dataType = response::class.java.simpleName
            Log.d("TAG","DATA TYPE IS $dataType")

            val jsonResponse = JSONObject(response)
            SDKConfig.refreshtoken = jsonResponse.getString("refreshToken")
            SDKConfig.accesstoken = jsonResponse.getString("accessToken")
            Log.d("TAG","NEW REFRESH TOKEN IS :${SDKConfig.refreshtoken}")
            callback(SDKConfig.accesstoken, null)
        },
        Response.ErrorListener { error ->
            Log.e("TAG", "generate accessssssssssss tokennnnnnnnnnnnn errorrrr occurred:", error)
            callback(null, error)
        }) {
            override fun getBody(): ByteArray {
                //val jsonBody = Gson().toJson(requestBodyone)
                val jsonBody2 = JsonObject().apply{
                    addProperty("refreshToken",SDKConfig.refreshtoken)
                }
                Log.d("TAG","&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&")
                Log.d("TAG", jsonBody2.toString())
                Log.d("TAG","------------------------------------------")
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
    Log.d("TAG","COMPLETED FUNCTION")
    }
}