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
import com.vgretailersdk.PaymentDetails
import com.vgretailersdk.BankDetail
import com.vgretailersdk.SelectedProd
import com.vgretailersdk.PaperDbFunctions
import com.vgretailersdk.InitializeSDKNew
import com.vgretailersdk.ValidateRetailerCouponRequest
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
import com.vgretailersdk.ProcessForPinRequest
import com.vgretailersdk.FileUploadData
import com.vgretailersdk.SDKConfig
import com.vgretailersdk.UserData
import com.vgretailersdk.UserFile
import com.vgretailersdk.ScanInRequest
import com.vgretailersdk.TdsCertificate
import com.android.volley.VolleyError
import kotlinx.coroutines.*
import kotlinx.coroutines.android.*
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.WritableNativeMap
import java.nio.charset.StandardCharsets
import java.util.*
import io.paperdb.Paper
import com.android.volley.Request.Method
import java.util.HashMap
import android.content.ContentResolver
import android.net.Uri
import java.io.ByteArrayOutputStream
import java.io.InputStream
import okhttp3.*
import java.io.IOException
import android.os.ParcelFileDescriptor
import java.io.File
import okhttp3.Callback
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request as OkHttpRequest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.MediaType.Companion.toMediaType




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
    val paperDbObject = PaperDbFunctions();
    val token = paperDbObject.getAccessToken();
    val refreshToken = paperDbObject.getRefreshToken();
    val refresAccessTokenObject = GenerateAccessToken(refreshToken,reactApplicationContext)
    if (isTokenExpired(token)) {
      refresAccessTokenObject.refreshAccessToken()
    }
    val accessToken = paperDbObject.getAccessToken();
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
    val baseurl = paperDbObject.getBaseURL();
    val stringRequest = object : StringRequest(
      Method.POST, baseurl+"/banks/verifyBankDetails",
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
    val paperDbObject = PaperDbFunctions();
    val token = paperDbObject.getAccessToken();
    val refreshToken = paperDbObject.getRefreshToken();
    val refresAccessTokenObject = GenerateAccessToken(refreshToken,reactApplicationContext)
    if (isTokenExpired(token)) {
      refresAccessTokenObject.refreshAccessToken()
    }
    val accessToken = paperDbObject.getAccessToken();
    val baseurl = paperDbObject.getBaseURL();
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
      Method.POST, baseurl+"/product/getCategoriesList",
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
    val paperDbObject = PaperDbFunctions();
    val token = paperDbObject.getAccessToken();
    val refreshToken = paperDbObject.getRefreshToken();
    val refresAccessTokenObject = GenerateAccessToken(refreshToken,reactApplicationContext)
    if (isTokenExpired(token)) {
      refresAccessTokenObject.refreshAccessToken()
    }
    val accessToken = paperDbObject.getAccessToken();
    val baseurl = paperDbObject.getBaseURL();
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
    Log.d("cat","categoryIdsArray is $categoryIdsArray");
    Log.d("cat","subCategoryIdsArray is $subCategoryIdsArray")
    val requestBody = getUserBasePointsRequest(categoryIdsArray,subCategoryIdsArray)
    Log.d("cat","requestbody is $requestBody")
    val stringRequest = object : StringRequest(
      Method.POST, baseurl+"/coupon/getUserBasePoints",
      Response.Listener { response ->
        promise.resolve(response)
      },
      Response.ErrorListener { error ->
        val errorCode = error.networkResponse?.statusCode
        val gson = Gson().toJson(error.networkResponse)
        Log.d("cat","error response is $gson")
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
    val paperDbObject = PaperDbFunctions();
    val token = paperDbObject.getAccessToken();
    val refreshToken = paperDbObject.getRefreshToken();
    val refresAccessTokenObject = GenerateAccessToken(refreshToken,reactApplicationContext)
    if (isTokenExpired(token)) {
      refresAccessTokenObject.refreshAccessToken()
    }
    val accessToken = paperDbObject.getAccessToken();
    val baseurl = paperDbObject.getBaseURL();
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
      requestData.getString("toDate") ?: "",
    )
    val stringRequest = object : StringRequest(
      Method.POST, baseurl+"/coupon/getUserScanHistory",
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
    val paperDbObject = PaperDbFunctions();
    val token = paperDbObject.getAccessToken();
    val refreshToken = paperDbObject.getRefreshToken();
    val refresAccessTokenObject = GenerateAccessToken(refreshToken,reactApplicationContext)
    if (isTokenExpired(token)) {
      refresAccessTokenObject.refreshAccessToken()
    }
    val accessToken = paperDbObject.getAccessToken();
    val baseurl = paperDbObject.getBaseURL();
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
        Method.POST, baseurl+"/product/userRewardHistory",
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
      val paperDbObject = PaperDbFunctions();
    val token = paperDbObject.getAccessToken();
    val refreshToken = paperDbObject.getRefreshToken();
    val refresAccessTokenObject = GenerateAccessToken(refreshToken,reactApplicationContext)
    if (isTokenExpired(token)) {
      refresAccessTokenObject.refreshAccessToken()
    }
    val accessToken = paperDbObject.getAccessToken();
    val baseurl = paperDbObject.getBaseURL();
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
        Method.POST, baseurl+"/product/userScannedBalancePoints",
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
      val paperDbObject = PaperDbFunctions();
    val token = paperDbObject.getAccessToken();
    val refreshToken = paperDbObject.getRefreshToken();
    val refresAccessTokenObject = GenerateAccessToken(refreshToken,reactApplicationContext)
    if (isTokenExpired(token)) {
      refresAccessTokenObject.refreshAccessToken()
    }
    val accessToken = paperDbObject.getAccessToken();
    val baseurl = paperDbObject.getBaseURL();
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
        Method.POST, baseurl+"/product/userScanOutPointSummary",
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
  fun captureCustomerDetails(requestData: ReadableMap,promise: Promise){
    try{
      val paperDbObject = PaperDbFunctions();
    val token = paperDbObject.getAccessToken();
    val refreshToken = paperDbObject.getRefreshToken();
    val refresAccessTokenObject = GenerateAccessToken(refreshToken,reactApplicationContext)
    if (isTokenExpired(token)) {
      refresAccessTokenObject.refreshAccessToken()
    }
    val accessToken = paperDbObject.getAccessToken();
    val baseurl = paperDbObject.getBaseURL();
      val context = reactApplicationContext
      val queue = Volley.newRequestQueue(context)
      val url = baseurl + "/product/getCustomerDetails"
      val mobileNo = requestData.getString("mobileNo")
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
      val paperDbObject = PaperDbFunctions();
    val token = paperDbObject.getAccessToken();
    val refreshToken = paperDbObject.getRefreshToken();
    val refresAccessTokenObject = GenerateAccessToken(refreshToken,reactApplicationContext)
    if (isTokenExpired(token)) {
      refresAccessTokenObject.refreshAccessToken()
    }
    val accessToken = paperDbObject.getAccessToken();
    val baseurl = paperDbObject.getBaseURL();
      val context = reactApplicationContext
      val queue = Volley.newRequestQueue(context)
      val cresp = requestData.getMap("cresp")
      val selectedProd = requestData.getMap("selectedProd")
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
        cresp?.getString("custIdForProdInstall") ?: "",
        cresp?.getString("modelForProdInstall") ?: "", 
        cresp?.getInt("errorCode") ?: 0,
        cresp?.getString("errorMsg") ?: "",
        cresp?.getInt("statusType") ?: 0,
        cresp?.getString("balance") ?: "",
        cresp?.getString("currentPoints") ?: "",
        cresp?.getString("couponPoints") ?: "",
        cresp?.getString("promotionPoints") ?: "",
        cresp?.getString("transactId") ?: "",
        cresp?.getString("schemePoints") ?: "",
        cresp?.getString("basePoints") ?: "",
        cresp?.getString("clubPoints") ?: "",
        cresp?.getString("scanDate") ?: "",
        cresp?.getString("scanStatus") ?: "",
        cresp?.getString("copuonCode") ?: "",
        cresp?.getBoolean("bitEligibleScratchCard") ?: false,
        cresp?.getInt("pardId") ?: 0,
        cresp?.getString("partNumber") ?: "",
        cresp?.getString("partName") ?: "",
        cresp?.getString("couponCode") ?: "",
        cresp?.getString("skuDetail") ?: "",
        cresp?.getString("purchaseDate") ?: "",
        cresp?.getString("categoryId") ?: "",
        cresp?.getString("category") ?: "",
        cresp?.getInt("anomaly") ?: 0,
        cresp?.getString("warranty") ?: "",
      ),
      SelectedProd(
        selectedProd?.getString("specs") ?: "",
        selectedProd?.getString("pointsFormat") ?: "",
        selectedProd?.getString("product") ?: "",
        selectedProd?.getString("productName") ?: "",
        selectedProd?.getString("productCategory") ?: "",
        selectedProd?.getString("productCode") ?: "",
        selectedProd?.getInt("points") ?: 0,
        selectedProd?.getString("imageUrl") ?: "",
        selectedProd?.getString("userId") ?: "",
        selectedProd?.getString("productId") ?: "",
        selectedProd?.getString("paytmMobileNo") ?: "",
      ),
      requestData.getString("latitude") ?: "",
      requestData.getString("longitude") ?: "",   
      requestData.getString("geolocation") ?: "",
      requestData.getString("dealerCategory") ?: "",
    )
      val stringRequest = object : StringRequest(
        Method.POST, baseurl+"/product/registerWarranty",
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
  fun getEligibleProducts(requestData: ReadableMap,promise: Promise){
    try{
      val paperDbObject = PaperDbFunctions();
    val token = paperDbObject.getAccessToken();
    val refreshToken = paperDbObject.getRefreshToken();
    val refresAccessTokenObject = GenerateAccessToken(refreshToken,reactApplicationContext)
    if (isTokenExpired(token)) {
      refresAccessTokenObject.refreshAccessToken()
    }
    val accessToken = paperDbObject.getAccessToken();
    val baseurl = paperDbObject.getBaseURL();
      val context = reactApplicationContext
      val queue = Volley.newRequestQueue(context)
      val requestBody = JSONObject()
      Log.d("b","$requestBody")
      requestBody.put("categoryId", requestData.getString("categoryId"))
      requestBody.put("schemeId", requestData.getString("schemeId"))
      Log.d("b"," request body is $requestBody")
      val stringRequest = object : StringRequest(
        Method.POST, baseurl+"/schemes/getEligibleProducts",
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
            return requestBody.toString().toByteArray()
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
      Log.d("b","error is $error")
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
    val paperDbObject = PaperDbFunctions();
    val token = paperDbObject.getAccessToken();
    val refreshToken = paperDbObject.getRefreshToken();
    val refresAccessTokenObject = GenerateAccessToken(refreshToken,reactApplicationContext)
    if (isTokenExpired(token)) {
      refresAccessTokenObject.refreshAccessToken()
    }
    val accessToken = paperDbObject.getAccessToken();
    val baseurl = paperDbObject.getBaseURL();
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
        Method.POST, baseurl+"/schemes/getComboSlabSchemes",
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
fun getSlabView(requestData: ReadableMap,promise: Promise){
  try{
    val paperDbObject = PaperDbFunctions();
    val token = paperDbObject.getAccessToken();
    val refreshToken = paperDbObject.getRefreshToken();
    val refresAccessTokenObject = GenerateAccessToken(refreshToken,reactApplicationContext)
    if (isTokenExpired(token)) {
      refresAccessTokenObject.refreshAccessToken()
    }
    val accessToken = paperDbObject.getAccessToken();
    val baseurl = paperDbObject.getBaseURL();
      val context = reactApplicationContext
      val queue = Volley.newRequestQueue(context)
      val requestBody = JSONObject()
      requestBody.put("schemeId", requestData.getString("schemeId"))
      val stringRequest = object : StringRequest(
        Method.POST, baseurl+"/schemes/getSlabView",
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
            // val gson = Gson()
            // val jsonBody = gson.toJson(requestBody)
            // Log.d("d","jsonboduy is $jsonBody")
            // return jsonBody.toByteArray()
            return requestBody.toString().toByteArray()
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
fun getCrossSchemesDetails(requestData: ReadableMap,promise: Promise){
  try{
    val paperDbObject = PaperDbFunctions();
    val token = paperDbObject.getAccessToken();
    val refreshToken = paperDbObject.getRefreshToken();
    val refresAccessTokenObject = GenerateAccessToken(refreshToken,reactApplicationContext)
    if (isTokenExpired(token)) {
      refresAccessTokenObject.refreshAccessToken()
    }
    val accessToken = paperDbObject.getAccessToken();
    val baseurl = paperDbObject.getBaseURL();
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
        Method.POST, baseurl+"/schemes/getSchemes/comboDetails",
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
fun getSlabBasedSchemes(requestData: ReadableMap,promise: Promise){
  try{
    val paperDbObject = PaperDbFunctions();
    val token = paperDbObject.getAccessToken();
    val refreshToken = paperDbObject.getRefreshToken();
    val refresAccessTokenObject = GenerateAccessToken(refreshToken,reactApplicationContext)
    if (isTokenExpired(token)) {
      refresAccessTokenObject.refreshAccessToken()
    }
    val accessToken = paperDbObject.getAccessToken();
    val baseurl = paperDbObject.getBaseURL();
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
        Method.POST, baseurl+"/schemes/getSchemes/slabDetails",
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
fun validateRetailerCoupon(requestData: ReadableMap,promise: Promise){
  try{
    val paperDbObject = PaperDbFunctions();
    val token = paperDbObject.getAccessToken();
    val refreshToken = paperDbObject.getRefreshToken();
    val refresAccessTokenObject = GenerateAccessToken(refreshToken,reactApplicationContext)
    if (isTokenExpired(token)) {
      refresAccessTokenObject.refreshAccessToken()
    }
    val accessToken = paperDbObject.getAccessToken();
    val baseurl = paperDbObject.getBaseURL();
      val context = reactApplicationContext
      val queue = Volley.newRequestQueue(context)
      val requestBody = ValidateRetailerCouponRequest(
        requestData.getString("category") ?: "",
        requestData.getString("couponCode") ?: "",
        requestData.getString("from") ?: "",
        requestData.getString("geolocation") ?: "",
        requestData.getString("latitude") ?: "",
        requestData.getString("longitude") ?: "",
        requestData.getString("retailerCoupon") ?.toBoolean() ?: false,
    )
    val stringRequest = object : StringRequest(
        Method.POST, baseurl+"/coupon/validateRetailerCoupon",
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
fun registerCustomer(requestData: ReadableMap,promise: Promise){
  try{
    val paperDbObject = PaperDbFunctions();
  val token = paperDbObject.getAccessToken();
  val refreshToken = paperDbObject.getRefreshToken();
  val refresAccessTokenObject = GenerateAccessToken(refreshToken,reactApplicationContext)
  if (isTokenExpired(token)) {
    refresAccessTokenObject.refreshAccessToken()
  }
  val accessToken = paperDbObject.getAccessToken();
  val baseurl = paperDbObject.getBaseURL();
    val context = reactApplicationContext
    val queue = Volley.newRequestQueue(context)
    val cresp = requestData.getMap("cresp")
    val selectedProd = requestData.getMap("selectedProd")
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
        cresp?.getString("custIdForProdInstall") ?: "",
        cresp?.getString("modelForProdInstall") ?: "", 
        cresp?.getInt("errorCode") ?: 0,
        cresp?.getString("errorMsg") ?: "",
        cresp?.getInt("statusType") ?: 0,
        cresp?.getString("balance") ?: "",
        cresp?.getString("currentPoints") ?: "",
        cresp?.getString("couponPoints") ?: "",
        cresp?.getString("promotionPoints") ?: "",
        cresp?.getString("transactId") ?: "",
        cresp?.getString("schemePoints") ?: "",
        cresp?.getString("basePoints") ?: "",
        cresp?.getString("clubPoints") ?: "",
        cresp?.getString("scanDate") ?: "",
        cresp?.getString("scanStatus") ?: "",
        cresp?.getString("copuonCode") ?: "",
        cresp?.getBoolean("bitEligibleScratchCard") ?: false,
        cresp?.getInt("pardId") ?: 0,
        cresp?.getString("partNumber") ?: "",
        cresp?.getString("partName") ?: "",
        cresp?.getString("couponCode") ?: "",
        cresp?.getString("skuDetail") ?: "",
        cresp?.getString("purchaseDate") ?: "",
        cresp?.getString("categoryId") ?: "",
        cresp?.getString("category") ?: "",
        cresp?.getInt("anomaly") ?: 0,
        cresp?.getString("warranty") ?: "",
      ),
      SelectedProd(
        selectedProd?.getString("specs") ?: "",
        selectedProd?.getString("pointsFormat") ?: "",
        selectedProd?.getString("product") ?: "",
        selectedProd?.getString("productName") ?: "",
        selectedProd?.getString("productCategory") ?: "",
        selectedProd?.getString("productCode") ?: "",
        selectedProd?.getInt("points") ?: 0,
        selectedProd?.getString("imageUrl") ?: "",
        selectedProd?.getString("userId") ?: "",
        selectedProd?.getString("productId") ?: "",
        selectedProd?.getString("paytmMobileNo") ?: "",
      ),
      requestData.getString("latitude") ?: "",
      requestData.getString("longitude") ?: "",   
      requestData.getString("geolocation") ?: "",
      requestData.getString("dealerCategory") ?: "",
    )
    val stringRequest = object : StringRequest(
      Method.POST, baseurl+"/product/registerCustomer",
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
    Log.d("b","$error")
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
fun processForPin(requestData: ReadableMap,promise: Promise){
  try{
    val paperDbObject = PaperDbFunctions();
    val token = paperDbObject.getAccessToken();
    val refreshToken = paperDbObject.getRefreshToken();
    val refresAccessTokenObject = GenerateAccessToken(refreshToken,reactApplicationContext)
    if (isTokenExpired(token)) {
      refresAccessTokenObject.refreshAccessToken()
    }
    val accessToken = paperDbObject.getAccessToken();
    val baseurl = paperDbObject.getBaseURL();
      val context = reactApplicationContext
      val queue = Volley.newRequestQueue(context)
      val requestBody = ProcessForPinRequest(
        requestData.getString("userMobileNumber") ?: "",
        requestData.getString("couponCode") ?: "",
        requestData.getString("pin") ?: "",
        requestData.getString("smsText") ?: "",
        requestData.getString("from") ?: "",
        requestData.getString("userType") ?: "",
        requestData.getString("userId") ?: "",
        requestData.getString("apmID") ?: "",
        requestData.getString("userCode") ?: "",
        requestData.getString("latitude") ?: "",
        requestData.getString("longitude") ?: "",
        requestData.getString("geolocation") ?: "",
        requestData.getString("category") ?: "",
    )
    val stringRequest = object : StringRequest(
        Method.POST, baseurl+"/coupon/processForPin",
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
fun processCoupon(requestData: ReadableMap,promise: Promise){
  try{
    val paperDbObject = PaperDbFunctions();
    val token = paperDbObject.getAccessToken();
    val refreshToken = paperDbObject.getRefreshToken();
    val refresAccessTokenObject = GenerateAccessToken(refreshToken,reactApplicationContext)
    if (isTokenExpired(token)) {
      refresAccessTokenObject.refreshAccessToken()
    }
    val accessToken = paperDbObject.getAccessToken();
    val baseurl = paperDbObject.getBaseURL();
      val context = reactApplicationContext
      val queue = Volley.newRequestQueue(context)
      val requestBody = ProcessForPinRequest(
        requestData.getString("userMobileNumber") ?: "",
        requestData.getString("couponCode") ?: "",
        requestData.getString("pin") ?: "",
        requestData.getString("smsText") ?: "",
        requestData.getString("from") ?: "",
        requestData.getString("userType") ?: "",
        requestData.getString("userId") ?: "",
        requestData.getString("apmID") ?: "",
        requestData.getString("userCode") ?: "",
        requestData.getString("latitude") ?: "",
        requestData.getString("longitude") ?: "",
        requestData.getString("geolocation") ?: "",
        requestData.getString("category") ?: "",
    )
    val stringRequest = object : StringRequest(
        Method.POST, baseurl+"/coupon/process",
        Response.Listener { response ->
            promise.resolve(response)
        },
        Response.ErrorListener { error ->
          Log.d("b","error is in error listnere $error")
          val errorCode = error.networkResponse?.statusCode
          val gson = Gson().toJson(error.networkResponse)
          Log.d("b","error is in error listnere $gson")
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
    Log.d("b","$error")
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
  fun getCategoryProductDetails(requestData: ReadableMap,promise: Promise){
    try{
      val paperDbObject = PaperDbFunctions();
    val token = paperDbObject.getAccessToken();
    val refreshToken = paperDbObject.getRefreshToken();
    val refresAccessTokenObject = GenerateAccessToken(refreshToken,reactApplicationContext)
    if (isTokenExpired(token)) {
      refresAccessTokenObject.refreshAccessToken()
    }
    val accessToken = paperDbObject.getAccessToken();
    val baseurl = paperDbObject.getBaseURL();
      val context = reactApplicationContext
      val queue = Volley.newRequestQueue(context)
      val requestBody = JSONObject()
      Log.d("b","$requestBody")
      requestBody.put("subCategory", requestData.getString("subCategory"))
      requestBody.put("category", requestData.getString("category"))
      requestBody.put("skuId", requestData.getString("skuId"))
      Log.d("b"," request body is $requestBody")
      val stringRequest = object : StringRequest(
        Method.POST, baseurl+"/product/getCategoryProductDetails",
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
            return requestBody.toString().toByteArray()
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
      Log.d("b","error is $error")
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
  fun bankTransfer(requestData: ReadableMap,promise: Promise){
    try{
      val paperDbObject = PaperDbFunctions();
      val token = paperDbObject.getAccessToken();
      val refreshToken = paperDbObject.getRefreshToken();
      val refresAccessTokenObject = GenerateAccessToken(refreshToken,reactApplicationContext)
      if (isTokenExpired(token)) {
        refresAccessTokenObject.refreshAccessToken()
      }
      val accessToken = paperDbObject.getAccessToken();
      val baseurl = paperDbObject.getBaseURL();
      val bankDetail = requestData.getMap("bankDetail")
        val context = reactApplicationContext
        val queue = Volley.newRequestQueue(context)
        val requestBody = PaymentDetails(
          requestData.getString("amount") ?: "",
          BankDetail(
            bankDetail?.getString("bankAccHolderName") ?: "",
            bankDetail?.getString("bankAccNo") ?: "",
            bankDetail?.getString("bankAccType") ?: "",
            bankDetail?.getString("bankIfsc") ?: "",
            bankDetail?.getString("bankNameAndBranch") ?: "",
            bankDetail?.getString("checkPhoto") ?: "",
          )
      )
      val stringRequest = object : StringRequest(
          Method.POST, baseurl+"/order/bankTransfer",
          Response.Listener { response ->
              promise.resolve(response)
          },
          Response.ErrorListener { error ->
            Log.d("b","error is in error listnere $error")
            val errorCode = error.networkResponse?.statusCode
            val gson = Gson().toJson(error.networkResponse)
            Log.d("b","error is in error listnere $gson")
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
      Log.d("b","$error")
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
fun scanIn(requestData: ReadableMap,promise: Promise){
  try{
    val paperDbObject = PaperDbFunctions();
    val token = paperDbObject.getAccessToken();
    val refreshToken = paperDbObject.getRefreshToken();
    val refresAccessTokenObject = GenerateAccessToken(refreshToken,reactApplicationContext)
    if (isTokenExpired(token)) {
      refresAccessTokenObject.refreshAccessToken()
    }
    val accessToken = paperDbObject.getAccessToken();
    val baseurl = paperDbObject.getBaseURL();
      val context = reactApplicationContext
      val queue = Volley.newRequestQueue(context)
      val requestBody = ScanInRequest(
        requestData.getString("couponCode") ?: "",
        requestData.getString("pin") ?: "",
        requestData.getString("smsText") ?: "",
        requestData.getString("from") ?: "",
        requestData.getString("userType") ?: "",
        requestData.getString("userId") ?:"", 
        requestData.getString("apmID") ?:"",
        requestData.getString("userCode") ?: "",
        requestData.getString("latitude") ?: "",
        requestData.getString("longitude") ?: "",
        requestData.getString("geolocation") ?: "",
        requestData.getString("category") ?: "",
    )
    val stringRequest = object : StringRequest(
        Method.POST, baseurl+"/coupon/scanIn",
        Response.Listener { response ->
            promise.resolve(response)
        },
        Response.ErrorListener { error ->
          Log.d("b","error is in error listnere $error")
          val errorCode = error.networkResponse?.statusCode
          val gson = Gson().toJson(error.networkResponse)
          Log.d("b","error is in error listnere $gson")
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
    Log.d("b","$error")
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
  fun getFile(requestData: ReadableMap,promise: Promise){
    try{
      val paperDbObject = PaperDbFunctions();
    val token = paperDbObject.getAccessToken();
    val refreshToken = paperDbObject.getRefreshToken();
    val refresAccessTokenObject = GenerateAccessToken(refreshToken,reactApplicationContext)
    if (isTokenExpired(token)) {
      refresAccessTokenObject.refreshAccessToken()
    }
    val accessToken = paperDbObject.getAccessToken();
    val baseurl = paperDbObject.getBaseURL();
      val context = reactApplicationContext
      val queue = Volley.newRequestQueue(context)
      val url = baseurl + "/file"
      val uuid = requestData.getString("uuid")
      val imageRelated = requestData.getString("imageRelated")
      val userRole = requestData.getString("userRole")
      val fullUrl = "$url/$uuid/$imageRelated/$userRole"

      Log.d("b",fullUrl)
      val stringRequest = object : StringRequest(
        Method.GET, 
        fullUrl, // Modified URL with query parameters
        Response.Listener { response ->
          promise.resolve(response)
        },
        Response.ErrorListener { error ->
          Log.d("b","error is $error")
          val errorCode = error.networkResponse?.statusCode
          val gson = Gson().toJson(error.networkResponse)
          Log.d("b","error response is $gson")
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
      Log.d("b","error is : ",error)
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
fun uploadFile(requestData: ReadableMap,promise:Promise) {
  try{
    val imageRelated = requestData.getString("imageRelated");
    val userRole = requestData.getString("userRole");
    val file = requestData.getMap("file");
    val fileName = file?.getString("fileName");
    val filetype = file?.getString("fileType");
    val fileUriString = file?.getString("fileUri");
    val paperDbObject = PaperDbFunctions();
    val baseurl = paperDbObject.getBaseURL();
    val fileUri = Uri.parse(fileUriString)
    val context = reactApplicationContext
    val contentResolver = context.contentResolver
    val inputStream = contentResolver.openInputStream(fileUri)
        ?: throw IOException("Could not open input stream for URI: $fileUri")
    val requestBodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
    val fileMediaType = filetype?.toMediaTypeOrNull() ?: "application/octet-stream".toMediaTypeOrNull()
    val fileRequestBody = inputStream.use { inputStream ->
        inputStream?.readBytes()?.toRequestBody(fileMediaType)
    }
    requestBodyBuilder.addFormDataPart("file", fileName, fileRequestBody!!)
    requestBodyBuilder.addFormDataPart("imageRelated", "BILL")
    requestBodyBuilder.addFormDataPart("userRole", "2")
    val requestBody = requestBodyBuilder.build()
    val client = OkHttpClient()
    val request = OkHttpRequest.Builder()
        .url(baseurl+"/file")
        .post(requestBody)
        .build()
    val response = client.newCall(request).execute()
    if (response.isSuccessful) {
      val responseBody = response.body?.string()
      promise.resolve(responseBody)
  } else {
    val jsonObject = JSONObject()
    jsonObject.put("message", "errorMessage")
    jsonObject.put("code", response.code)
    val jsonString = jsonObject.toString()
    promise.reject(jsonString)
  }
  }catch(error:Exception){
    val jsonObject = JSONObject()
    jsonObject.put("message", "Internal Server Error.")
    jsonObject.put("code", 500)
    val jsonString = jsonObject.toString()
    promise.reject(jsonString)   
  }
}


@ReactMethod
fun getSchemeFileList(requestData: ReadableMap,promise: Promise){
  try{
    val paperDbObject = PaperDbFunctions();
    val token = paperDbObject.getAccessToken();
    val refreshToken = paperDbObject.getRefreshToken();
    val refresAccessTokenObject = GenerateAccessToken(refreshToken,reactApplicationContext)
    if (isTokenExpired(token)) {
      refresAccessTokenObject.refreshAccessToken()
    }
    val accessToken = paperDbObject.getAccessToken();
    val baseurl = paperDbObject.getBaseURL();
      val context = reactApplicationContext
      val queue = Volley.newRequestQueue(context)
      val requestBody = JSONObject()
      requestBody.put("schemeId", requestData.getString("schemeId"))
      val stringRequest = object : StringRequest(
        Method.POST, baseurl+"/schemes/file/getSpecialSchemes",
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
            // val gson = Gson()
            // val jsonBody = gson.toJson(requestBody)
            // Log.d("d","jsonboduy is $jsonBody")
            // return jsonBody.toByteArray()
            return requestBody.toString().toByteArray()
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
fun getTdsCertificate(requestData: ReadableMap,promise: Promise) {
  try{
    val paperDbObject = PaperDbFunctions();
    val token = paperDbObject.getAccessToken();
    val refreshToken = paperDbObject.getRefreshToken();
    val refresAccessTokenObject = GenerateAccessToken(refreshToken,reactApplicationContext)
    if (isTokenExpired(token)) {
      refresAccessTokenObject.refreshAccessToken()
    }
    val accessToken = paperDbObject.getAccessToken();
    val context = reactApplicationContext
    val queue = Volley.newRequestQueue(context)
    val request = TdsCertificate(
      requestData.getString("fileId") ?: "",
      requestData.getString("fiscalStartYear") ?: "",
      requestData.getString("fiscalEndYear") ?: "",
      requestData.getString("quater") ?: "",
    )
    val baseurl = paperDbObject.getBaseURL();
    val stringRequest = object : StringRequest(
      Method.POST, baseurl+"/user/tdsCertificate",
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
fun GetPrimarySchemeFileList(promise: Promise){
  try{
    val paperDbObject = PaperDbFunctions();
    val token = paperDbObject.getAccessToken();
    val refreshToken = paperDbObject.getRefreshToken();
    val refresAccessTokenObject = GenerateAccessToken(refreshToken,reactApplicationContext)
    if (isTokenExpired(token)) {
      refresAccessTokenObject.refreshAccessToken()
    }
    val accessToken = paperDbObject.getAccessToken();
    val baseurl = paperDbObject.getBaseURL();
      val context = reactApplicationContext
      val queue = Volley.newRequestQueue(context)
      
    val stringRequest = object : StringRequest(
        Method.GET, baseurl+"/schemes/getActiveSchemeOffers",
        Response.Listener { response ->
            promise.resolve(response)
        },
        Response.ErrorListener { error ->
          Log.d("b","error is in error listnere $error")
          val errorCode = error.networkResponse?.statusCode
          val gson = Gson().toJson(error.networkResponse)
          Log.d("b","error is in error listnere $gson")
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
    Log.d("b","$error")
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
    Paper.init(reactApplicationContext)
    val sdk = InitializeSDKNew(
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
