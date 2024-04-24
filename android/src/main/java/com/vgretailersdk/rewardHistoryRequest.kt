package com.vgretailersdk

class RewardHistoryRequest{
    var mode: Array<String> = emptyArray()
    var status: Array<String> = emptyArray()
    var fromDate : String = ""
    var toDate : String = ""
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
        val validModes = listOf("paytm", "bank transfer", "upi")
        val validStatus = listOf("pending", "success", "failed")
    }
}