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
import com.vgretailersdk.AuthenticationError
import com.vgretailersdk.SDKConfig
import com.android.volley.VolleyError
import kotlinx.coroutines.*
import kotlinx.coroutines.android.*


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
  
  //@ReactMethod
//   fun verifyBankDetails(requestData: ReadableMap,promise: Promise) {
//     try{
//         val refreshToken = SDKConfig.refreshtoken
//         val refresAccessTokenObject = GenerateAccessToken(refreshToken,reactApplicationContext)
//         val response = refresAccessTokenObject.refreshAccessToken(){ refreshedToken: String?, error: AuthenticationError? ->
//             try{
//             if (error != null) {
//                 Log.e("car", "Error occurred in callback:", error)
//                 throw error
//             }else{
//                 val accessToken = SDKConfig.accesstoken
//                 val context = reactApplicationContext
//                 val queue = Volley.newRequestQueue(context)
//                 val request = VerifyBankDetailsRequest(
//                     requestData.getString("bankIfsc") ?: "",
//                     requestData.getString("bankAccNo") ?: "",
//                     requestData.getString("bankAccHolderName") ?: "",
//                     requestData.getString("bankAccType") ?: "",
//                     requestData.getString("bankNameAndBranch") ?: "",
//                     requestData.getString("checkPhoto") ?: ""
//                 )
//                 val stringRequest = object : StringRequest(
//                     Method.POST, SDKConfig.baseurl+"/banks/verifyBankDetails",
//                     Response.Listener { response ->
//                         promise.resolve(response)
//                         Log.d("car","api response is $response")
//                     },
//                     Response.ErrorListener { error ->
//                         Log.e("car", "Error occurred inside api request:", error)
//                         //throw new AuthenticationError("Access Token Error")
//                         throw Exception("This is a normal exception")
//                     }) {
//                         override fun getBody(): ByteArray {
//                             val gson = Gson()
//                             val jsonBody = gson.toJson(request)
//                             return jsonBody.toByteArray()
//                     }
//                     override fun getBodyContentType(): String {
//                         return "application/json"
//                     }
//                     override fun getHeaders(): Map<String, String> {
//                         val headers = HashMap<String, String>()
//                         headers["Authorization"] = "Bearer $accessToken"
//                         return headers
//                     }
//                 }
//                 stringRequest.retryPolicy = DefaultRetryPolicy(
//                     50000,
//                     DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                     DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
//                 )
//                 queue.add(stringRequest)
//             }
//             }catch (e: Exception) {
//                 Log.d("car","enteered")
//                 throw e
//             }
//         }
//         Log.d("car","response is $response")
//     }catch(error: Throwable){
//         Log.e("car", "Authentication Error occurred:", error)
//         promise.reject(error.message)
//     }
// }
@ReactMethod
fun verifyBankDetails(requestData: ReadableMap,promise: Promise) {
    try{
        val refreshToken = SDKConfig.refreshtoken
        val refresAccessTokenObject = GenerateAccessToken(refreshToken,reactApplicationContext)
        val oldaccesstoken = SDKConfig.accesstoken;
        Log.d("car","old access token is $oldaccesstoken")
        runBlocking {
            refresAccessTokenObject.refreshAccessToken()
        }
        val accessToken = SDKConfig.accesstoken
        Log.d("car","new accesstoken is $accessToken")
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
                Log.d("car","api response is $response")
            },
            Response.ErrorListener { error ->
                Log.e("car", "Error occurred inside api request:", error)
                //throw new AuthenticationError("Access Token Error")
                throw Exception("This is a normal exception")
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
        Log.d("car","inside catch")
        promise.reject("Access token error")
    }
}
    
//   @ReactMethod
//   fun checkIfUserExists(mobileNumber:String,promise: Promise){
//     val refreshToken = SDKConfig.refreshtoken
//     val accessToken = SDKConfig.accesstoken
//     val context = reactApplicationContext
//     val queue = Volley.newRequestQueue(context)
//     val requestBody = CheckIfUserExistRequest(mobileNumber)
//     val stringRequest = object : StringRequest(
//         Method.POST, SDKConfig.baseurl+"/user/verifyUserMobile",
//         Response.Listener { response ->
//             // Display the response string
//             promise.resolve(response)
//         },
//         Response.ErrorListener { error ->
//             // Handle error
//             promise.reject(error);
//         }) {
//         // Override getBody to return the request body
//         override fun getBody(): ByteArray {
//             //return params.toString().toByteArray()
//             val gson = Gson()
//             val jsonBody = gson.toJson(requestBody)
//             return jsonBody.toByteArray()
//         }

