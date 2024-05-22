package com.vgretailersdk

data class  ScanInRequest(
    val couponCode: String,
    val pin: String,
    val smsText: String,
    val from: String,
    val userType: String,
    val userId: String,
    val apmID: String,
    val userCode: String,
    val latitude: String,
    val longitude: String,
    val geolocation: String,
    val category: String
)
