package com.vgretailersdk

import com.google.gson.annotations.SerializedName


class ValidateRetailerCouponRequest{
    @SerializedName("category")
    var category : String = ""
    @SerializedName("couponCode")
    var couponCode : String = ""
    @SerializedName("from")
    var from : String = ""
    @SerializedName("geolocation")
    var geolocation : String = ""
    @SerializedName("latitude")
    var latitude : String = ""
    @SerializedName("longitude")
    var longitude : String = ""
    @SerializedName("pin")
    var pin : String = ""
    @SerializedName("retailerCoupon")
    var retailerCoupon : Boolean
    @SerializedName("source")
    var source : String = "SDK"
    

    constructor(category: String,couponCode: String,from: String,geolocation: String,latitude: String,longitude: String,retailerCoupon: Boolean,pin: String) {
        this.category = category
        this.couponCode = couponCode
        this.from = from
        this.geolocation = geolocation
        this.latitude = latitude
        this.longitude = longitude
        this.retailerCoupon = retailerCoupon
        this.pin = pin
        this.source = "SDK"
    }

}