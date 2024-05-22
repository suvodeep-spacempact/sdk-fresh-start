package com.vgretailersdk
data class PaymentDetails(
    val amount: String,
    val bankDetail: BankDetail
)

data class BankDetail(
    val bankAccHolderName: String,
    val bankAccNo: String,
    val bankAccType: String,
    val bankIfsc: String,
    val bankNameAndBranch: String,
    val checkPhoto: String
)
