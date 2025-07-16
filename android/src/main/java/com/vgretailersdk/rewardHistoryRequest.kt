package com.vgretailersdk

import com.google.gson.annotations.SerializedName

class RewardHistoryRequest{
    @SerializedName("mode")
    var mode: Array<String> = emptyArray()
    @SerializedName("status")
    var status: Array<String> = emptyArray()
    @SerializedName("fromDate")
    var fromDate : String = ""
    @SerializedName("toDate")
    var toDate : String = ""
    @SerializedName("userId")
    var userId : String = ""

    constructor(mode: Array<String>, status: Array<String>, fromDate: String = "", toDate: String = "", userId: String = "") {
        this.mode = mode
        this.status = status
        this.fromDate = fromDate
        this.toDate = toDate
        this.userId = userId
    }

    init {
        require(mode.all { it in validModes }) { "Invalid mode specified" }
        require(status.all { it in validStatus }) { "Invalid mode specified" }
    }

    companion object {
        @SerializedName("validModes")
        val validModes = listOf("paytm", "bank transfer", "upi")
        @SerializedName("validStatus")
        val validStatus = listOf("pending", "success", "failed")
    }
}