//         // Override getBodyContentType to specify the content type
//         override fun getBodyContentType(): String {
//             return "application/json"
//         }
//         // Override getHeaders to add authorization header
//         override fun getHeaders(): Map<String, String> {
//             val headers = HashMap<String, String>()
//             // Add authorization header with Bearer token
//             headers["Authorization"] = "Bearer $accessToken"
//             return headers
//         }
//     }
//     stringRequest.retryPolicy = DefaultRetryPolicy(
//         50000,
//         DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//         DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
//     )
//     queue.add(stringRequest)
//   }

//   @ReactMethod
//   fun rewardPointsHistory(requestData: ReadableMap,promise: Promise){
//     val refreshToken = SDKConfig.refreshtoken
//     val refresAccessTokenObject = GenerateAccessToken(refreshToken,reactApplicationContext)
//     refresAccessTokenObject.refreshAccessToken(){ refreshedToken: String?, error: AuthenticationError? ->
//         if (error != null) {
//         }else{
//             val accessToken = SDKConfig.accesstoken
//     val context = reactApplicationContext
//     val queue = Volley.newRequestQueue(context)

//     val modeArray: Array<String> = if (requestData.hasKey("mode")) {
//         val modeReadableArray = requestData.getArray("mode")
//         val modeList = mutableListOf<String>()
//         for (i in 0 until (modeReadableArray?.size() ?: 0)) {
//             modeList.add(modeReadableArray?.getString(i) ?: "")
//         }
//         modeList.toTypedArray()
//     } else {
//         emptyArray()
//     }

//     val statusArray: Array<String> = if (requestData.hasKey("status")) {
//         val statusReadableArray = requestData.getArray("status")
//         val statusList = mutableListOf<String>()
//         for (i in 0 until (statusReadableArray?.size() ?: 0)) {
//             statusList.add(statusReadableArray?.getString(i) ?: "")
//         }
//         statusList.toTypedArray()
//     } else {
//         emptyArray()
//     }
    
//     val requestBody = RewardHistoryRequest(
//         modeArray,
//         statusArray,
//         requestData.getString("fromDate") ?: "",
//         requestData.getString("toDate") ?: "",
//         requestData.getString("userId") ?: "",   
//     )
//     val stringRequest = object : StringRequest(
//         Method.POST, SDKConfig.baseurl+"/product/userRewardHistory",
//         Response.Listener { response ->
//             // Display the response string
//             promise.resolve(response)
//         },
//         Response.ErrorListener { error ->
//             // Handle error
//             promise.reject(error);
//         }) {
//         // Override getBody to return the request body
//         override fun getBody(): ByteArray {
//             //return params.toString().toByteArray()
//             val gson = Gson()
//             val jsonBody = gson.toJson(requestBody)
//             return jsonBody.toByteArray()
//         }

//         // Override getBodyContentType to specify the content type
//         override fun getBodyContentType(): String {
//             return "application/json"
//         }
//         override fun getHeaders(): Map<String, String> {
//             val headers = HashMap<String, String>()
//             // Add authorization header with Bearer token
//             headers["Authorization"] = "Bearer $accessToken"
//             return headers
//         }
//     }
//     stringRequest.retryPolicy = DefaultRetryPolicy(
//         50000,
//         DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//         DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
//     )
//     queue.add(stringRequest)
//         }
//     }
//   }

