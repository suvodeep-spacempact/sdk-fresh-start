package com.vgretailersdk

class ValidateRetailerCouponRequest{
    var category : String = ""
    var couponCode : String = ""
    var from : String = ""
    var geolocation : String = ""
    var latitude : String = ""
    var longitude : String = ""
    var pin : String = ""
    var retailerCoupon : Boolean
    

    constructor(category: String,couponCode: String,from: String,geolocation: String,latitude: String,longitude: String,retailerCoupon: Boolean,pin: String) {
        this.category = category
        this.couponCode = couponCode
        this.from = from
        this.geolocation = geolocation
        this.latitude = latitude
        this.longitude = longitude
        this.retailerCoupon = retailerCoupon
        this.pin = pin
    }

}