package com.vgretailersdk

class GetUserScanHistoryRequest{
    var status: Array<String> = emptyArray()
    var scanType: String = ""
    var fromDate: String = ""
    var couponCode: String = ""


    constructor(status: Array<String>,scanType: String,fromDate: String,couponCode: String ) {
        this.status = status
        this.scanType = scanType
        this.fromDate = fromDate
        this.couponCode = couponCode
    }

}