//   @ReactMethod
//   fun ScannedBalancePoints(requestData: ReadableMap,promise: Promise){
//     val refreshToken = SDKConfig.refreshtoken
//     val refresAccessTokenObject = GenerateAccessToken(refreshToken,reactApplicationContext)
//     refresAccessTokenObject.refreshAccessToken(){ refreshedToken: String?, error: AuthenticationError? ->
//         if (error != null) {
//         }else{
//             val accessToken = SDKConfig.accesstoken
//     val refresAccessTokenObject = GenerateAccessToken(refreshToken,reactApplicationContext)
//     val context = reactApplicationContext
//     val queue = Volley.newRequestQueue(context)
//     val categoriesArray: Array<Int> = if (requestData.hasKey("categories")) {
//         val modeReadableArray = requestData.getArray("categories")
//         val modeList = mutableListOf<Int>()
//         for (i in 0 until (modeReadableArray?.size() ?: 0)) {
//             modeList.add(modeReadableArray?.getInt(i) ?: 0)
//         }
//         modeList.toTypedArray()
//     } else {
//         emptyArray()
//     }
//     val subCategoriesArray: Array<Int> = if (requestData.hasKey("subCategories")) {
//         val modeReadableArray = requestData.getArray("subCategories")
//         val modeList = mutableListOf<Int>()
//         for (i in 0 until (modeReadableArray?.size() ?: 0)) {
//             modeList.add(modeReadableArray?.getInt(i) ?: 0)
//         }
//         modeList.toTypedArray()
//     } else {
//         emptyArray()
//     }
//     val requestBody = ScannedBalancePointsRequest(
//         categoriesArray,
//         subCategoriesArray,
//         requestData.getString("userId") ?: "",
//     )
//     val stringRequest = object : StringRequest(
//         Method.POST, SDKConfig.baseurl+"/product/userScannedBalancePoints",
//         Response.Listener { response ->
//             // Display the response string
//             promise.resolve(response)
//         },
//         Response.ErrorListener { error ->
//             // Handle error
//             promise.reject(error);
//         }) {
//         // Override getBody to return the request body
//         override fun getBody(): ByteArray {
//             //return params.toString().toByteArray()
//             val gson = Gson()
//             val jsonBody = gson.toJson(requestBody)
//             return jsonBody.toByteArray()
//         }

//         // Override getBodyContentType to specify the content type
//         override fun getBodyContentType(): String {
//             return "application/json"
//         }
//         override fun getHeaders(): Map<String, String> {
//             val headers = HashMap<String, String>()
//             // Add authorization header with Bearer token
//             headers["Authorization"] = "Bearer $accessToken"
//             return headers
//         }
//     }
//     stringRequest.retryPolicy = DefaultRetryPolicy(
//         50000,
//         DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//         DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
//     )
//     queue.add(stringRequest)
//         }
//     }
//   }
//   @ReactMethod
//   fun userScanOutPointSummary(requestData: ReadableMap,promise: Promise){
//     val refreshToken = SDKConfig.refreshtoken
//     val refresAccessTokenObject = GenerateAccessToken(refreshToken,reactApplicationContext)
//     refresAccessTokenObject.refreshAccessToken(){ refreshedToken: String?, error: AuthenticationError? ->
//         if (error != null) {
//         }else{
//             val accessToken = SDKConfig.accesstoken
//     val context = reactApplicationContext
//     val queue = Volley.newRequestQueue(context)
//     val categoriesArray: Array<Int> = if (requestData.hasKey("categories")) {
//         val modeReadableArray = requestData.getArray("categories")
//         val modeList = mutableListOf<Int>()
//         for (i in 0 until (modeReadableArray?.size() ?: 0)) {
//             modeList.add(modeReadableArray?.getInt(i) ?: 0)
//         }
//         modeList.toTypedArray()
//     } else {
//         emptyArray()
//     }
//     val subCategoriesArray: Array<Int> = if (requestData.hasKey("subCategories")) {
//         val modeReadableArray = requestData.getArray("subCategories")
//         val modeList = mutableListOf<Int>()
//         for (i in 0 until (modeReadableArray?.size() ?: 0)) {
//             modeList.add(modeReadableArray?.getInt(i) ?: 0)
//         }
//         modeList.toTypedArray()
//     } else {
//         emptyArray()
//     }
//     val requestBody = ScannedBalancePointsRequest(
//         categoriesArray,
//         subCategoriesArray,
//         requestData.getString("userId") ?: "",
//     )
//     val stringRequest = object : StringRequest(
//         Method.POST, SDKConfig.baseurl+"/product/userScanOutPointSummary",
//         Response.Listener { response ->
//             // Display the response string
           
//             promise.resolve(response)
//         },
//         Response.ErrorListener { error ->
//             // Handle error
//             promise.reject(error);
            
//         }) {
//         // Override getBody to return the request body
//         override fun getBody(): ByteArray {
//             //return params.toString().toByteArray()
//             val gson = Gson()
//             val jsonBody = gson.toJson(requestBody)
//             return jsonBody.toByteArray()
//         }

//         // Override getBodyContentType to specify the content type
//         override fun getBodyContentType(): String {
//             return "application/json"
//         }
//         override fun getHeaders(): Map<String, String> {
//             val headers = HashMap<String, String>()
//             // Add authorization header with Bearer token
//             headers["Authorization"] = "Bearer $accessToken"
//             return headers
//         }
//     }
//     stringRequest.retryPolicy = DefaultRetryPolicy(
//         50000,
//         DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//         DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
//     )
//     queue.add(stringRequest)
//         }
//     }
//   }

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
