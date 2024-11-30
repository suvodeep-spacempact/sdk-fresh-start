package com.vgretailersdk

import com.google.gson.annotations.SerializedName

class GetUserScanHistoryRequest{
    @SerializedName("status")
    var status: Array<String> = emptyArray()
    @SerializedName("scanType")
    var scanType: String = ""
    @SerializedName("fromDate")
    var fromDate: String = ""
    @SerializedName("couponCode")
    var couponCode: String = ""
    @SerializedName("toDate")
    var toDate: String = ""

    constructor(status: Array<String>,scanType: String,fromDate: String,couponCode: String,toDate: String ) {
        this.status = status
        this.scanType = scanType
        this.fromDate = fromDate
        this.couponCode = couponCode
        this.toDate = toDate
    }

}