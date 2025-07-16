package com.vgretailersdk

import com.google.gson.annotations.SerializedName

data class PaymentDetails(
    @SerializedName("amount")
    val amount: String,
    @SerializedName("bankDetail")
    val bankDetail: BankDetail
)

data class BankDetail(
    @SerializedName("bankAccHolderName")
    val bankAccHolderName: String,
    @SerializedName("bankAccNo")
    val bankAccNo: String,
    @SerializedName("bankAccType")
    val bankAccType: String,
    @SerializedName("bankIfsc")
    val bankIfsc: String,
    @SerializedName("bankNameAndBranch")
    val bankNameAndBranch: String,
    @SerializedName("checkPhoto")
    val checkPhoto: String
)
