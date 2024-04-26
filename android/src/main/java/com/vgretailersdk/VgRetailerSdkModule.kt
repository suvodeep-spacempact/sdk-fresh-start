package com.vgretailersdk
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.Promise
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import android.util.Log
import android.content.Context
import com.vgretailersdk.VerifyBankDetailsRequest
import com.google.gson.Gson
import org.json.JSONObject
import com.google.gson.JsonObject
import com.android.volley.DefaultRetryPolicy
import com.vgretailersdk.CheckIfUserExistRequest
import com.vgretailersdk.RewardHistoryRequest
import com.facebook.react.bridge.ReadableArray
import com.vgretailersdk.ScannedBalancePointsRequest
import com.vgretailersdk.InitializeSDK
import com.vgretailersdk.GenerateAccessToken
import com.vgretailersdk.SDKConfig
import com.android.volley.VolleyError


class VgRetailerSdkModule(reactContext: ReactApplicationContext) :
  ReactContextBaseJavaModule(reactContext) {

  override fun getName(): String {
    return NAME
  }
  
  // Example method
  // See https://reactnative.dev/docs/native-modules-android
  @ReactMethod
  fun multiply(a: Double, b: Double, promise: Promise) {
    promise.resolve(a * b)
  }
  
  @ReactMethod
  fun sampletry(requestData: ReadableMap,promise: Promise) {
    val refreshToken = SDKConfig.refreshtoken
    val refresAccessTokenObject = GenerateAccessToken(refreshToken,reactApplicationContext)
    refresAccessTokenObject.refreshAccessToken(){ refreshedToken: String?, error: VolleyError? ->
        if (error != null) {
            Log.d("TAG","CALLBACK ERROR!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
        }else{
            val accessToken = SDKConfig.accesstoken
    Log.d("TAG","Accessssssss token is $accessToken")
    val context = reactApplicationContext
    Log.d("TAG","123456790098765432123456789098765432123456789098765432123456789098765432")
    val queue = Volley.newRequestQueue(context)
    Log.d("TAG","$requestData")
    val request = VerifyBankDetailsRequest(
        requestData.getString("bankIfsc") ?: "",
        requestData.getString("bankAccNo") ?: "",
        requestData.getString("bankAccHolderName") ?: "",
        requestData.getString("bankAccType") ?: "",
        requestData.getString("bankNameAndBranch") ?: "",
        requestData.getString("checkPhoto") ?: ""
    )
    val stringRequest = object : StringRequest(
        Method.POST, SDKConfig.baseurl+"/banks/verifyBankDetails",
        Response.Listener { response ->
            // Display the response string
            Log.d("TAG", "Response is: $response")
            promise.resolve(response)
        },
        Response.ErrorListener { error ->
            // Handle error
            promise.reject(error);
            Log.e("TAG", "Error occurred:", error)
        }) {
        // Override getBody to return the request body
        override fun getBody(): ByteArray {
            //return params.toString().toByteArray()
            val gson = Gson()
            val jsonBody = gson.toJson(request)
            return jsonBody.toByteArray()
        }

        // Override getBodyContentType to specify the content type
        override fun getBodyContentType(): String {
            return "application/json"
        }
        override fun getHeaders(): Map<String, String> {
            val headers = HashMap<String, String>()
            // Add authorization header with Bearer token
            headers["Authorization"] = "Bearer $accessToken"
            return headers
        }
    }
    stringRequest.retryPolicy = DefaultRetryPolicy(
        50000,
        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
    )

    // Add the request to the RequestQueue.
    //Log.d("TAG",stringRequest)
    queue.add(stringRequest)
    
    request.printProperties();
    //promise.resolve(a + b)
        }
    }
  }
  @ReactMethod
  fun checkIfUserExists(mobileNumber:String,promise: Promise){
    val refreshToken = SDKConfig.refreshtoken
    val accessToken = SDKConfig.accesstoken
    Log.d("TAG","Accessssssss token is $accessToken")
    val context = reactApplicationContext
    val queue = Volley.newRequestQueue(context)
    val requestBody = CheckIfUserExistRequest(mobileNumber)
    val stringRequest = object : StringRequest(
        Method.POST, SDKConfig.baseurl+"/user/verifyUserMobile",
        Response.Listener { response ->
            // Display the response string
            Log.d("TAG", "Response is: $response")
            promise.resolve(response)
        },
        Response.ErrorListener { error ->
            // Handle error
            promise.reject(error);
            Log.e("TAG", "Error occurred:", error)
        }) {
        // Override getBody to return the request body
        override fun getBody(): ByteArray {
            //return params.toString().toByteArray()
            val gson = Gson()
            val jsonBody = gson.toJson(requestBody)
            return jsonBody.toByteArray()
        }

        // Override getBodyContentType to specify the content type
        override fun getBodyContentType(): String {
            return "application/json"
        }
        // Override getHeaders to add authorization header
        override fun getHeaders(): Map<String, String> {
            val headers = HashMap<String, String>()
            // Add authorization header with Bearer token
            headers["Authorization"] = "Bearer $accessToken"
            return headers
        }
    }
    stringRequest.retryPolicy = DefaultRetryPolicy(
        50000,
        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
    )
    queue.add(stringRequest)
  }

  fun convertReadableArrayToArray(readableArray: ReadableArray?): Array<String> {
    val arrayList = mutableListOf<String>()
    readableArray?.let {
        for (i in 0 until it.size()) {
            arrayList.add(it.getString(i) ?: "")
        }
    }
    return arrayList.toTypedArray()
}
  @ReactMethod
  fun rewardPointsHistory(requestData: ReadableMap,promise: Promise){
    val refreshToken = SDKConfig.refreshtoken
    val refresAccessTokenObject = GenerateAccessToken(refreshToken,reactApplicationContext)
    refresAccessTokenObject.refreshAccessToken(){ refreshedToken: String?, error: VolleyError? ->
        if (error != null) {
            Log.d("TAG","CALLBACK ERROR!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
        }else{
            val accessToken = SDKConfig.accesstoken
    Log.d("TAG","Accessssssss token is $accessToken")
    val context = reactApplicationContext
    val queue = Volley.newRequestQueue(context)

    val modeArray: Array<String> = if (requestData.hasKey("mode")) {
        val modeReadableArray = requestData.getArray("mode")
        val modeList = mutableListOf<String>()
        for (i in 0 until (modeReadableArray?.size() ?: 0)) {
            modeList.add(modeReadableArray?.getString(i) ?: "")
        }
        modeList.toTypedArray()
    } else {
        emptyArray()
    }

    val statusArray: Array<String> = if (requestData.hasKey("status")) {
        val statusReadableArray = requestData.getArray("status")
        val statusList = mutableListOf<String>()
        for (i in 0 until (statusReadableArray?.size() ?: 0)) {
            statusList.add(statusReadableArray?.getString(i) ?: "")
        }
        statusList.toTypedArray()
    } else {
        emptyArray()
    }
    
    val requestBody = RewardHistoryRequest(
        modeArray,
        statusArray,
        requestData.getString("fromDate") ?: "",
        requestData.getString("toDate") ?: "",
        requestData.getString("userId") ?: "",   
    )
    val stringRequest = object : StringRequest(
        Method.POST, SDKConfig.baseurl+"/product/userRewardHistory",
        Response.Listener { response ->
            // Display the response string
            Log.d("TAG", "Response is: $response")
            promise.resolve(response)
        },
        Response.ErrorListener { error ->
            // Handle error
            promise.reject(error);
            Log.e("TAG", "Error occurred:", error)
        }) {
        // Override getBody to return the request body
        override fun getBody(): ByteArray {
            //return params.toString().toByteArray()
            val gson = Gson()
            val jsonBody = gson.toJson(requestBody)
            return jsonBody.toByteArray()
        }

        // Override getBodyContentType to specify the content type
        override fun getBodyContentType(): String {
            return "application/json"
        }
        override fun getHeaders(): Map<String, String> {
            val headers = HashMap<String, String>()
            // Add authorization header with Bearer token
            headers["Authorization"] = "Bearer $accessToken"
            return headers
        }
    }
    stringRequest.retryPolicy = DefaultRetryPolicy(
        50000,
        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
    )
    queue.add(stringRequest)
        }
    }
  }

  @ReactMethod
  fun ScannedBalancePoints(requestData: ReadableMap,promise: Promise){
    val refreshToken = SDKConfig.refreshtoken
    val refresAccessTokenObject = GenerateAccessToken(refreshToken,reactApplicationContext)
    refresAccessTokenObject.refreshAccessToken(){ refreshedToken: String?, error: VolleyError? ->
        if (error != null) {
            Log.d("TAG","CALLBACK ERROR!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
        }else{
            val accessToken = SDKConfig.accesstoken
    val refresAccessTokenObject = GenerateAccessToken(refreshToken,reactApplicationContext)
    Log.d("TAG","Accessssssss token is $accessToken")
    val context = reactApplicationContext
    val queue = Volley.newRequestQueue(context)
    val categoriesArray: Array<Int> = if (requestData.hasKey("categories")) {
        val modeReadableArray = requestData.getArray("categories")
        val modeList = mutableListOf<Int>()
        for (i in 0 until (modeReadableArray?.size() ?: 0)) {
            modeList.add(modeReadableArray?.getInt(i) ?: 0)
        }
        modeList.toTypedArray()
    } else {
        emptyArray()
    }
    val subCategoriesArray: Array<Int> = if (requestData.hasKey("subCategories")) {
        val modeReadableArray = requestData.getArray("subCategories")
        val modeList = mutableListOf<Int>()
        for (i in 0 until (modeReadableArray?.size() ?: 0)) {
            modeList.add(modeReadableArray?.getInt(i) ?: 0)
        }
        modeList.toTypedArray()
    } else {
        emptyArray()
    }
    val requestBody = ScannedBalancePointsRequest(
        categoriesArray,
        subCategoriesArray,
        requestData.getString("userId") ?: "",
    )
    val stringRequest = object : StringRequest(
        Method.POST, SDKConfig.baseurl+"/product/userScannedBalancePoints",
        Response.Listener { response ->
            // Display the response string
            Log.d("TAG", "Response is: $response")
            promise.resolve(response)
        },
        Response.ErrorListener { error ->
            // Handle error
            promise.reject(error);
            Log.e("TAG", "Error occurred:", error)
        }) {
        // Override getBody to return the request body
        override fun getBody(): ByteArray {
            //return params.toString().toByteArray()
            val gson = Gson()
            val jsonBody = gson.toJson(requestBody)
            return jsonBody.toByteArray()
        }

        // Override getBodyContentType to specify the content type
        override fun getBodyContentType(): String {
            return "application/json"
        }
        override fun getHeaders(): Map<String, String> {
            val headers = HashMap<String, String>()
            // Add authorization header with Bearer token
            headers["Authorization"] = "Bearer $accessToken"
            return headers
        }
    }
    stringRequest.retryPolicy = DefaultRetryPolicy(
        50000,
        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
    )
    queue.add(stringRequest)
        }
    }
  }
  @ReactMethod
  fun userScanOutPointSummary(requestData: ReadableMap,promise: Promise){
    val refreshToken = SDKConfig.refreshtoken
    val refresAccessTokenObject = GenerateAccessToken(refreshToken,reactApplicationContext)
    refresAccessTokenObject.refreshAccessToken(){ refreshedToken: String?, error: VolleyError? ->
        if (error != null) {
            Log.d("TAG","CALLBACK ERROR!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
        }else{
            val accessToken = SDKConfig.accesstoken
    Log.d("TAG","Accessssssss token is $accessToken")
    val context = reactApplicationContext
    val queue = Volley.newRequestQueue(context)
    val categoriesArray: Array<Int> = if (requestData.hasKey("categories")) {
        val modeReadableArray = requestData.getArray("categories")
        val modeList = mutableListOf<Int>()
        for (i in 0 until (modeReadableArray?.size() ?: 0)) {
            modeList.add(modeReadableArray?.getInt(i) ?: 0)
        }
        modeList.toTypedArray()
    } else {
        emptyArray()
    }
    val subCategoriesArray: Array<Int> = if (requestData.hasKey("subCategories")) {
        val modeReadableArray = requestData.getArray("subCategories")
        val modeList = mutableListOf<Int>()
        for (i in 0 until (modeReadableArray?.size() ?: 0)) {
            modeList.add(modeReadableArray?.getInt(i) ?: 0)
        }
        modeList.toTypedArray()
    } else {
        emptyArray()
    }
    val requestBody = ScannedBalancePointsRequest(
        categoriesArray,
        subCategoriesArray,
        requestData.getString("userId") ?: "",
    )
    val stringRequest = object : StringRequest(
        Method.POST, SDKConfig.baseurl+"/product/userScanOutPointSummary",
        Response.Listener { response ->
            // Display the response string
            Log.d("TAG", "Response is: $response")
            promise.resolve(response)
        },
        Response.ErrorListener { error ->
            // Handle error
            promise.reject(error);
            Log.e("TAG", "Error occurred:", error)
        }) {
        // Override getBody to return the request body
        override fun getBody(): ByteArray {
            //return params.toString().toByteArray()
            val gson = Gson()
            val jsonBody = gson.toJson(requestBody)
            return jsonBody.toByteArray()
        }

        // Override getBodyContentType to specify the content type
        override fun getBodyContentType(): String {
            return "application/json"
        }
        override fun getHeaders(): Map<String, String> {
            val headers = HashMap<String, String>()
            // Add authorization header with Bearer token
            headers["Authorization"] = "Bearer $accessToken"
            return headers
        }
    }
    stringRequest.retryPolicy = DefaultRetryPolicy(
        50000,
        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
    )
    queue.add(stringRequest)
        }
    }
  }

  @ReactMethod
  fun postData() {
    // Instantiate the RequestQueue.
    val context = reactApplicationContext
    val queue = Volley.newRequestQueue(context)
    val requestBodyone = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOjIyMzkwLCJyb2xlSWQiOiIyIiwidXNlckNvZGUiOiJWR0lMMDEyMTg5OCIsImlzQWN0aXZlIjoiMSIsIm1vYmlsZSI6Ijk4MTE1NTU3ODkiLCJkaXNwbGF5TmFtZSI6IlJldGFpbGVyIFRlc3Q0IiwiaWF0IjoxNzE0MDQxNjUwLCJleHAiOjE3MTY2MzM2NTB9.prebKsA-UU553K8s9QCKD8ui0DcQ1JGU41HD_Tvcr3g"

    val stringRequestone = object : StringRequest(
        Method.POST, "http://34.93.239.251:5000/vguard/api/user/refreshAccessToken",
        Response.Listener { response ->
            // Display the response string
            Log.d("TAG", "generate accessssssssss tokennnnnnnnnnnnnnn Response is: $response")
            // promise.resolve(response)
        },
        Response.ErrorListener { error ->
            // Handle error
            // promise.reject(error);
            Log.e("TAG", "Errorrrrrrrrrrrrrrrrrrrrrrrrrr occurred:", error)
        }) {
        // Override getBody to return the request body
        override fun getBody(): ByteArray {
            //return params.toString().toByteArray()
            //val jsonBody = Gson().toJson(requestBodyone)
            val jsonBody2 = JsonObject().apply{
                addProperty("refreshToken","eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOjIyMzkwLCJyb2xlSWQiOiIyIiwidXNlckNvZGUiOiJWR0lMMDEyMTg5OCIsImlzQWN0aXZlIjoiMSIsIm1vYmlsZSI6Ijk4MTE1NTU3ODkiLCJkaXNwbGF5TmFtZSI6IlJldGFpbGVyIFRlc3Q0IiwiaWF0IjoxNzE0MDY1ODE0LCJleHAiOjE3MTY2NTc4MTR9.upT0HsoK5hdR7xOoXlpbuEiZlP7SbTjkqyq2ZDY5Y7Q")
            }
            Log.d("TAG", jsonBody2.toString())
            return jsonBody2.toString().toByteArray()
        }

        // Override getBodyContentType to specify the content type
        override fun getBodyContentType(): String {
            return "application/json"
        }
        // override fun getHeaders(): Map<String, String> {
        //     val headers = HashMap<String, String>()
        //     // Add authorization header with Bearer token
        //     headers["Authorization"] = "Bearer $accessToken"
        //     return headers
        // }
    }
    // stringRequestone.retryPolicy = DefaultRetryPolicy(
    //     50000,
    //     DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
    //     DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
    // )
    queue.add(stringRequestone)
    }
  @ReactMethod
  fun InitializeSDK(requestData: ReadableMap,promise: Promise){
    val sdk = InitializeSDK(
        requestData.getString("baseurl") ?: "",
        requestData.getString("accesstoken") ?: "",
        requestData.getString("refreshtoken") ?: "",
    )
    promise.resolve("SDK initalized successfully")
  }
  companion object {
    const val NAME = "VgRetailerSdk"
  }
}
