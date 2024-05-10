package com.vgretailersdk

class ProcessForPinRequest{
    var userMobileNumber: String = ""
    var couponCode: String = ""
    var pin: String = ""
    var smsText: String = ""
    var from: String = ""
    var userType: String = ""
    var userId: String = ""
    var apmID: String = ""
    var userCode: String = ""
    var latitude: String = ""
    var longitude: String = ""
    var geolocation: String = ""
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