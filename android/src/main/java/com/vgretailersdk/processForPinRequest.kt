package com.vgretailersdk

import com.google.gson.annotations.SerializedName

class ProcessForPinRequest{
    @SerializedName("userMobileNumber")
    var userMobileNumber: String = ""
    @SerializedName("couponCode")
    var couponCode: String = ""
    @SerializedName("pin")
    var pin: String = ""
    @SerializedName("smsText")
    var smsText: String = ""
    @SerializedName("from")
    var from: String = ""
    @SerializedName("userType")
    var userType: String = ""
    @SerializedName("userId")
    var userId: String = ""
    @SerializedName("apmID")
    var apmID: String = ""
    @SerializedName("userCode")
    var userCode: String = ""
    @SerializedName("latitude")
    var latitude: String = ""
    @SerializedName("longitude")
    var longitude: String = ""
    @SerializedName("geolocation")
    var geolocation: String = ""
    @SerializedName("category")
    var category: String = ""


    constructor(userMobileNumber: String,couponCode: String,pin: String,smsText: String,from: String,userType: String,userId: String,apmID: String,userCode: String,latitude: String,longitude: String,geolocation: String,category: String) {
        this.userMobileNumber = userMobileNumber
        this.couponCode = couponCode
        this.pin = pin
        this.smsText = smsText
        this.from = from
        this.userType = userType
        this.userId = userId
        this.apmID = apmID
        this.userCode = userCode
        this.latitude = latitude
        this.longitude = longitude
        this.geolocation = geolocation
        this.category = category
    }

}