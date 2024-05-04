package com.vgretailersdk
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.Promise
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import android.util.Log
import android.content.Context
import android.os.Handler
import com.vgretailersdk.VerifyBankDetailsRequest
import com.google.gson.Gson
import org.json.JSONObject
import com.google.gson.JsonObject
import com.android.volley.DefaultRetryPolicy
import com.vgretailersdk.CheckIfUserExistRequest
import com.vgretailersdk.RegisterWarrantyRequest
import com.vgretailersdk.Cresp
import com.vgretailersdk.SelectedProd
import com.vgretailersdk.getCategoriesListRequest
import com.vgretailersdk.RewardHistoryRequest
import com.vgretailersdk.GetUserScanHistoryRequest
import com.facebook.react.bridge.ReadableArray
import com.vgretailersdk.ScannedBalancePointsRequest
import com.vgretailersdk.getUserBasePointsRequest
import com.vgretailersdk.GetComboSlabSchemesRequest
import com.vgretailersdk.InitializeSDK
import com.vgretailersdk.GenerateAccessToken
import com.vgretailersdk.RegenerateAccessTokenError
import com.vgretailersdk.SDKConfig
import com.android.volley.VolleyError
import kotlinx.coroutines.*
import kotlinx.coroutines.android.*
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.WritableNativeMap
import java.nio.charset.StandardCharsets
import java.util.*


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
  


fun isTokenExpired(token: String): Boolean {
  val parts = token.split("\\.".toRegex()).toTypedArray()
  val decodedPayload = String(Base64.getDecoder().decode(parts[1]), StandardCharsets.UTF_8)
  val payloadJson = JSONObject(decodedPayload)
  
  val expirationTime = payloadJson.optLong("exp", 0)
  val currentTime = System.currentTimeMillis() / 1000

  return expirationTime < currentTime
}



@ReactMethod
fun verifyBankDetails(requestData: ReadableMap,promise: Promise) {
  try{
    val token = SDKConfig.accesstoken
    val refreshToken = SDKConfig.refreshtoken
    val refresAccessTokenObject = GenerateAccessToken(refreshToken,reactApplicationContext)
    if (isTokenExpired(token)) {
      refresAccessTokenObject.refreshAccessToken()
    }
    val accessToken = SDKConfig.accesstoken
    val context = reactApplicationContext
    val queue = Volley.newRequestQueue(context)
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
      promise.resolve(response)
      },
      Response.ErrorListener { error ->
        val errorCode = error.networkResponse?.statusCode
        val gson = Gson().toJson(error.networkResponse)
        if(errorCode == 403 || errorCode == 401){
          val jsonObject = JSONObject()
          jsonObject.put("message", "Session has timed out. Please re-initialize the SDK.")
          jsonObject.put("code", 440)
          val jsonString = jsonObject.toString()
          promise.reject(jsonString)
        }else if(errorCode == 440){
          Log.d("cat","Refreshing expired token")
          Log.d("cat","Old: ${SDKConfig.accesstoken}")
          Log.d("cat","New: ${SDKConfig.accesstoken}")
        }
        val jsonObject = JSONObject()
        jsonObject.put("message", "Internal Server Error.")
        jsonObject.put("code", 500)
        val jsonString = jsonObject.toString()
        promise.reject(jsonString)
      }) {
        override fun getBody(): ByteArray {
          val gson = Gson()
          val jsonBody = gson.toJson(request)
          return jsonBody.toByteArray()
        }
        override fun getBodyContentType(): String {
          return "application/json"
        }
        override fun getHeaders(): Map<String, String> {
          val headers = HashMap<String, String>()
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
    }catch (e: Exception) {
      if(e is RegenerateAccessTokenError){
        Log.d("cat","inside if 1");
        Log.d("cat","error is",e);
        val jsonObject = JSONObject()
        jsonObject.put("message", "Session has timed out. Please re-initialize the SDK.")
        jsonObject.put("code", 440)
        val jsonString = jsonObject.toString()
        promise.reject(jsonString)
      }
      Log.d("cat","outside if")
      val jsonObject = JSONObject()
      jsonObject.put("message", "Internal Server Error.")
      jsonObject.put("code", 500)
      val jsonString = jsonObject.toString()
      promise.reject(jsonString)
    }
}

