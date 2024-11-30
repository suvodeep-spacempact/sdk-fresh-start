package com.vgretailersdk

import com.google.gson.annotations.SerializedName

data class  ScanInRequest(
    @SerializedName("couponCode")
    val couponCode: String,
    @SerializedName("pin")
    val pin: String,
    @SerializedName("smsText")
    val smsText: String,
    @SerializedName("from")
    val from: String,
    @SerializedName("userType")
    val userType: String,
    @SerializedName("userId")
    val userId: String,
    @SerializedName("apmID")
    val apmID: String,
    @SerializedName("userCode")
    val userCode: String,
    @SerializedName("latitude")
    val latitude: String,
    @SerializedName("longitude")
    val longitude: String,
    @SerializedName("geolocation")
    val geolocation: String,
    @SerializedName("category")
    val category: String
)