@ReactMethod
fun getCategoriesList(requestData: ReadableMap,promise: Promise){
  try{
    val token = SDKConfig.accesstoken
    val refreshToken = SDKConfig.refreshtoken
    val refresAccessTokenObject = GenerateAccessToken(refreshToken,reactApplicationContext)
    if (isTokenExpired(token)) {
      refresAccessTokenObject.refreshAccessToken()
    }
    val accessToken = SDKConfig.accesstoken
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
    val requestBody = getCategoriesListRequest(categoriesArray)
    val stringRequest = object : StringRequest(
      Method.POST, SDKConfig.baseurl+"/product/getCategoriesList",
      Response.Listener { response ->
        promise.resolve(response)
      },
      Response.ErrorListener { error ->
        val errorCode = error.networkResponse?.statusCode
        val gson = Gson().toJson(error.networkResponse)
        if(errorCode == 403 || errorCode == 401){
          val jsonObject = JSONObject()
          jsonObject.put("message", "Session has timed out. Please re-initialize the SDK.")
          jsonObject.put("code", 440)
          val jsonString = jsonObject.toString()
          promise.reject(jsonString)
        }else if(errorCode == 440){
          val jsonObject = JSONObject()
          jsonObject.put("message", "Please retry the action")
          jsonObject.put("code", 440)
          val jsonString = jsonObject.toString()
          promise.reject(jsonString)
        }
        val jsonObject = JSONObject()
        jsonObject.put("message", "Internal Server Error.")
        jsonObject.put("code", 500)
        val jsonString = jsonObject.toString()
        promise.reject(jsonString)
      }) {
        override fun getBody(): ByteArray {
          val gson = Gson()
          val jsonBody = gson.toJson(requestBody)
          return jsonBody.toByteArray()
        }
        override fun getBodyContentType(): String {
          return "application/json"
        }
        override fun getHeaders(): Map<String, String> {
          val headers = HashMap<String, String>()
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
  }catch(error: Exception){
    Log.d("cat","error is  $error")
    if(error is RegenerateAccessTokenError){
      val jsonObject = JSONObject()
      jsonObject.put("message", "Session has timed out. Please re-initialize the SDK.")
      jsonObject.put("code", 440)
      val jsonString = jsonObject.toString()
      promise.reject(jsonString)
    }
    val jsonObject = JSONObject()
    jsonObject.put("message", "Internal Server Error.")
    jsonObject.put("code", 500)
    val jsonString = jsonObject.toString()
    promise.reject(jsonString)
  }
}

@ReactMethod
fun getUserBasePoints(requestData: ReadableMap,promise: Promise){
  try{
    val token = SDKConfig.accesstoken
    val refreshToken = SDKConfig.refreshtoken
    val refresAccessTokenObject = GenerateAccessToken(refreshToken,reactApplicationContext)
    if (isTokenExpired(token)) {
      refresAccessTokenObject.refreshAccessToken()
    }
    val accessToken = SDKConfig.accesstoken
    val context = reactApplicationContext
    val queue = Volley.newRequestQueue(context)
    val categoryIdsArray: Array<String> = if (requestData.hasKey("categoryIds")) {
      val modeReadableArray = requestData.getArray("categoryIds")
      val modeList = mutableListOf<String>()
      for (i in 0 until (modeReadableArray?.size() ?: 0)) {
        modeList.add(modeReadableArray?.getString(i) ?: "")
      }
      modeList.toTypedArray()
    } else {
      emptyArray()
    }

    val subCategoryIdsArray: Array<String> = if (requestData.hasKey("subCategoryIds")) {
      val statusReadableArray = requestData.getArray("subCategoryIds")
      val statusList = mutableListOf<String>()
      for (i in 0 until (statusReadableArray?.size() ?: 0)) {
        statusList.add(statusReadableArray?.getString(i) ?: "")
      }
      statusList.toTypedArray()
    } else {
      emptyArray()
    }
    val requestBody = getUserBasePointsRequest(categoryIdsArray,subCategoryIdsArray)
    val stringRequest = object : StringRequest(
      Method.POST, SDKConfig.baseurl+"/coupon/getUserBasePoints",
      Response.Listener { response ->
        promise.resolve(response)
      },
      Response.ErrorListener { error ->
        val errorCode = error.networkResponse?.statusCode
        val gson = Gson().toJson(error.networkResponse)
        if(errorCode == 403 || errorCode == 401){
          val jsonObject = JSONObject()
          jsonObject.put("message", "Session has timed out. Please re-initialize the SDK.")
          jsonObject.put("code", 440)
          val jsonString = jsonObject.toString()
          promise.reject(jsonString)
        }else if(errorCode == 440){
          val jsonObject = JSONObject()
          jsonObject.put("message", "Please retry the action")
          jsonObject.put("code", 440)
          val jsonString = jsonObject.toString()
          promise.reject(jsonString)
        }
        val jsonObject = JSONObject()
        jsonObject.put("message", "Internal Server Error.")
        jsonObject.put("code", 500)
        val jsonString = jsonObject.toString()
        promise.reject(jsonString)
      }) {
        override fun getBody(): ByteArray {
          val gson = Gson()
          val jsonBody = gson.toJson(requestBody)
          return jsonBody.toByteArray()
        }
        override fun getBodyContentType(): String {
          return "application/json"
        }
        override fun getHeaders(): Map<String, String> {
          val headers = HashMap<String, String>()
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
  }catch(error: Exception){
    Log.d("cat","error is $error")
    if(error is RegenerateAccessTokenError){
      val jsonObject = JSONObject()
      jsonObject.put("message", "Session has timed out. Please re-initialize the SDK.")
      jsonObject.put("code", 440)
      val jsonString = jsonObject.toString()
      promise.reject(jsonString)
    }
    val jsonObject = JSONObject()
    jsonObject.put("message", "Internal Server Error.")
    jsonObject.put("code", 500)
    val jsonString = jsonObject.toString()
    promise.reject(jsonString)
  }
}

@ReactMethod
fun getUserScanHistory(requestData: ReadableMap,promise: Promise){
  try{
    val token = SDKConfig.accesstoken
    val refreshToken = SDKConfig.refreshtoken
    val refresAccessTokenObject = GenerateAccessToken(refreshToken,reactApplicationContext)
    if (isTokenExpired(token)) {
      refresAccessTokenObject.refreshAccessToken()
    }
    val accessToken = SDKConfig.accesstoken
    val context = reactApplicationContext
    val queue = Volley.newRequestQueue(context)
    val statusArray: Array<String> = if (requestData.hasKey("status")) {
      val modeReadableArray = requestData.getArray("status")
      val modeList = mutableListOf<String>()
      for (i in 0 until (modeReadableArray?.size() ?: 0)) {
        modeList.add(modeReadableArray?.getString(i) ?: "")
      }
      modeList.toTypedArray()
    } else {
      emptyArray()
    }
    val requestBody = GetUserScanHistoryRequest(
      statusArray,
      requestData.getString("scanType") ?: "",
      requestData.getString("fromDate") ?: "",
      requestData.getString("couponCode") ?: "",
    )
    val stringRequest = object : StringRequest(
      Method.POST, SDKConfig.baseurl+"/coupon/getUserScanHistory",
      Response.Listener { response ->
        promise.resolve(response)
      },
      Response.ErrorListener { error ->
        val errorCode = error.networkResponse?.statusCode
        val gson = Gson().toJson(error.networkResponse)
        if(errorCode == 403 || errorCode == 401){
          val jsonObject = JSONObject()
          jsonObject.put("message", "Session has timed out. Please re-initialize the SDK.")
          jsonObject.put("code", 440)
          val jsonString = jsonObject.toString()
          promise.reject(jsonString)
        }else if(errorCode == 440){
          val jsonObject = JSONObject()
          jsonObject.put("message", "Please retry the action")
          jsonObject.put("code", 440)
          val jsonString = jsonObject.toString()
          promise.reject(jsonString)
        }
        val jsonObject = JSONObject()
        jsonObject.put("message", "Internal Server Error.")
        jsonObject.put("code", 500)
        val jsonString = jsonObject.toString()
        promise.reject(jsonString)
      }) {
        override fun getBody(): ByteArray {
          val gson = Gson()
          val jsonBody = gson.toJson(requestBody)
          return jsonBody.toByteArray()
        }
        override fun getBodyContentType(): String {
          return "application/json"
        }
        override fun getHeaders(): Map<String, String> {
          val headers = HashMap<String, String>()
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


  }catch(error: Exception){
    if(error is RegenerateAccessTokenError){
      val jsonObject = JSONObject()
      jsonObject.put("message", "Session has timed out. Please re-initialize the SDK.")
      jsonObject.put("code", 440)
      val jsonString = jsonObject.toString()
      promise.reject(jsonString)
    }
    val jsonObject = JSONObject()
    jsonObject.put("message", "Internal Server Error.")
    jsonObject.put("code", 500)
    val jsonString = jsonObject.toString()
    promise.reject(jsonString)
  }
}
  
@ReactMethod
fun rewardPointsHistory(requestData: ReadableMap,promise: Promise){
  try{
    val token = SDKConfig.accesstoken
    val refreshToken = SDKConfig.refreshtoken
    val refresAccessTokenObject = GenerateAccessToken(refreshToken,reactApplicationContext)
    if (isTokenExpired(token)) {
      refresAccessTokenObject.refreshAccessToken()
    }
    val accessToken = SDKConfig.accesstoken
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
            promise.resolve(response)
        },
        Response.ErrorListener { error ->
          val errorCode = error.networkResponse?.statusCode
          val gson = Gson().toJson(error.networkResponse)
          if(errorCode == 403 || errorCode == 401){
            val jsonObject = JSONObject()
            jsonObject.put("message", "Session has timed out. Please re-initialize the SDK.")
            jsonObject.put("code", 440)
            val jsonString = jsonObject.toString()
            promise.reject(jsonString)
          }else if(errorCode == 440){
            val jsonObject = JSONObject()
            jsonObject.put("message", "Please retry the action")
            jsonObject.put("code", 440)
            val jsonString = jsonObject.toString()
            promise.reject(jsonString)
          }
          val jsonObject = JSONObject()
          jsonObject.put("message", "Internal Server Error.")
          jsonObject.put("code", 500)
          val jsonString = jsonObject.toString()
          promise.reject(jsonString)
        }) {
        
        override fun getBody(): ByteArray {
            val gson = Gson()
            val jsonBody = gson.toJson(requestBody)
            return jsonBody.toByteArray()
        }
        override fun getBodyContentType(): String {
            return "application/json"
        }
        override fun getHeaders(): Map<String, String> {
            val headers = HashMap<String, String>()
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

  }catch(error: Exception){
    if(error is RegenerateAccessTokenError){
      val jsonObject = JSONObject()
      jsonObject.put("message", "Session has timed out. Please re-initialize the SDK.")
      jsonObject.put("code", 440)
      val jsonString = jsonObject.toString()
      promise.reject(jsonString)
    }
    val jsonObject = JSONObject()
    jsonObject.put("message", "Internal Server Error.")
    jsonObject.put("code", 500)
    val jsonString = jsonObject.toString()
    promise.reject(jsonString)
  }
}


  @ReactMethod
  fun ScannedBalancePoints(requestData: ReadableMap,promise: Promise){
    try{
      val token = SDKConfig.accesstoken
      val refreshToken = SDKConfig.refreshtoken
      val refresAccessTokenObject = GenerateAccessToken(refreshToken,reactApplicationContext)
      if (isTokenExpired(token)) {
        refresAccessTokenObject.refreshAccessToken()
      }
      val accessToken = SDKConfig.accesstoken
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
            promise.resolve(response)
        },
        Response.ErrorListener { error ->
          val errorCode = error.networkResponse?.statusCode
          val gson = Gson().toJson(error.networkResponse)
          if(errorCode == 403 || errorCode == 401){
            val jsonObject = JSONObject()
            jsonObject.put("message", "Session has timed out. Please re-initialize the SDK.")
            jsonObject.put("code", 440)
            val jsonString = jsonObject.toString()
            promise.reject(jsonString)
          }else if(errorCode == 440){
            val jsonObject = JSONObject()
            jsonObject.put("message", "Please retry the action")
            jsonObject.put("code", 440)
            val jsonString = jsonObject.toString()
            promise.reject(jsonString)
          }
          val jsonObject = JSONObject()
          jsonObject.put("message", "Internal Server Error.")
          jsonObject.put("code", 500)
          val jsonString = jsonObject.toString()
          promise.reject(jsonString)
        }) {
        override fun getBody(): ByteArray {
            val gson = Gson()
            val jsonBody = gson.toJson(requestBody)
            return jsonBody.toByteArray()
        }
        override fun getBodyContentType(): String {
            return "application/json"
        }
        override fun getHeaders(): Map<String, String> {
            val headers = HashMap<String, String>()
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

    }catch(error: Exception){
    if(error is RegenerateAccessTokenError){
      val jsonObject = JSONObject()
      jsonObject.put("message", "Session has timed out. Please re-initialize the SDK.")
      jsonObject.put("code", 440)
      val jsonString = jsonObject.toString()
      promise.reject(jsonString)
    }
    val jsonObject = JSONObject()
    jsonObject.put("message", "Internal Server Error.")
    jsonObject.put("code", 500)
    val jsonString = jsonObject.toString()
    promise.reject(jsonString)
  }
  }


  @ReactMethod
  fun userScanOutPointSummary(requestData: ReadableMap,promise: Promise){
    try{
      val token = SDKConfig.accesstoken
      val refreshToken = SDKConfig.refreshtoken
      val refresAccessTokenObject = GenerateAccessToken(refreshToken,reactApplicationContext)
      if (isTokenExpired(token)) {
        refresAccessTokenObject.refreshAccessToken()
      }
      val accessToken = SDKConfig.accesstoken
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
            promise.resolve(response)
        },
        Response.ErrorListener { error ->
          val errorCode = error.networkResponse?.statusCode
          val gson = Gson().toJson(error.networkResponse)
          if(errorCode == 403 || errorCode == 401){
            val jsonObject = JSONObject()
            jsonObject.put("message", "Session has timed out. Please re-initialize the SDK.")
            jsonObject.put("code", 440)
            val jsonString = jsonObject.toString()
            promise.reject(jsonString)
          }else if(errorCode == 440){
            val jsonObject = JSONObject()
            jsonObject.put("message", "Please retry the action")
            jsonObject.put("code", 440)
            val jsonString = jsonObject.toString()
            promise.reject(jsonString)
          }
          val jsonObject = JSONObject()
          jsonObject.put("message", "Internal Server Error.")
          jsonObject.put("code", 500)
          val jsonString = jsonObject.toString()
          promise.reject(jsonString)
        }) {
        override fun getBody(): ByteArray {
            val gson = Gson()
            val jsonBody = gson.toJson(requestBody)
            return jsonBody.toByteArray()
        }
        override fun getBodyContentType(): String {
            return "application/json"
        }
        override fun getHeaders(): Map<String, String> {
            val headers = HashMap<String, String>()
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

    }catch(error:Exception){
      if(error is RegenerateAccessTokenError){
        val jsonObject = JSONObject()
        jsonObject.put("message", "Session has timed out. Please re-initialize the SDK.")
        jsonObject.put("code", 440)
        val jsonString = jsonObject.toString()
        promise.reject(jsonString)
      }
      val jsonObject = JSONObject()
      jsonObject.put("message", "Internal Server Error.")
      jsonObject.put("code", 500)
      val jsonString = jsonObject.toString()
      promise.reject(jsonString)
    }
  }

  @ReactMethod
  fun captureCustomerDetails(mobileNo: String,promise: Promise){
    try{
      val token = SDKConfig.accesstoken
      val refreshToken = SDKConfig.refreshtoken
      val refresAccessTokenObject = GenerateAccessToken(refreshToken,reactApplicationContext)
      if (isTokenExpired(token)) {
        refresAccessTokenObject.refreshAccessToken()
      }
      val accessToken = SDKConfig.accesstoken
      val context = reactApplicationContext
      val queue = Volley.newRequestQueue(context)
      val url = SDKConfig.baseurl + "/product/getCustomerDetails"
      val queryParams = hashMapOf("mobileNo" to mobileNo)
      val fullUrl = "$url/$mobileNo"
      Log.d("cat",fullUrl)
      val stringRequest = object : StringRequest(
        Method.GET, 
        fullUrl, // Modified URL with query parameters
        Response.Listener { response ->
          promise.resolve(response)
        },
        Response.ErrorListener { error ->
          val errorCode = error.networkResponse?.statusCode
          val gson = Gson().toJson(error.networkResponse)
          if(errorCode == 403 || errorCode == 401){
            val jsonObject = JSONObject()
            jsonObject.put("message", "Session has timed out. Please re-initialize the SDK.")
            jsonObject.put("code", 440)
            val jsonString = jsonObject.toString()
            promise.reject(jsonString)
          } else if(errorCode == 440){
            val jsonObject = JSONObject()
            jsonObject.put("message", "Please retry the action")
            jsonObject.put("code", 440)
            val jsonString = jsonObject.toString()
            promise.reject(jsonString)
          }
          val jsonObject = JSONObject()
          jsonObject.put("message", "Internal Server Error.")
          jsonObject.put("code", 500)
          val jsonString = jsonObject.toString()
          promise.reject(jsonString)
        }) {
          override fun getHeaders(): Map<String, String> {
            val headers = HashMap<String, String>()
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
    }catch(error: Exception){
      Log.d("cat","error is : ",error)
      if(error is RegenerateAccessTokenError){
        val jsonObject = JSONObject()
        jsonObject.put("message", "Session has timed out. Please re-initialize the SDK.")
        jsonObject.put("code", 440)
        val jsonString = jsonObject.toString()
        promise.reject(jsonString)
      }
      val jsonObject = JSONObject()
      jsonObject.put("message", "Internal Server Error.")
      jsonObject.put("code", 500)
      val jsonString = jsonObject.toString()
      promise.reject(jsonString)      
    }
  }

  @ReactMethod
  fun registerWarranty(requestData: ReadableMap,promise: Promise){
    try{
      val token = SDKConfig.accesstoken
      val refreshToken = SDKConfig.refreshtoken
      val refresAccessTokenObject = GenerateAccessToken(refreshToken,reactApplicationContext)
      if (isTokenExpired(token)) {
        refresAccessTokenObject.refreshAccessToken()
      }
      val accessToken = SDKConfig.accesstoken
      val context = reactApplicationContext
      val queue = Volley.newRequestQueue(context)
      val requestBody = RegisterWarrantyRequest(
        requestData.getString("nameTitle") ?: "",
        requestData.getString("contactNo") ?: "",
        requestData.getString("name") ?: "",
        requestData.getString("email") ?: "",
        requestData.getString("currAdd") ?: "",
        requestData.getString("alternateNo") ?: "",
        requestData.getString("state") ?: "",
        requestData.getString("district") ?: "",
        requestData.getString("city") ?: "",
        requestData.getString("landmark") ?: "",
        requestData.getString("pinCode") ?: "",
        requestData.getString("dealerName") ?: "",
        requestData.getString("dealerAdd") ?: "",
        requestData.getString("dealerState") ?: "",
        requestData.getString("dealerDist") ?: "",
        requestData.getString("dealerCity") ?: "",
        requestData.getString("dealerPinCode") ?: "",
        requestData.getString("dealerNumber") ?: "",
        requestData.getInt("addedBy") ?: 0,
        requestData.getString("billDetails") ?: "",
        requestData.getString("warrantyPhoto") ?: "",
        requestData.getString("sellingPrice") ?: "",
        requestData.getString("emptStr") ?: "",
        Cresp(
          requestData.getString("custIdForProdInstall") ?: "",
          requestData.getString("modelForProdInstall") ?: "", 
          requestData.getInt("errorCode") ?: 0,
          requestData.getString("errorMsg") ?: "",
          requestData.getInt("statusType") ?: 0,
          requestData.getString("balance") ?: "",
          requestData.getString("currentPoints") ?: "",
          requestData.getString("couponPoints") ?: "",
          requestData.getString("promotionPoints") ?: "",
          requestData.getString("transactId") ?: "",
          requestData.getString("schemePoints") ?: "",
          requestData.getString("basePoints") ?: "",
          requestData.getString("clubPoints") ?: "",
          requestData.getString("scanDate") ?: "",
          requestData.getString("scanStatus") ?: "",
          requestData.getString("copuonCode") ?: "",
          requestData.getString("bitEligibleScratchCard") ?.toBoolean() ?: false,
          requestData.getInt("pardId") ?: 0,
          requestData.getString("partNumber") ?: "",
          requestData.getString("partName") ?: "",
          requestData.getString("couponCode") ?: "",
          requestData.getString("skuDetail") ?: "",
          requestData.getString("purchaseDate") ?: "",
          requestData.getString("categoryId") ?: "",
          requestData.getString("category") ?: "",
          requestData.getInt("anomaly") ?: 0,
          requestData.getString("warranty") ?: "",
        ),
        SelectedProd(
          requestData.getString("specs") ?: "",
          requestData.getString("pointsFormat") ?: "",
          requestData.getString("product") ?: "",
          requestData.getString("productName") ?: "",
          requestData.getString("productCategory") ?: "",
          requestData.getString("productCode") ?: "",
          requestData.getInt("points") ?: 0,
          requestData.getString("imageUrl") ?: "",
          requestData.getString("userId") ?: "",
          requestData.getString("productId") ?: "",
          requestData.getString("paytmMobileNo") ?: "",
        ),
        requestData.getString("latitude") ?: "",
        requestData.getString("longitude") ?: "",   
        requestData.getString("geolocation") ?: "",
        requestData.getString("dealerCategory") ?: "",
      )
      val stringRequest = object : StringRequest(
        Method.POST, SDKConfig.baseurl+"/product/registerWarranty",
        Response.Listener { response ->
            promise.resolve(response)
        },
        Response.ErrorListener { error ->
          val errorCode = error.networkResponse?.statusCode
          val gson = Gson().toJson(error.networkResponse)
          if(errorCode == 403 || errorCode == 401){
            val jsonObject = JSONObject()
            jsonObject.put("message", "Session has timed out. Please re-initialize the SDK.")
            jsonObject.put("code", 440)
            val jsonString = jsonObject.toString()
            promise.reject(jsonString)
          }else if(errorCode == 440){
            val jsonObject = JSONObject()
            jsonObject.put("message", "Please retry the action")
            jsonObject.put("code", 440)
            val jsonString = jsonObject.toString()
            promise.reject(jsonString)
          }
          val jsonObject = JSONObject()
          jsonObject.put("message", "Internal Server Error.")
          jsonObject.put("code", 500)
          val jsonString = jsonObject.toString()
          promise.reject(jsonString)
        }) {
        override fun getBody(): ByteArray {
            val gson = Gson()
            val jsonBody = gson.toJson(requestBody)
            return jsonBody.toByteArray()
        }
        override fun getBodyContentType(): String {
            return "application/json"
        }
        override fun getHeaders(): Map<String, String> {
            val headers = HashMap<String, String>()
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

    }catch(error: Exception){
      if(error is RegenerateAccessTokenError){
        val jsonObject = JSONObject()
        jsonObject.put("message", "Session has timed out. Please re-initialize the SDK.")
        jsonObject.put("code", 440)
        val jsonString = jsonObject.toString()
        promise.reject(jsonString)
      }
      val jsonObject = JSONObject()
      jsonObject.put("message", "Internal Server Error.")
      jsonObject.put("code", 500)
      val jsonString = jsonObject.toString()
      promise.reject(jsonString)      
    }
  }

  @ReactMethod 
  fun getEligibleProducts(categoryId : String,schemeId: String,promise: Promise){
    try{
      val token = SDKConfig.accesstoken
      val refreshToken = SDKConfig.refreshtoken
      val refresAccessTokenObject = GenerateAccessToken(refreshToken,reactApplicationContext)
      if (isTokenExpired(token)) {
        refresAccessTokenObject.refreshAccessToken()
      }
      val accessToken = SDKConfig.accesstoken
      val context = reactApplicationContext
      val queue = Volley.newRequestQueue(context)
      val requestBody = JSONObject()
      requestBody.put("categoryId", categoryId)
      requestBody.put("schemeId", schemeId)
      val stringRequest = object : StringRequest(
        Method.POST, SDKConfig.baseurl+"/schemes/getEligibleProducts",
        Response.Listener { response ->
            promise.resolve(response)
        },
        Response.ErrorListener { error ->
          val errorCode = error.networkResponse?.statusCode
          val gson = Gson().toJson(error.networkResponse)
          if(errorCode == 403 || errorCode == 401){
            val jsonObject = JSONObject()
            jsonObject.put("message", "Session has timed out. Please re-initialize the SDK.")
            jsonObject.put("code", 440)
            val jsonString = jsonObject.toString()
            promise.reject(jsonString)
          }else if(errorCode == 440){
            val jsonObject = JSONObject()
            jsonObject.put("message", "Please retry the action")
            jsonObject.put("code", 440)
            val jsonString = jsonObject.toString()
            promise.reject(jsonString)
          }
          val jsonObject = JSONObject()
          jsonObject.put("message", "Internal Server Error.")
          jsonObject.put("code", 500)
          val jsonString = jsonObject.toString()
          promise.reject(jsonString)
        }) {
        override fun getBody(): ByteArray {
            val gson = Gson()
            val jsonBody = gson.toJson(requestBody)
            return jsonBody.toByteArray()
        }
        override fun getBodyContentType(): String {
            return "application/json"
        }
        override fun getHeaders(): Map<String, String> {
            val headers = HashMap<String, String>()
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

    }catch(error:Exception){
      if(error is RegenerateAccessTokenError){
        val jsonObject = JSONObject()
        jsonObject.put("message", "Session has timed out. Please re-initialize the SDK.")
        jsonObject.put("code", 440)
        val jsonString = jsonObject.toString()
        promise.reject(jsonString)
      }
      val jsonObject = JSONObject()
      jsonObject.put("message", "Internal Server Error.")
      jsonObject.put("code", 500)
      val jsonString = jsonObject.toString()
      promise.reject(jsonString)      
    }
    
  }

@ReactMethod
fun getComboSlabSchemes(requestData: ReadableMap,promise: Promise){
  try{
    val token = SDKConfig.accesstoken
      val refreshToken = SDKConfig.refreshtoken
      val refresAccessTokenObject = GenerateAccessToken(refreshToken,reactApplicationContext)
      if (isTokenExpired(token)) {
        refresAccessTokenObject.refreshAccessToken()
      }
      val accessToken = SDKConfig.accesstoken
      val context = reactApplicationContext
      val queue = Volley.newRequestQueue(context)
      val categoryIdsArray: Array<Int> = if (requestData.hasKey("categoryIds")) {
        val modeReadableArray = requestData.getArray("categoryIds")
        val modeList = mutableListOf<Int>()
        for (i in 0 until (modeReadableArray?.size() ?: 0)) {
            modeList.add(modeReadableArray?.getInt(i) ?: 0)
        }
        modeList.toTypedArray()
    } else {
        emptyArray()
    }
      val requestBody = GetComboSlabSchemesRequest(
        categoryIdsArray,
        requestData.getString("endDate") ?: "",
        requestData.getString("fromDate") ?: "",
        requestData.getString("status") ?: ""
    )
    val stringRequest = object : StringRequest(
        Method.POST, SDKConfig.baseurl+"/schemes/getComboSlabSchemes",
        Response.Listener { response ->
            promise.resolve(response)
        },
        Response.ErrorListener { error ->
          val errorCode = error.networkResponse?.statusCode
          val gson = Gson().toJson(error.networkResponse)
          if(errorCode == 403 || errorCode == 401){
            val jsonObject = JSONObject()
            jsonObject.put("message", "Session has timed out. Please re-initialize the SDK.")
            jsonObject.put("code", 440)
            val jsonString = jsonObject.toString()
            promise.reject(jsonString)
          }else if(errorCode == 440){
            val jsonObject = JSONObject()
            jsonObject.put("message", "Please retry the action")
            jsonObject.put("code", 440)
            val jsonString = jsonObject.toString()
            promise.reject(jsonString)
          }
          val jsonObject = JSONObject()
          jsonObject.put("message", "Internal Server Error.")
          jsonObject.put("code", 500)
          val jsonString = jsonObject.toString()
          promise.reject(jsonString)
        }) {
        override fun getBody(): ByteArray {
            val gson = Gson()
            val jsonBody = gson.toJson(requestBody)
            return jsonBody.toByteArray()
        }
        override fun getBodyContentType(): String {
            return "application/json"
        }
        override fun getHeaders(): Map<String, String> {
            val headers = HashMap<String, String>()
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
  }catch(error:Exception){
    if(error is RegenerateAccessTokenError){
      val jsonObject = JSONObject()
      jsonObject.put("message", "Session has timed out. Please re-initialize the SDK.")
      jsonObject.put("code", 440)
      val jsonString = jsonObject.toString()
      promise.reject(jsonString)
    }
    val jsonObject = JSONObject()
    jsonObject.put("message", "Internal Server Error.")
    jsonObject.put("code", 500)
    val jsonString = jsonObject.toString()
    promise.reject(jsonString)
  }
}

@ReactMethod
fun getSlabView(schemeId: String,promise: Promise){
  try{
    val token = SDKConfig.accesstoken
      val refreshToken = SDKConfig.refreshtoken
      val refresAccessTokenObject = GenerateAccessToken(refreshToken,reactApplicationContext)
      if (isTokenExpired(token)) {
        refresAccessTokenObject.refreshAccessToken()
      }
      val accessToken = SDKConfig.accesstoken
      val context = reactApplicationContext
      val queue = Volley.newRequestQueue(context)
      val requestBody = JSONObject()
      requestBody.put("schemeId", schemeId)
      val stringRequest = object : StringRequest(
        Method.POST, SDKConfig.baseurl+"/schemes/getSlabView",
        Response.Listener { response ->
            promise.resolve(response)
        },
        Response.ErrorListener { error ->
          val errorCode = error.networkResponse?.statusCode
          val gson = Gson().toJson(error.networkResponse)
          if(errorCode == 403 || errorCode == 401){
            val jsonObject = JSONObject()
            jsonObject.put("message", "Session has timed out. Please re-initialize the SDK.")
            jsonObject.put("code", 440)
            val jsonString = jsonObject.toString()
            promise.reject(jsonString)
          }else if(errorCode == 440){
            val jsonObject = JSONObject()
            jsonObject.put("message", "Please retry the action")
            jsonObject.put("code", 440)
            val jsonString = jsonObject.toString()
            promise.reject(jsonString)
          }
          val jsonObject = JSONObject()
          jsonObject.put("message", "Internal Server Error.")
          jsonObject.put("code", 500)
          val jsonString = jsonObject.toString()
          promise.reject(jsonString)
        }) {
        override fun getBody(): ByteArray {
            val gson = Gson()
            val jsonBody = gson.toJson(requestBody)
            return jsonBody.toByteArray()
        }
        override fun getBodyContentType(): String {
            return "application/json"
        }
        override fun getHeaders(): Map<String, String> {
            val headers = HashMap<String, String>()
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

  }catch(error:Exception){
    if(error is RegenerateAccessTokenError){
      val jsonObject = JSONObject()
      jsonObject.put("message", "Session has timed out. Please re-initialize the SDK.")
      jsonObject.put("code", 440)
      val jsonString = jsonObject.toString()
      promise.reject(jsonString)
    }
    val jsonObject = JSONObject()
    jsonObject.put("message", "Internal Server Error.")
    jsonObject.put("code", 500)
    val jsonString = jsonObject.toString()
    promise.reject(jsonString)
  }
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
